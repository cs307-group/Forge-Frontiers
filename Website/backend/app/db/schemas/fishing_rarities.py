from ..base import db


class FishingRarities(db.Model):
    _id = db.Column(db.Integer, primary_key=True)
    rarity = db.Column(db.TEXT, unique=True)
    chance = db.Column(db.Integer)

    def __init__(self, rarity: str, chance: str):
        self.rarity = rarity
        self.chance = chance

    @property
    def as_json(self):
        return {"_id": self._id, "rarity": self.rarity, "chance": self.chance}
