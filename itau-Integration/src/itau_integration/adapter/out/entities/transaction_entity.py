from __future__ import annotations

from dataclasses import dataclass
from datetime import datetime
from decimal import Decimal

from itau_integration.core.domain.transaction import TransactionItau


@dataclass(frozen=True)
class TransactionItauEntity:
    id: int | None
    transaction_id: str
    encrypted_user_document: str | None
    encrypted_credit_card_token: str | None
    transaction_value: int | None
    amount: Decimal
    created_at: datetime

    @classmethod
    def from_domain(cls, transaction: TransactionItau) -> TransactionItauEntity:
        if transaction.amount is None or transaction.created_at is None:
            raise ValueError("amount e created_at são obrigatórios para persistir a transação.")
        if transaction.transaction_id is None:
            raise ValueError("transaction_id deve ser gerado antes da persistência.")
        return cls(
            id=transaction.id,
            transaction_id=transaction.transaction_id,
            encrypted_user_document=transaction.encrypted_user_document,
            encrypted_credit_card_token=transaction.encrypted_credit_card_token,
            transaction_value=transaction.transaction_value,
            amount=transaction.amount,
            created_at=transaction.created_at,
        )
