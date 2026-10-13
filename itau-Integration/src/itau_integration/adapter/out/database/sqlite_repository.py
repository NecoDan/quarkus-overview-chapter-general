from __future__ import annotations

import sqlite3
import threading
from pathlib import Path

from itau_integration.adapter.out.entities.transaction_entity import TransactionItauEntity


class TransactionItauRepository:
    def __init__(self, database_path: str | Path) -> None:
        self._lock = threading.RLock()
        self._connection = sqlite3.connect(
            str(database_path), check_same_thread=False, isolation_level=None
        )
        self._connection.row_factory = sqlite3.Row
        with self._connection:
            self._connection.execute(
                """
                CREATE TABLE IF NOT EXISTS tb_itau_transaction (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_transacao TEXT NOT NULL UNIQUE,
                    num_documento_usuario TEXT,
                    token_cartao_credito TEXT,
                    valor_transacao INTEGER,
                    quantia TEXT NOT NULL,
                    dt_criacao TEXT NOT NULL
                )
                """
            )

    def list_all(self) -> list[sqlite3.Row]:
        with self._lock:
            return list(
                self._connection.execute(
                    "SELECT * FROM tb_itau_transaction ORDER BY id"
                ).fetchall()
            )

    def list_page(self, page_size: int) -> list[sqlite3.Row]:
        if page_size <= 0:
            raise ValueError("page_size deve ser maior que zero.")
        with self._lock:
            return list(
                self._connection.execute(
                    "SELECT * FROM tb_itau_transaction ORDER BY id LIMIT ?",
                    (page_size,),
                ).fetchall()
            )

    def list_ordered_by_amount(self) -> list[sqlite3.Row]:
        with self._lock:
            return list(
                self._connection.execute(
                    """
                    SELECT * FROM tb_itau_transaction
                    ORDER BY CAST(quantia AS REAL) DESC, id
                    """
                ).fetchall()
            )

    def find_by_id(self, transaction_id: int) -> sqlite3.Row | None:
        with self._lock:
            return self._connection.execute(
                "SELECT * FROM tb_itau_transaction WHERE id = ?", (transaction_id,)
            ).fetchone()

    def list_created_after(self, created_after: str) -> list[sqlite3.Row]:
        with self._lock:
            return list(
                self._connection.execute(
                    "SELECT * FROM tb_itau_transaction WHERE dt_criacao > ? ORDER BY id",
                    (created_after,),
                ).fetchall()
            )

    def persist(self, entity: TransactionItauEntity) -> int:
        with self._lock:
            cursor = self._connection.execute(
                """
                INSERT INTO tb_itau_transaction (
                    id_transacao, num_documento_usuario, token_cartao_credito,
                    valor_transacao, quantia, dt_criacao
                ) VALUES (?, ?, ?, ?, ?, ?)
                """,
                (
                    entity.transaction_id,
                    entity.encrypted_user_document,
                    entity.encrypted_credit_card_token,
                    entity.transaction_value,
                    str(entity.amount),
                    entity.created_at.isoformat(timespec="microseconds"),
                ),
            )
            return int(cursor.lastrowid)

    def delete_by_id(self, transaction_id: int) -> None:
        with self._lock:
            self._connection.execute(
                "DELETE FROM tb_itau_transaction WHERE id = ?", (transaction_id,)
            )

    def delete_all(self) -> None:
        with self._lock:
            self._connection.execute("DELETE FROM tb_itau_transaction")

    def close(self) -> None:
        with self._lock:
            self._connection.close()
