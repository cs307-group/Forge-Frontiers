import json

from app.db.mutations.util import commit
from app.db.queries.feature import get_features

# pylint: disable=E1101


def update_all_features(new_features: str):
    u = get_features()
    u.value = json.dumps(new_features)
    commit()
