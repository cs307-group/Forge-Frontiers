from app.db.schemas import FishingRarities


def get_fishing_roll_chances():
    return [x.as_json for x in FishingRarities.query.all()]
