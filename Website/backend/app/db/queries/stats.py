from app.internal.helpers.guard import guard
from app.db.schemas.stats import Stats

message = "MC User not found"


def get_stats_by_player_uuid(uuid: str) -> Stats:
    if not uuid:
        return guard(None, message)
    return guard(Stats.query.filter_by(player_uuid=uuid).first(), message)
