from ..base import db
import json


class FeatureFlags(db.Model):
    _id: str = db.Column(db.Text, primary_key=True)
    value: str = db.Column(db.Text)

    def __init__(self, id: str):
        self._id = id
        self.value = "{}"

    @property
    def as_json(self):
        print(self.value)
        print(json.loads(self.value))
        return {
            "_id": self._id,
            "value": json.loads(self.value),
        }
