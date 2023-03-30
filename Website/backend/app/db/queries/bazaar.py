from sqlalchemy import func

from app.db.schemas.bazaar_lookup import BazaarLookup
from app.db.schemas.bazaar_orders import BazaarOrders

from ..base import db


def get_current_bazar_state():
    orders: list[BazaarOrders] = BazaarOrders.query.all()
    return [x.as_json for x in orders]


def get_current_marketplace_lookup():
    q: list[BazaarLookup] = BazaarLookup.query.all()
    return [x.as_json for x in q]


def get_cheapest_per_lookup():
    lowest_price_orders = (
        db.session.query(
            BazaarOrders.slot_id, func.min(BazaarOrders.price).label("min_price")
        )
        .group_by(BazaarOrders.slot_id)
        .subquery()
    )

    result = (
        db.session.query(BazaarOrders)
        .join(
            lowest_price_orders,
            db.and_(
                BazaarOrders.slot_id == lowest_price_orders.c.slot_id,
                BazaarOrders.price == lowest_price_orders.c.min_price,
            ),
        )
        .all()
    )
    return [x.as_json for x in result]


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
    return ret
