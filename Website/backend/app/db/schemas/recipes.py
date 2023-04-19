from ..base import db


class Recipe(db.Model):
    _id = db.Column(db.Integer, primary_key=True)
    mat1 = db.Column(db.String(50))
    mat2 = db.Column(db.String(50))
    mat3 = db.Column(db.String(50))
    mat4 = db.Column(db.String(50))
    mat5 = db.Column(db.String(50))
    mat6 = db.Column(db.String(50))
    mat7 = db.Column(db.String(50))
    mat8 = db.Column(db.String(50))
    mat9 = db.Column(db.String(50))
    type1 = db.Column(db.Text)
    type2 = db.Column(db.Text)
    type3 = db.Column(db.Text)
    type4 = db.Column(db.Text)
    type5 = db.Column(db.Text)
    type6 = db.Column(db.Text)
    type7 = db.Column(db.Text)
    type8 = db.Column(db.Text)
    type9 = db.Column(db.Text)
    amt1 = db.Column(db.Integer)
    amt2 = db.Column(db.Integer)
    amt3 = db.Column(db.Integer)
    amt4 = db.Column(db.Integer)
    amt5 = db.Column(db.Integer)
    amt6 = db.Column(db.Integer)
    amt7 = db.Column(db.Integer)
    amt8 = db.Column(db.Integer)
    amt9 = db.Column(db.Integer)

    def __init__(self) -> None:
        self.mat1 = None
        self.mat2 = None
        self.mat3 = None
        self.mat4 = None
        self.mat5 = None
        self.mat6 = None
        self.mat7 = None
        self.mat8 = None
        self.mat9 = None
        self.type1 = None
        self.type2 = None
        self.type3 = None
        self.type4 = None
        self.type5 = None
        self.type6 = None
        self.type7 = None
        self.type8 = None
        self.type9 = None
        self.amt1 = None
        self.amt2 = None
        self.amt3 = None
        self.amt4 = None
        self.amt5 = None
        self.amt6 = None
        self.amt7 = None
        self.amt8 = None
        self.amt9 = None

    @property
    def as_json(self):
        return {
            "mat1": self.mat1,
            "mat2": self.mat2,
            "mat3": self.mat3,
            "mat4": self.mat4,
            "mat5": self.mat5,
            "mat6": self.mat6,
            "mat7": self.mat7,
            "mat8": self.mat8,
            "mat9": self.mat9,
            "type1": self.type1,
            "type2": self.type2,
            "type3": self.type3,
            "type4": self.type4,
            "type5": self.type5,
            "type6": self.type6,
            "type7": self.type7,
            "type8": self.type8,
            "type9": self.type9,
            "amt1": self.amt1,
            "amt2": self.amt2,
            "amt3": self.amt3,
            "amt4": self.amt4,
            "amt5": self.amt5,
            "amt6": self.amt6,
            "amt7": self.amt7,
            "amt8": self.amt8,
            "amt9": self.amt9,
        }
