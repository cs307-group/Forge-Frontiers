from math import floor
from json import loads, dumps
from app.db.schemas.generator_instances import GeneratorInstances
from app.db.schemas.stash_instances import StashInstance
from time import time
from sqlalchemy import and_
from app.db.queries.generators import get_generator_config


def update_generator_collect_time(island_id: str):
    res: list[GeneratorInstances] = GeneratorInstances.query.filter_by(
        island_id=island_id
    ).all()
    resource_collected = {}
    cfg = get_generator_config()
    for i in res:
        _time = int(time() * 1000)
        prev = i.last_collection_time
        i.last_collection_time = _time
        if i.generator_id not in resource_collected:
            resource_collected[i.generator_id] = 0
        resource_collected[i.generator_id] += min(
            (_time - prev) / cfg[i.generator_id]["levels"][i.level]["generation_rate"],
            cfg[i.generator_id]["levels"][i.level]["max_size"],
        )
    for k, v in resource_collected.items():
        resource_collected[k] = floor(v)
    return resource_collected


def update_stash_stats(island_id: str, vals: dict[str, int]):
    stashes: list[StashInstance] = StashInstance.query.filter(
        and_(StashInstance.island_id == island_id)
    ).all()
    cfg = get_generator_config()
    for _k, v in vals.items():
        k = cfg[_k]["resource"]
        for s in stashes:
            cjs = loads(s.contents_json)
            if k not in cjs:
                continue
            new_size = v + cjs[k]
            s.contents_json = dumps({**cjs, k: new_size})
            break
    return [x.as_json for x in stashes]
