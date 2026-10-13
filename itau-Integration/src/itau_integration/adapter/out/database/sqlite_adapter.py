from __future__ import annotations

import sqlite3
from datetime import datetime
from decimal import Decimal
from uuid import uuid4

from itau_integration.adapter.out.database.sqlite_repository import TransactionItauRepository
from itau_integration.adapter.out.entities.transaction_entity import TransactionItauEntity
from itau_integration.config.encryption import EncryptionManager
from itau_integration.core.domain.transaction import TransactionItau


class TransactionItauAdapter:
    def __init__(
        self, repository: TransactionItauRepository, encryption: EncryptionManager
    ) -> None:
        self._repository = repository
        self._encryption = encryption

    def get_all_transactions(self) -> list[TransactionItau]:
        return [self._from_row(row) for row in self._repository.list_all()]

    def get_all_pageable(self, page_size: int) -> list[TransactionItau]:
        return [self._from_row(row) for row in self._repository.list_page(page_size)]

    def get_all_order_by_amount(self) -> list[TransactionItau]:
        return [
            self._from_row(row) for row in self._repository.list_ordered_by_amount()
        ]

    def get_transactions_by_datetime(self, range_start: datetime) -> list[TransactionItau]:
        return [
            self._from_row(row)
            for row in self._repository.list_created_after(range_start.isoformat())
        ]

    def create_transaction(self, transaction: TransactionItau) -> TransactionItau:
        transaction.transaction_id = str(uuid4())
        entity = TransactionItauEntity.from_domain(transaction)
        transaction.id = self._repository.persist(entity)
        return transaction

    def create_transaction_by(self, transaction: TransactionItau) -> None:
        self.create_transaction(transaction)

    def get_by_id(self, transaction_id: int) -> TransactionItau | None:
        row = self._repository.find_by_id(transaction_id)
        return self._from_row(row) if row is not None else None

    def delete_by_id(self, transaction_id: int) -> None:
        self._repository.delete_by_id(transaction_id)

    def delete_all(self) -> None:
        self._repository.delete_all()

    def _from_row(self, row: sqlite3.Row) -> TransactionItau:
        return TransactionItau.from_persisted(
            id=row["id"],
            transaction_id=row["id_transacao"],
            amount=Decimal(row["quantia"]),
            encrypted_user_document=row["num_documento_usuario"],
            encrypted_credit_card_token=row["token_cartao_credito"],
            transaction_value=row["valor_transacao"],
            created_at=datetime.fromisoformat(row["dt_criacao"]),
            encryption=self._encryption,
        )
