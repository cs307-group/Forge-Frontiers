from secrets import token_urlsafe
from time import time

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


class GeneratorInstances(db.Model):
    # pylint: disable=E1101
    id_: str = db.Column(TEXT, primary_key=True)
    level: int = db.Column(db.Integer, default=-1)
    last_collection_time: int = db.Column(db.Integer)
    collected_amount: int = db.Column(db.Integer)
    location_x: int = db.Column(db.Integer)
    location_y: int = db.Column(db.Integer)
    location_z: int = db.Column(db.Integer)
    location_world: str = db.Column(TEXT)
    owner_uuid: str = db.Column(TEXT)
    # pylint: enable=E1101

    def __init__(
        self,
        level: int = None,
        last_collection_time: int = None,
        collected_amount: int = None,
        location_x: int = None,
        location_y: int = None,
        location_z: int = None,
        location_world: str = None,
        owner_uuid: str = None,
    ):
        self.id_ = token_urlsafe(20)
        self.level = level
        self.last_collection_time = last_collection_time
        self.collected_amount = collected_amount
        self.location_x = location_x
        self.location_y = location_y
        self.location_z = location_z
        self.location_world = location_world
        self.owner_uuid = owner_uuid

    @property
    def as_json(self):
        return {
            "id_": self.id_,
            "level": self.level,
            "last_collection_time": self.last_collection_time,
            "collected_amount": self.collected_amount,
            "location_x": self.location_x,
            "location_y": self.location_y,
            "location_z": self.location_z,
            "location_world": self.location_world,
            "owner_uuid": self.owner_uuid,
        }
