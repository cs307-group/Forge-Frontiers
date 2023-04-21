from app.db.schemas import (
    GeneratorConfig,
    GeneratorInstances,
    StashInstance,
    StashConfig,
)


def get_generators_for_island(island_id: str):
    res: list[GeneratorInstances] = GeneratorInstances.query.filter_by(
        island_id=island_id
    ).all()
    out = [x.as_json for x in res]
    return out


def get_stash_for_island(island_id: str):
    res: list[StashInstance] = StashInstance.query.filter_by(island_id=island_id).all()
    out = [x.as_json for x in res]
    return out


def get_stash_config():
    return [x.as_json for x in StashConfig.query.all()]


def get_generator_config():
    cfg: list[GeneratorConfig] = GeneratorConfig.query.all()
    ret = {}
    for item in cfg:
        ret[item.generator_id] = item.as_json
        ret[item.generator_id]["resource"] = item.resource_item_id
    return ret
