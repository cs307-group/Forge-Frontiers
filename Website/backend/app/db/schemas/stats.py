from sqlalchemy.dialects.postgresql import TEXT
from ..base import db


class Stats(db.Model):
    # pylint: disable=E1101
    player_uuid: str = db.Column(
        TEXT,
        unique=True,
        nullable=False,
        primary_key=True,
    )
    current_health: float = db.Column(db.Float)
    HP: int = db.Column(db.Integer)
    ATK: int = db.Column(db.Integer)
    STR: int = db.Column(db.Integer)
    DEX: int = db.Column(db.Integer)
    CRATE: int = db.Column(db.Integer)
    CDMG: int = db.Column(db.Integer)
    DEF: int = db.Column(db.Integer)

    # pylint: enable=E1101

    def __init__(
        self,
        player_uuid: str = None,
        current_health: float = None,
        HP: int = None,
        ATK: int = None,
        STR: int = None,
        DEX: int = None,
        CRATE: int = None,
        CDMG: int = None,
        DEF: int = None,
    ):
        self.player_uuid = player_uuid
        self.current_health = current_health
        self.HP = HP
        self.ATK = ATK
        self.STR = STR
        self.DEX = DEX
        self.CRATE = CRATE
        self.CDMG = CDMG
        self.DEF = DEF

    @property
    def as_json(self):
        return {
            "player_uuid": self.player_uuid,
            "current_health": self.current_health,
            "HP": self.HP,
            "ATK": self.ATK,
            "STR": self.STR,
            "DEX": self.DEX,
            "CRATE": self.CRATE,
            "CDMG": self.CDMG,
            "DEF": self.DEF,
        }
