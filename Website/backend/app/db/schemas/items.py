from ..base import db


class Item(db.Model):
    __tablename__ = "items"

    _id: str = db.Column(db.Text, primary_key=True)
    item_id: str = db.Column(db.Text)
    material: str = db.Column(db.Text)
    name: str = db.Column(db.Text)
    lore: str = db.Column(db.Text)

    def __init__(
        self,
        _id=None,
        item_id=None,
        material=None,
        name=None,
        lore=None,
    ):
        self._id = _id
        self.item_id = item_id
        self.material = material
        self.name = name
        self.lore = lore

    @property
    def as_json(self):
        return {
            "_id": self._id,
            "item_id": self.item_id,
            "material": self.material,
            "name": self.name,
            "lore": self.lore,
        }
