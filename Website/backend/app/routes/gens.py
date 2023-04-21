from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.generators import (
    get_generators_for_island,
    get_stash_for_island,
    get_generator_config,
    get_stash_config,
)
from app.db.mutations.generators import (
    update_generator_collect_time,
    update_stash_stats,
)
from app.db.base import db

router = Blueprint("generators", __name__, url_prefix="/generators")


@router.get("/<island_id>")
@api.none
def api_get_gens(island_id: str):
    res = {
        "generators": get_generators_for_island(island_id),
        "stashes": get_stash_for_island(island_id),
        "stash_config": get_stash_config(),
    }
    # print(res)
    return res


@router.get("/config")
@api.none
def api_get_generator_config():
    return get_generator_config()


@router.get("/update/<island_id>")
@api.strict
def api_collect_gen(island_id: str):
    cfg = get_generator_config()
    data, gen_instances = update_generator_collect_time(island_id, cfg)
    stashes = update_stash_stats(island_id, data, cfg, gen_instances)
    db.session.commit()
    return {"generators": data, "stashes": stashes}
