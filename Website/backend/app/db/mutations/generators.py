from math import floor
from json import loads, dumps
from app.db.schemas.generator_instances import GeneratorInstances
from app.db.schemas.stash_instances import StashInstance
from time import time
from sqlalchemy import and_

generator_data__keep_updated = {
    "silver-gen": {
        "resource": "SilverIngot",
        "levels": {
            "0": {"generation_rate": 10000, "max_size": 256},
            "1": {"generation_rate": 9000, "max_size": 512},
        },
    },
    "coin-gen": {
        "resource": "coin",
        "levels": {
            "0": {"generation_rate": 3000, "max_size": 10000},
            "1": {"generation_rate": 2000, "max_size": 10000},
        },
    },
}


def update_generator_collect_time(island_id: str):
    res: list[GeneratorInstances] = GeneratorInstances.query.filter_by(
        island_id=island_id
    ).all()
    resource_collected = {}

    for i in res:
        _time = int(time() * 1000)
        prev = i.last_collection_time
        i.last_collection_time = _time
        if i.generator_id not in resource_collected:
            resource_collected[i.generator_id] = 0
        resource_collected[i.generator_id] += min(
            (_time - prev)
            / generator_data__keep_updated[i.generator_id]["levels"][f"{i.level}"][
                "generation_rate"
            ],
            generator_data__keep_updated[i.generator_id]["levels"][f"{i.level}"][
                "max_size"
            ],
        )
    for k, v in resource_collected.items():
        resource_collected[k] = floor(v)
    return resource_collected


def update_stash_stats(island_id: str, vals: dict[str, int]):
    stashes: list[StashInstance] = StashInstance.query.filter(
        and_(StashInstance.island_id == island_id)
    ).all()
    for _k, v in vals.items():
        k = generator_data__keep_updated[_k]["resource"]
        for s in stashes:
            cjs = loads(s.contents_json)
            if k not in cjs:
                continue
            new_size = v + cjs[k]
            s.contents_json = dumps({**cjs, k: new_size})
            break
    return [x.as_json for x in stashes]
