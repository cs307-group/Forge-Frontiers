from app.internal.helpers.guard import guard
from ..schemas.user import User
import email_validator

message = "User does not exist"


def get_user_by_username(idx: str) -> User:
    # email validator??
    try:
        email_validator.validate_email(idx)
    except Exception as _:
        return guard(None, message)
    if not idx:
        return guard(None, message)
    return guard(User.query.filter_by(user=idx).first(), message)


def get_user_by_id(idx: str) -> User:
    return guard(User.query.filter_by(id_=idx).first(), message)
