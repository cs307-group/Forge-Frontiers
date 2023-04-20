from app.internal.helpers.guard import guard
from ..schemas.FeatureFlags import FeatureFlags
from sqlalchemy import or_

def get_features() -> FeatureFlags:
    x = guard(FeatureFlags.query.filter_by(_id="root_config"))
		print(x)
		return x