from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.bazaar import (
    get_current_bazar_state,
    get_current_marketplace_lookup,
    get_lookup_count,
    get_orders_for_slot,
)
from app.internal.context import Context

router = Blueprint("market", __name__, url_prefix="/market")


def _min(x, key):
    y = [*x]
    if not y:
        return []
    return min(y, key=key)


@router.get("/")
@api.none
def api_list_marketplace():
    c = Context()
    q = c.args.get("q")
    res = {
        "bazaar": get_current_bazar_state(),
        "lookup": get_current_marketplace_lookup(q),
    }
    res["cheapest"] = {}
    selling = [x for x in res["bazaar"] if not x["order_type"]]
    for slot in res["lookup"]:
        res["cheapest"][slot["slot_id"]] = _min(
            (x for x in selling if x["slot_id"] == slot["slot_id"]),
            key=lambda x: x["price"],
        )
    res["counts"] = get_lookup_count()
    return res


@router.get("/<int:idx>")
@api.none
def api_view_slot(idx: int):
    return get_orders_for_slot(idx)
