from __future__ import annotations

import secrets
import threading
from datetime import datetime
from uuid import uuid4

from itau_integration.core.domain.transaction import TransactionItau
from itau_integration.exceptions import TransactionCreateError, TransactionNotFoundError


class TransactionItauMemoryAdapter:
    def __init__(self) -> None:
        self._transactions: list[TransactionItau] = []
        self._lock = threading.RLock()

    def get_all_transactions(self) -> list[TransactionItau]:
        with self._lock:
            return list(self._transactions)

    def get_transactions_by_datetime(self, range_start: datetime) -> list[TransactionItau]:
        with self._lock:
            return [
                transaction
                for transaction in self._transactions
                if transaction.created_at is not None and transaction.created_at > range_start
            ]

    def create_transaction(self, transaction: TransactionItau) -> TransactionItau:
        with self._lock:
            transaction.id = secrets.randbelow(999_999) + 1
            transaction.transaction_id = str(uuid4())
            self._transactions.append(transaction)
            if self.get_by_transaction_id(transaction.transaction_id) is None:
                raise TransactionCreateError("Falha ao criar a transação.")
            return transaction

    def get_by_transaction_id(self, transaction_id: str) -> TransactionItau | None:
        with self._lock:
            return next(
                (
                    item
                    for item in self._transactions
                    if item.transaction_id == transaction_id
                ),
                None,
            )

    def delete_by_transaction_id(self, transaction_id: str) -> None:
        with self._lock:
            if self.get_by_transaction_id(transaction_id) is None:
                raise TransactionNotFoundError(
                    f"Nenhuma transação encontrada por meio do id da transação {transaction_id}."
                )
            self._transactions = [
                item for item in self._transactions if item.transaction_id != transaction_id
            ]

    def delete_all(self) -> None:
        with self._lock:
            if not self._transactions:
                raise TransactionNotFoundError(
                    "Nenhuma transação encontrada para ser deletada."
                )
            self._transactions.clear()
