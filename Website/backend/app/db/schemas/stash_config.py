from ..base import db
import json


class StashConfig(db.Model):
    __tablename__ = "stash_config"

    _id: str = db.Column(db.Text, primary_key=True)
    stash_id: str = db.Column(db.Text)
    friendly_name: str = db.Column(db.Text)
    block_material: str = db.Column(db.Text)
    costs: str = db.Column(db.Text)
    contents: str = db.Column(db.Text)

    def __init__(
        self,
        _id=None,
        stash_id=None,
        friendly_name=None,
        block_material=None,
        costs=None,
        contents=None,
    ):
        self._id = _id
        self.stash_id = stash_id
        self.friendly_name = friendly_name
        self.block_material = block_material
        self.costs = json.dumps(costs)
        self.contents = json.dumps(contents)

    @property
    def as_json(self):
        return {
            "_id": self._id,
            "stash_id": self.stash_id,
            "friendly_name": self.friendly_name,
            "block_material": self.block_material,
            "costs": json.loads(self.costs),
            "contents": json.loads(self.contents),
        }
