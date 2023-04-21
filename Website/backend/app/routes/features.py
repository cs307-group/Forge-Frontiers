from flask import Blueprint

from app.internal.context import Context
from app.db.queries.feature import get_features
from app.db.mutations.feature import update_all_features
from app.decorators.api_response import api

router = Blueprint("feature", __name__, url_prefix="/feature")


@router.get("/list")
@api.admin
def view_all_features():
    temp = get_features()
    return temp.as_json


@router.post("/update")
@api.admin
def update():
    new_features = Context()
    update_all_features(new_features.json)
    return {}
