from ..base import db
import json


class GeneratorConfig(db.Model):
    __tablename__ = "generator_config"

    _id: str = db.Column(db.Text, primary_key=True)
    generator_id: str = db.Column(db.Text)
    friendly_name: str = db.Column(db.Text)
    block_material: str = db.Column(db.Text)
    resource_item_id: str = db.Column(db.Text)
    costs: str = db.Column(db.Text)
    levels: str = db.Column(db.Text)

    def __init__(
        self,
        _id=None,
        generator_id=None,
        friendly_name=None,
        block_material=None,
        resource_item_id=None,
        costs=None,
        levels=None,
    ):
        self._id = _id
        self.generator_id = generator_id
        self.friendly_name = friendly_name
        self.block_material = block_material
        self.resource_item_id = resource_item_id
        self.costs = json.dumps(costs)
        self.levels = json.dumps(levels)

    @property
    def as_json(self):
        return {
            "_id": self._id,
            "generator_id": self.generator_id,
            "friendly_name": self.friendly_name,
            "block_material": self.block_material,
            "resource_item_id": self.resource_item_id,
            "costs": json.loads(self.costs),
            "levels": json.loads(self.levels),
        }
