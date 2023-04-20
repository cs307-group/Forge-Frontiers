from app.internal.helpers.guard import guard
from ..schemas.feature_flags import FeatureFlags
from sqlalchemy import or_

def get_features():
	res: FeatureFlags = FeatureFlags.query.filter_by(_id="root_config")
	out = res.as_json
	print(out)
	return out