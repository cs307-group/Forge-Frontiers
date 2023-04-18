from secrets import token_urlsafe
from time import time

from sqlalchemy.dialects.postgresql import TEXT, JSONB

from ..base import db


class User(db.Model):
    # pylint: disable=E1101
    id_: str = db.Column(TEXT, unique=True, nullable=False, primary_key=True)

    # for reference: this is the EMAIL
    user: str = db.Column(TEXT, unique=True, nullable=False)
    mc_user: str = db.Column(TEXT, unique=True)
    name: str = db.Column(db.String(100), nullable=False)
    password_hash: str = db.Column(TEXT, nullable=False)
    created_at: int = db.Column(db.Integer)
    is_admin: bool = db.Column(db.Boolean, default=False)
    island_id: str = db.Column(db.TEXT)
    config: dict = db.Column(JSONB, default={})
    # pylint: enable=E1101

    def __init__(
        self,
        user: str = None,
        name: str = None,
        password_hash: str = None,
        mc_user: str = None,
        island_id: str = None,
        config: dict = {},
    ):
        self.id_ = token_urlsafe(20)
        self.user = user
        self.mc_user = mc_user
        self.name = name
        self.password_hash = password_hash
        self.created_at = time()
        self.island_id = island_id
        self.config = config

    @property
    def as_json(self):
        return {
            "id_": self.id_,
            "name": self.name,
            "created_at": self.created_at,
            "is_admin": self.is_admin,
            "mc_user": self.mc_user,
            "island_id": self.island_id,
            "_secure_": {"user": self.user, "config": self.config or {}},
        }
