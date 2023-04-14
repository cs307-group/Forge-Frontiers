from app.internal.helpers.guard import guard
from ..schemas.user import User
from ..schemas.shop import Shop
from sqlalchemy import or_
import email_validator

message = "User does not exist"


def get_user_by_username(idx: str) -> User:
    # email validator??
    try:
        email_validator.validate_email(idx)
    except Exception:
        return guard(None, message)
    if not idx:
        return guard(None, message)
    return guard(User.query.filter_by(user=idx).first(), message)


def get_user_by_id(idx: str) -> User:
    return guard(User.query.filter_by(id_=idx).first(), message)


def search(idx: str, user_id: str) -> list[User]:
    if not idx:
        return guard(None, message)
    p = f"%{idx}%"

    args = (
        (or_(User.name.ilike(p), User.mc_user == idx),)
        if user_id is None
        else (or_(User.name.ilike(p), User.mc_user == idx), User.id_ != user_id)
    )
    return User.query.filter(*args).all()


def get_shop_by_mc(mc_id: str) -> list[Shop]:
    return [x.as_json for x in Shop.query.filter(Shop.lister_player_id == mc_id).all()]
