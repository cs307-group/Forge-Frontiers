from sqlalchemy.dialects.postgresql import TEXT

from ..base import db

# GeneratorInstanceId: int (primary key),
# GeneratorId: int,
# Level: int,
# LastCollectionTime: long
# CollectedAmt: int
# LocationX: int
# LocationY: int
# LocationZ: int
# LocationWorld: string
# OwnerUUID: uuid (string


class BazaarRedeem(db.Model):
    # pylint: disable=E1101
    order_id: str = db.Column(TEXT, primary_key=True)
    player_id: str = db.Column(TEXT)
    item_id: int = db.Column(db.BigInteger)
    amount: int = db.Column(db.BigInteger)

    # pylint: enable=E1101

    def __init__(self, order_id, player_id, item_id, amount):
        self.order_id = order_id
        self.player_id = player_id
        self.item_id = item_id
        self.amount = amount

    @property
    def as_json(self):
        return {
            "order_id": self.order_id,
            "player_id": self.player_id,
            "item_id": self.item_id,
            "amount": self.amount,
        }
