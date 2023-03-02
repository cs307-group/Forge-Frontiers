from typing import TypeVar
from app.exceptions import AppException

G = TypeVar("G")


def guard(value: G, message: str = "Assertion Error"):
    if not value:
        raise AppException(message)
    return value
