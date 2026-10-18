from __future__ import annotations

from dataclasses import dataclass
from datetime import datetime
from decimal import Decimal, ROUND_DOWN

from itau_integration.config.encryption import EncryptionManager
from itau_integration.core.usecase.input import TransactionInput


@dataclass
class TransactionItau:
    id: int | None = None
    transaction_id: str | None = None
    amount: Decimal | None = None
    encrypted_user_document: str | None = None
    encrypted_credit_card_token: str | None = None
    transaction_value: int | None = None
    created_at: datetime | None = None
    raw_user_document: str | None = None
    raw_credit_card_token: str | None = None

    @classmethod
    def from_input(
        cls, transaction_input: TransactionInput, encryption: EncryptionManager
    ) -> TransactionItau:
        amount = transaction_input.amount
        created_at = transaction_input.createdAt.replace(tzinfo=None)
        return cls(
            amount=amount,
            created_at=created_at,
            raw_user_document=transaction_input.documentNumber,
            raw_credit_card_token=transaction_input.creditCardToken,
            encrypted_user_document=encryption.encrypt(transaction_input.documentNumber),
            encrypted_credit_card_token=encryption.encrypt(transaction_input.creditCardToken),
            transaction_value=int(amount.to_integral_value(rounding=ROUND_DOWN)),
        )

    @classmethod
    def from_persisted(
        cls,
        *,
        id: int,
        transaction_id: str,
        amount: Decimal,
        encrypted_user_document: str | None,
        encrypted_credit_card_token: str | None,
        transaction_value: int | None,
        created_at: datetime | None,
        encryption: EncryptionManager,
    ) -> TransactionItau:
        return cls(
            id=id,
            transaction_id=transaction_id,
            amount=amount,
            encrypted_user_document=encrypted_user_document,
            encrypted_credit_card_token=encrypted_credit_card_token,
            transaction_value=transaction_value,
            created_at=created_at,
            raw_user_document=encryption.decrypt(encrypted_user_document),
            raw_credit_card_token=encryption.decrypt(encrypted_credit_card_token),
        )
