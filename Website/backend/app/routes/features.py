import json

from flask import Blueprint, request

from app.db.queries.feature import get_features
from app.decorators.api_response import api

router = Blueprint("admin", __name__, url_prefix="/control-panel/feature")


@router.get("/control-panel/feature")
@api.admin
def view_all_features():
		temp = get_features()
		print(temp)
    return temp