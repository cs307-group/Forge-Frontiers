from uuid import uuid4

from sqlalchemy import text

from ..base import db


class StashInstance(db.Model):
    __tablename__ = "stash_instances"

    id_: str = db.Column(
        db.Text, primary_key=True, unique=True, server_default=text("gen_random_uuid()")
    )
    stash_id: str = db.Column(db.Text)
    location_x: int = db.Column(db.BigInteger)
    location_y: int = db.Column(db.BigInteger)
    location_z: int = db.Column(db.BigInteger)
    location_world: str = db.Column(db.Text)
    contents_json: str = db.Column(db.Text)
    island_id: str = db.Column(db.Text)

    def __init__(
        self,
        stash_id: str = None,
        location_x: int = None,
        location_y: int = None,
        location_z: int = None,
        location_world: str = None,
        contents_json: str = None,
        island_id: str = None,
    ):
        self.id_ = str(uuid4())
        self.stash_id = stash_id
        self.location_x = location_x
        self.location_y = location_y
        self.location_z = location_z
        self.location_world = location_world
        self.contents_json = contents_json
        self.island_id = island_id

    @property
    def as_json(self):
        return {
            "id_": self.id_,
            "stash_id": self.stash_id,
            "location_x": self.location_x,
            "location_y": self.location_y,
            "location_z": self.location_z,
            "location_world": self.location_world,
            "contents_json": self.contents_json,
            "island_id": self.island_id,
        }
