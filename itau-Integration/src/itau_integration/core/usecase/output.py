from pydantic import BaseModel, ConfigDict, Field


class TransactionOutput(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    id: str
    transaction_id: str = Field(alias="transactionId")
    amount: str
    user_document: str | None = Field(alias="userDocument")
    credit_card_token: str | None = Field(alias="creditCardToken")
    created_at: str = Field(alias="createdAt")


class StatisticsItauOutput(BaseModel):
    count: int
    sum: float
    avg: float
    min: float
    max: float
