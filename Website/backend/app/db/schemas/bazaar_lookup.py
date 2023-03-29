from ..base import db


class BazaarLookup(db.Model):
    __tablename__ = "bazaar_lookup"

    slot_id: int = db.Column(db.BigInteger, primary_key=True, unique=True)
    item_name: str = db.Column(db.Text)
    item_lore: str = db.Column(db.Text)
    item_material: str = db.Column(db.Text)
    custom_data: str = db.Column(db.Text)

    def __init__(
        self,
        slot_id=None,
        item_name=None,
        item_lore=None,
        item_material=None,
        custom_data=None,
    ):
        self.slot_id = slot_id
        self.item_name = item_name
        self.item_lore = item_lore
        self.item_material = item_material
        self.custom_data = custom_data

    @property
    def as_json(self):
        return {
            "slot_id": self.slot_id,
            "item_name": self.item_name,
            "item_lore": self.item_lore,
            "item_material": self.item_material,
            "custom_data": self.custom_data,
        }
