# pylint: disable=E0213
from typing import Optional

from pydantic import EmailStr, constr, validator
from pydantic.fields import Field
from pydantic.main import BaseModel

from app.internal.security.danger import generate_password_hash
from app.models.base import CustomBase


class UserSession(BaseModel):
    user_id: Optional[str]
    user: Optional[str]
    is_admin: bool


PasswordType = constr(min_length=4)


class AuthModel(BaseModel):
    user: EmailStr


class LoginModel(AuthModel):
    password: PasswordType


class LinkModel(BaseModel):
    code: str


class _UserBase(AuthModel):
    name: constr(strip_whitespace=True, max_length=100)


class UserEditable(_UserBase):
    pass


class UserIn(_UserBase):
    is_admin: bool
    password_hash: PasswordType

    @validator("password_hash")
    def validate_pw_hash(cls, password: str):
        return generate_password_hash(password)


class UserOut(CustomBase):
    id_ = str
    name: str
    mc_user: Optional[str]
    created_at: int
    is_admin: bool


class UserOutSecure(UserOut):
    secure: dict = Field(alias="_secure_")
