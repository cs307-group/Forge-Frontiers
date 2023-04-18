from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.shop import get_all_shops

router = Blueprint("admin", __name__, url_prefix="/admin")


@router.get("/shops")
@api.admin
def view_all_shops():
    return get_all_shops()
