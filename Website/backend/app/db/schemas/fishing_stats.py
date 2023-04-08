from ..base import db


class FishingStats(db.Model):
    __tablename__ = "fishing_stats"

    player_id: str = db.Column(db.Text, primary_key=True, unique=True)
    fishcaught: int = db.Column(db.BigInteger, nullable=True)
    fishlevel: int = db.Column(db.Integer, nullable=True)
    common: int = db.Column(db.Integer, nullable=True)
    uncommon: int = db.Column(db.Integer, nullable=True)
    rare: int = db.Column(db.Integer, nullable=True)
    sr: int = db.Column(db.Integer, nullable=True)
    ur: int = db.Column(db.Integer, nullable=True)
    legendary: int = db.Column(db.Integer, nullable=True, default=0)

    def __init__(
        self,
        player_id=None,
        fishcaught=None,
        fishlevel=None,
        common=None,
        uncommon=None,
        rare=None,
        sr=None,
        ur=None,
        legendary=None,
    ):
        self.player_id = player_id
        self.fishcaught = fishcaught
        self.fishlevel = fishlevel
        self.common = common
        self.uncommon = uncommon
        self.rare = rare
        self.sr = sr
        self.ur = ur
        self.legendary = legendary

    @property
    def as_json(self):
        return {
            "player_id": self.player_id,
            "fishcaught": self.fishcaught,
            "fishlevel": self.fishlevel,
            "common": self.common,
            "uncommon": self.uncommon,
            "rare": self.rare,
            "sr": self.sr,
            "ur": self.ur,
            "legendary": self.legendary,
        }
