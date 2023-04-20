import json

from app.db import db
from app.db.mutations.util import commit
from app.db.queries.feature import get_features

# pylint: disable=E1101

def update_features(new_features:str):
    u = get_features()
    u.value = new_features
    commit()
