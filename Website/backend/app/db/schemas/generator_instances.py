from secrets import token_urlsafe

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
    level: int = db.Column(db.BigInteger, default=-1)
    last_collection_time: int = db.Column(db.BigInteger)

    location_x: int = db.Column(db.BigInteger)
    location_y: int = db.Column(db.BigInteger)
    location_z: int = db.Column(db.BigInteger)
    location_world: str = db.Column(TEXT)
    generator_id: str = db.Column(TEXT)
    island_id: str = db.Column(TEXT)

    # pylint: enable=E1101

    def __init__(
        self,
        level: int = None,
        last_collection_time: int = None,
        location_x: int = None,
        location_y: int = None,
        location_z: int = None,
        location_world: str = None,
        generator_id: str = None,
        island_id: str = None,
    ):
        self.id_ = token_urlsafe(20)
        self.level = level
        self.last_collection_time = last_collection_time

        self.location_x = location_x
        self.location_y = location_y
        self.location_z = location_z
        self.location_world = location_world
        self.generator_id = generator_id
        self.island_id = island_id

    @property
    def as_json(self):
        return {
            "id_": self.id_,
            "level": self.level,
            "last_collection_time": self.last_collection_time,
            "location_x": self.location_x,
            "location_y": self.location_y,
            "location_z": self.location_z,
            "location_world": self.location_world,
        }
