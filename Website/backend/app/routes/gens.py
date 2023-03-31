from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.generators import (
    get_generators_for_island,
)

router = Blueprint("generators", __name__, url_prefix="/generators")


# @router.get("/")
# @api.none
# def api_list_marketplace():
#     res = {
#         "bazaar": get_current_bazar_state(),
#         "lookup": get_current_marketplace_lookup(),
#     }
#     res["cheapest"] = get_cheapest_per_lookup()
#     res["counts"] = get_lookup_count()
#     return res

@router.get("/<str:island_id>")
@api.none
def api_get_gens(island_id: str):
    x: get_generators_for_island(island_id)
	print(x)
	return x
