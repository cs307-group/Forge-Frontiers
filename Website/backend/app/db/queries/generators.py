from sqlalchemy import func
from app.db.schemas.generator_instances import GeneratorInstances
from app.db.schemas.stash_instances import StashInstance


def get_generators_for_island(island_id: str):
    res: list[GeneratorInstances] = GeneratorInstances.query.filter_by(
        island_id=island_id
    ).all()
    out = [x.as_json for x in res]
    return out

def get_stash_for_island(island_id: str):
    res: list[StashInstance] = StashInstance.query.filter_by(
        island_id=island_id
    ).all()
    out = [x.as_json for x in res]
    return out

