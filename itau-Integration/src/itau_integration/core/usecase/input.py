from datetime import datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field, field_validator


class TransactionInput(BaseModel):
    model_config = ConfigDict(extra="ignore")

    amount: Decimal
    createdAt: datetime
    documentNumber: str = Field(min_length=1)
    creditCardToken: str = Field(min_length=1)

    @field_validator("documentNumber", "creditCardToken")
    @classmethod
    def reject_blank_values(cls, value: str) -> str:
        if not value.strip():
            raise ValueError("O campo não pode estar em branco.")
        return value

    @field_validator("createdAt")
    @classmethod
    def require_timezone(cls, value: datetime) -> datetime:
        if value.tzinfo is None or value.utcoffset() is None:
            raise ValueError("createdAt deve incluir o fuso horário.")
        return value
