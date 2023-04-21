from app.db.schemas import FishingRarities
from app.db.mutations.util import commit


def update_fishing_roll_chances(updates):
    print(updates)
    for update in updates:
        obj = FishingRarities.query.filter_by(rarity=update).first()
        obj.chance = updates[update]

    commit()
