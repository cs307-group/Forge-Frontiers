from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.bazaar import (
    get_current_bazar_state,
    get_current_marketplace_lookup,
    get_cheapest_per_lookup,
    get_lookup_count,
)

router = Blueprint("market", __name__, url_prefix="/market")


@router.get("/")
@api.none
def api_list_marketplace():
    res = {
        "bazaar": get_current_bazar_state(),
        "lookup": get_current_marketplace_lookup(),
    }
    res["cheapest"] = get_cheapest_per_lookup()
    res["counts"] = get_lookup_count()
    return res


