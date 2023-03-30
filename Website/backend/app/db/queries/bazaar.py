from sqlalchemy import func
from sqlalchemy import or_
from app.db.schemas.bazaar_lookup import BazaarLookup
from app.db.schemas.bazaar_orders import BazaarOrders

from ..base import db


def get_current_bazar_state():
    orders: list[BazaarOrders] = BazaarOrders.query.all()
    return [x.as_json for x in orders]


def get_current_marketplace_lookup(q: str):
    if not q:
        res: list[BazaarLookup] = BazaarLookup.query.all()
    else:
        p = f"%{q}%"
        res: list[BazaarLookup] = BazaarLookup.query.filter(
            or_(
                BazaarLookup.item_name.ilike(p),
                BazaarLookup.item_material.ilike(p),
                BazaarLookup.item_lore.ilike(p),
            )
        ).all()
    return [x.as_json for x in res]


def get_lookup_count():
    return {
        k: int(v)
        for k, v in (
            db.session.query(
                BazaarOrders.slot_id, func.sum(BazaarOrders.amount).label("amount_sum")
            )
            .group_by(BazaarOrders.slot_id)
            .all()
        )
    }


def get_orders_for_slot(i: int):
    res: list[BazaarOrders] = BazaarOrders.query.filter_by(slot_id=i).all()
    ret = {"buy": [], "sell": []}
    for item in res:
        if item.order_type:
            # buy order
            ret["buy"].append(item.as_json)
        else:
            ret["sell"].append(item.as_json)
    lookup = BazaarLookup.query.filter_by(slot_id=i).first()
    ret["lookup"] = lookup.as_json
    return ret
