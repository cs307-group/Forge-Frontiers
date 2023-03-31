from secrets import token_urlsafe
from time import time

from sqlalchemy.dialects.postgresql import TEXT

from ..base import db


class Shop(db.Model):
    # pylint: disable=E1101
    id_: str = db.Column(TEXT, unique=True, nullable=False, primary_key=True)
    item_id: str = db.Column(TEXT, unique=False, nullable=False)
    item_material: str = db.Column(db.String(100), unique=True, nullable=False)
    item_name: str = db.Column(db.String(100), nullable=False)
    item_lore: str = db.Column(TEXT, nullable=False)
    player_name: str = db.Column(TEXT, nullable=False)
    # DO NOT store as float, we don't want floating point errors
    price: str = db.Column(TEXT, nullable=False, default="0")
    amount: int = db.Column(db.BigInteger, default=0)
    lister_player_id = db.Column(TEXT, nullable=False)
    buyer_id: str = db.Column(TEXT, nullable=True)
    date_sold: int = db.Column(db.BigInteger)
    created_at: int = db.Column(db.BigInteger)
    custom_data: str = db.Column(TEXT)

    # pylint: enable=E1101

    def __init__(
        self,
        item_id: str,
        item_material: str,
        item_name: str,
        item_lore: str,
        price: str,
        amount: int,
        listed_player_id: str,
        date_sold: int = None,
        buyer_id: str = None,
        custom_data: str = None,
    ):
        self.id_ = token_urlsafe(20)
        self.item_id = item_id
        self.item_material = item_material
        self.item_name = item_name
        self.item_lore = item_lore
        self.price = price
        self.amount = amount
        self.lister_player_id = listed_player_id
        self.buyer_id = buyer_id
        self.date_sold = date_sold
        self.created_at = time()
        self.custom_data = custom_data

    @property
    def as_json(self):
        return {
            "id_": self.id_,
            "item_material": self.item_material,
            "item_name": self.item_name,
            "item_lore": self.item_lore,
            "price": self.price,
            "amount": self.amount,
            "lister_player_id": self.lister_player_id,
            "buyer_id": self.buyer_id,
            "date_sold": self.date_sold,
            "custom_data": self.custom_data,
        }
