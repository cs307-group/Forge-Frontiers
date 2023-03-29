from app.models.user import UserSession
from flask import request, g
from typing import Callable, TypeVar, Generic

M = TypeVar("M")


class Context(Generic[M]):
    body: M

    def __init__(self, model: Callable[[], M] = None):
        self._reqest = request
        self.args = request.args
        if request.method.lower() in {"get", "head", "options"}:
            self.json = {}
        else:
            self.json = request.json or {}
        self.body = model(**self.json) if model else None
        self.headers = request.headers
        self.auth: UserSession = g._auth_state  # pylint: disable=E0237
