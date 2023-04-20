import json

from flask import Blueprint, request

from app.db.queries.feature import get_features
from app.decorators.api_response import api

router = Blueprint("feature", __name__, url_prefix="/feature")


@router.get("/list")
@api.none
def view_all_features():
    temp = get_features()
    print(temp)
    return temp
