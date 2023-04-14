from set_env import setup_env

setup_env()
# pylint: disable=unused-wildcard-import
from app.main import app  # noqa: E402
from app.main import *  # noqa: E402, F403
from app.db.schemas import *  # noqa: E402, F403
from app.models.user import *  # noqa: F403, E402

app.app_context().push()
