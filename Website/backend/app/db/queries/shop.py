from ..schemas.shop import Shop


def get_all_shops():
    return [x.as_json for x in Shop.query.all()]
