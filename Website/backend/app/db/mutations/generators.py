from math import floor
from json import loads, dumps
from app.db.schemas.generator_instances import GeneratorInstances
from app.db.schemas.stash_instances import StashInstance
from app.db.schemas.stash_config import StashConfig
from time import time
from sqlalchemy import and_
from app.internal.constants import IS_PROD


def update_generator_collect_time(island_id: str, cfg):
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
            (_time - prev) / cfg[i.generator_id]["levels"][i.level]["generation_rate"],
            cfg[i.generator_id]["levels"][i.level]["max_size"],
        )
    for k, v in resource_collected.items():
        resource_collected[k] = floor(v)
    return [resource_collected, res]


def populate_caches(stash_id: str, config_cache: dict, query_cache: dict):
    if stash_id not in config_cache:
        stash_config: StashConfig = query_cache.get(
            stash_id, StashConfig.query.filter_by(stash_id=stash_id).first()
        )
        query_cache[stash_id] = stash_config
        stash_config_as_json = {}

        if stash_config:
            stash_config_as_json_arr = loads(stash_config.contents)
            for item in stash_config_as_json_arr:
                stash_config_as_json[item["item_id"]] = item
        config_cache[stash_id] = stash_config_as_json


def update_stash_stats(
    island_id: str,
    vals: dict[str, int],
    cfg,
    res_inst: list[GeneratorInstances],
):
    stashes: list[StashInstance] = StashInstance.query.filter(
        and_(StashInstance.island_id == island_id)
    ).all()
    query_cache: dict[str, StashConfig] = {}
    config_cache: dict[str, dict] = {}
    remaining = {cfg[k]["resource"]: v for k, v in vals.items()}
    for _k, v in vals.items():
        k = cfg[_k]["resource"]
        for s in stashes:
            if not v:
                break
            cjs = loads(s.contents_json)
            if k not in cjs:
                continue
            populate_caches(s.stash_id, config_cache, query_cache)
            config = config_cache.get(s.stash_id, {}).get(k) or {}
            max_amt = config.get("max_amount")
            if not max_amt:
                if not IS_PROD:
                    print(
                        f"skipping {k=} in {s.id_=}"
                        f"as it's not in {config=} ({max_amt=})"
                    )
                continue

            new_size = min(cjs[k] + v, max_amt)
            if new_size == cjs[k]:
                print(f"skipping {k=} {new_size=} {cjs[k]=} {v=} ")
                continue

            print(f"Setting new_size to {new_size=} from {cjs[k]=} for {s.id_=}")
            v -= new_size - cjs[k]
            s.contents_json = dumps({**cjs, k: new_size})
        remaining[k] = v
    t = time() * 1000
    print(remaining)
    for res in res_inst:
        k = cfg[res.generator_id]["resource"]
        if remaining[k] <= 0:
            continue
        res.last_collection_time = (
            t
            - remaining[k]
            * cfg[res.generator_id]["levels"][res.level]["generation_rate"]
        )
    return [x.as_json for x in stashes]
