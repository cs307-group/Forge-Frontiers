from app.internal.helpers.guard import guard
from app.db.schemas.links import Links

message = "Code not found.. did you run /ls?"


def get_link_by_link_code(code: str) -> Links:
    if not code:
        return guard(None, message)

    return guard(Links.query.filter_by(link_code=code).first(), message)
