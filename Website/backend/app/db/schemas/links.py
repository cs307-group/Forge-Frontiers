from secrets import token_urlsafe
from time import time
from sqlalchemy.dialects.postgresql import UUID, TEXT
from sqlalchemy import text
from uuid import uuid4
from ..base import db


class Links(db.Model):
    # pylint: disable=E1101
    link_id: str = db.Column(
        UUID(as_uuid=False),
        unique=True,
        nullable=False,
        primary_key=True,
        default=uuid4,
        server_default=text("public.gen_random_uuid()"),
    )
    player_uuid: str = db.Column(TEXT, unique=True, nullable=False)
    link_code: str = db.Column(TEXT, unique=True, nullable=False)
    bool_used: bool = db.Column(db.Boolean, default=False)

    # pylint: enable=E1101

    def __init__(self, player_uuid: str = None, link_code: str = None, bool_used=False):
        self.link_id = token_urlsafe(20)
        self.player_uuid = player_uuid
        self.link_code = link_code
        self.bool_used = bool_used

    @property
    def as_json(self):
        return {
            "link_id": self.link_id,
            "player_uuid": self.player_uuid,
            "link_code": self.link_code,
            "bool_used": self.bool_used,
        }
