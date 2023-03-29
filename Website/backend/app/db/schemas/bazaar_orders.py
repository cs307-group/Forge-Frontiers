from ..base import db
from datetime import datetime


class BazaarOrders(db.Model):
    __tablename__ = "bazaar_orders"

    order_id: str = db.Column(db.Text, primary_key=True, unique=True)
    order_type: bool = db.Column(db.Boolean)
    lister_id: str = db.Column(db.Text)
    slot_id: int = db.Column(db.BigInteger, db.ForeignKey("bazaar_lookup.slot_id"))
    item = db.relationship(
        "BazaarLookup", backref=db.backref("bazaar_orders", lazy=True)
    )
    amount: int = db.Column(db.BigInteger)
    price: float = db.Column(db.Float)
    listdate: datetime = db.Column(db.DateTime, nullable=True)

    def __init__(
        self,
        order_id: str = None,
        order_type: bool = None,
        lister_id: str = None,
        slot_id: int = None,
        amount: int = None,
        price: float = None,
        listdate: datetime = None,
    ):
        self.order_id = order_id
        self.order_type = order_type
        self.lister_id = lister_id
        self.slot_id = slot_id
        self.amount = amount
        self.price = price
        self.listdate = listdate

    @property
    def as_json(self):
        return {
            "order_id": self.order_id,
            "order_type": self.order_type,
            "lister_id": self.lister_id,
            "slot_id": self.slot_id,
            "amount": self.amount,
            "price": self.price,
            "listdate": self.listdate.isoformat(),
            "item": self.item.as_json,
        }
