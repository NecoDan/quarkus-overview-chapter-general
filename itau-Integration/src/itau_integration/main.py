from __future__ import annotations

import os

from fastapi import FastAPI

from itau_integration.adapter.inbound.http.controllers.transaction_controller import (
    create_statistics_router,
    create_transaction_router,
)
from itau_integration.adapter.out.database.memory_adapter import TransactionItauMemoryAdapter
from itau_integration.adapter.out.database.sqlite_adapter import TransactionItauAdapter
from itau_integration.adapter.out.database.sqlite_repository import TransactionItauRepository
from itau_integration.config.encryption import EncryptionManager
from itau_integration.core.usecase.transaction_use_cases import (
    StatisticsUseCase,
    TransactionUseCases,
)


def create_app(
    database_path: str | None = None, encryption_key: str | None = None
) -> FastAPI:
    encryption = EncryptionManager(
        encryption_key
        if encryption_key is not None
        else os.getenv("APP_KEY_PWD_ENCRYPT")
    )
    repository = TransactionItauRepository(
        database_path or os.getenv("ITAU_DATABASE_PATH", "itau_transactions.sqlite3")
    )
    memory_adapter = TransactionItauMemoryAdapter()
    persistent_adapter = TransactionItauAdapter(repository, encryption)
    transaction_use_cases = TransactionUseCases(
        memory_adapter, persistent_adapter, encryption
    )
    statistics_use_case = StatisticsUseCase(memory_adapter)

    app = FastAPI(title="Integração Itaú", version="0.1.0")
    app.state.transaction_memory_adapter = memory_adapter
    app.state.transaction_repository = repository
    app.include_router(create_transaction_router(transaction_use_cases))
    app.include_router(create_statistics_router(statistics_use_case))
    return app


app = create_app()
