from sqlalchemy import func
from app.db.schemas.generator_instances import GeneratorInstances


def get_generators_for_island(island_id: str):
    print(island_id)
    res: list[GeneratorInstances] = GeneratorInstances.query.filter_by(
        island_id=island_id
    ).all()
    out = [x.as_json for x in res]
    print(out)
    return out
