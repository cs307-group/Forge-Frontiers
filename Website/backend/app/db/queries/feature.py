from app.internal.helpers.guard import guard
from ..schemas.FeatureFlags import FeatureFlags
from sqlalchemy import or_

def get_features() -> FeatureFlags:
    x = guard(FeatureFlags.query.filter_by(id_="root_config").first())
		print(x)
		return x