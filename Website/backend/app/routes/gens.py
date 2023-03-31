from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.generators import (
    get_generators_for_island
)

router = Blueprint("generators", __name__, url_prefix="/generators")

@router.get("/<str:island_id>")
@api.none
def api_get_gens(island_id: str):
    print(island_id)
    return get_generators_for_island(island_id)
