from flask import Blueprint

from app.db.mutations.user import create_user
from app.db.mutations.util import commit
from app.db.queries.links import get_link_by_link_code
from app.db.queries.stats import get_stats_by_player_uuid
from app.db.queries.user import get_user_by_id, get_user_by_username, search
from app.decorators.api_response import api
from app.exceptions.app_exception import AppException
from app.internal.context import Context
from app.internal.helpers.json_response import json_response
from app.internal.security.auth_token import (
    authenticate,
    get_bearer_token,
    regenerate_access_token,
)
from app.internal.security.danger import create_token, decode_token
from app.models.user import (
    LinkModel,
    LoginModel,
    UserEditable,
    UserIn,
    UserOut,
    UserOutSecure,
)

router = Blueprint("user", __name__, url_prefix="/users")


@router.post("/-/register", strict_slashes=False)
@api.none
def register():
    req = Context()
    json = req.json
    pw = json.pop("password", None)
    user = json.pop("email", None)
    body = UserIn(**req.json, user=user, password_hash=pw, is_admin=False)
    js = create_user(body, return_json=True)
    return js


@router.post("/-/login")
@api.none
def login():
    req = Context(LoginModel)
    body = req.body
    access, refresh, user_data = authenticate(body.user, body.password)
    return json_response(
        {"data": user_data.as_json},
        headers={"x-access-token": access, "x-refresh-token": refresh},
    )


@router.get("/-/token/refresh")
@api.lax
def api_refresh_token():
    context = Context()
    headers = context.headers
    access_token = get_bearer_token(headers)
    decoded_access = decode_token(access_token)
    if decoded_access is None:
        refresh_token = headers.get("x-refresh-token")
        decoded_refresh = decode_token(refresh_token)
        access, refresh = regenerate_access_token(decoded_refresh)
        if access is None:
            raise AppException("re-auth")

        return json_response(
            {},
            headers={
                "x-access-token": create_token(access),
                "x-refresh-token": create_token(refresh),
            },
        )
    return {}


@router.get("/<user>/")
@api.strict
def user_details(user: str):
    req = Context()
    auth = req.auth
    is_me = user == "me"
    if is_me:
        if not auth.user:
            raise AppException("Not authenticated", 401)
        user = auth.user
    user_data = get_user_by_username(user)
    show_secure = user_data.user == auth.user or auth.is_admin
    model = (
        UserOutSecure.from_db(user_data) if show_secure else UserOut.from_db(user_data)
    )
    return {"user_data": model.dict()}


@router.get("/search")
@api.strict
def api_search_for_user():
    req = Context()
    q = req.args.get("q")
    if not q:
        raise AppException("Invalid")
    users = search(q)
    res = [UserOut.from_db(x).dict() for x in users]
    return res


@router.patch("/<user>/")
@api.strict
def edit(user: str):
    req = Context()
    if user != req.auth.user and not req.auth.is_admin:
        raise AppException("Not authorized to edit", 401)
    if not req.auth.is_admin:
        body = UserEditable(**req.json)
    else:
        body = UserIn(**req.json)
    user_data = get_user_by_username(user)
    user_data.user = body.user or user_data.user
    user_data.name = body.name or user_data.name
    json = user_data.as_json
    commit()
    return json


@router.post("/-/link/")
@api.strict
def link_mc_account():
    req = Context(LinkModel)
    code = req.body.code
    link = get_link_by_link_code(code)
    if link.bool_used:
        raise AppException("Code has already been used")
    link.bool_used = True
    user = req.auth.user_id
    user_data = get_user_by_id(user)
    user_data.mc_user = link.player_uuid
    js = user_data.as_json
    commit()
    return js


@router.get("/-/stats")
@api.strict
def get_mc_stats():
    req = Context()
    user = req.auth.user_id
    mc_user = get_user_by_id(user).mc_user
    if mc_user is not None:
        stats = get_stats_by_player_uuid(mc_user)
        return stats.as_json
    return {}
