from flask import Blueprint
from app.decorators.api_response import api
from app.db.queries.generators import get_generators_for_island, get_stash_for_island
from app.db.mutations.generators import (
    generator_data__keep_updated,
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
    }
    # print(res)
    return res


@router.get("/config")
@api.none
def get_generator_config():
    return generator_data__keep_updated


@router.get("/update/<island_id>")
@api.strict
def api_collect_gen(island_id: str):
    data = update_generator_collect_time(island_id)
    stashes = update_stash_stats(island_id, data)
    db.session.commit()
    return {"generators": data, "stashes": stashes}
