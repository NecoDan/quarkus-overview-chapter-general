from __future__ import annotations

import logging
from datetime import datetime, timedelta
from decimal import Decimal, ROUND_HALF_UP

from itau_integration.config.encryption import EncryptionManager
from itau_integration.core.domain.transaction import TransactionItau
from itau_integration.core.port.transaction_port import TransactionMemoryPort, TransactionPort
from itau_integration.core.usecase.input import TransactionInput
from itau_integration.core.usecase.output import StatisticsItauOutput, TransactionOutput
from itau_integration.exceptions import TransactionNotFoundError, TransactionValidationError

logger = logging.getLogger(__name__)


def _java_string(value: object | None) -> str:
    return "null" if value is None else str(value)


def _format_amount(value: Decimal | None) -> str:
    if value is None:
        raise TransactionValidationError("O valor da transação é obrigatório.")
    return str(value.quantize(Decimal("0.01"), rounding=ROUND_HALF_UP))


def transaction_output(transaction: TransactionItau) -> TransactionOutput:
    created_at = (
        transaction.created_at.strftime("%d/%m/%Y %H:%M:%S")
        if transaction.created_at is not None
        else ""
    )
    return TransactionOutput(
        id=_java_string(transaction.id),
        transactionId=_java_string(transaction.transaction_id),
        amount=_format_amount(transaction.amount),
        userDocument=transaction.raw_user_document,
        creditCardToken=transaction.raw_credit_card_token,
        createdAt=created_at,
    )


def _local_datetime_now() -> datetime:
    return datetime.now().astimezone().replace(tzinfo=None)


class TransactionUseCases:
    def __init__(
        self,
        memory_port: TransactionMemoryPort,
        persistent_port: TransactionPort,
        encryption: EncryptionManager,
    ) -> None:
        self._memory_port = memory_port
        self._persistent_port = persistent_port
        self._encryption = encryption

    def create_transaction(self, transaction_input: TransactionInput) -> TransactionOutput:
        return self._create(transaction_input, self._memory_port)

    def create_persistent_transaction(
        self, transaction_input: TransactionInput
    ) -> TransactionOutput:
        return self._create(transaction_input, self._persistent_port)

    def _create(
        self,
        transaction_input: TransactionInput,
        port: TransactionMemoryPort | TransactionPort,
    ) -> TransactionOutput:
        logger.info("Inicializando processamento de criação de transação.")
        self._validate(transaction_input)
        try:
            transaction = TransactionItau.from_input(transaction_input, self._encryption)
            return transaction_output(port.create_transaction(transaction))
        except TransactionValidationError:
            raise
        except Exception as error:
            logger.exception("Falha ao criar a transação.")
            raise RuntimeError(f"Falha ao criar a transação: {error}") from error

    @staticmethod
    def _validate(transaction_input: TransactionInput) -> None:
        if (
            not transaction_input.amount.is_finite()
            or transaction_input.amount <= Decimal("0")
        ):
            raise TransactionValidationError("O valor da transação deve ser maior que zero.")
        if TransactionUseCases.is_created_at_invalid(transaction_input):
            raise TransactionValidationError(
                "A data/hora de criação da transação é obrigatória. "
                "Data e hora maiores que a data atual não são permitidas."
            )

    @staticmethod
    def is_created_at_invalid(transaction_input: TransactionInput) -> bool:
        return transaction_input.createdAt > datetime.now().astimezone()

    def get_all(self) -> list[TransactionOutput]:
        return [transaction_output(item) for item in self._persistent_port.get_all_transactions()]

    def get_by_id(self, transaction_id: str) -> TransactionOutput:
        try:
            numeric_id = int(transaction_id)
        except ValueError as error:
            raise TransactionNotFoundError(
                f"Nenhuma transação encontrada por meio do id da transação {transaction_id}."
            ) from error
        transaction = self._persistent_port.get_by_id(numeric_id)
        if transaction is None:
            raise TransactionNotFoundError(
                f"Nenhuma transação encontrada por meio do id da transação {transaction_id}."
            )
        return transaction_output(transaction)

    def delete_by_id(self, transaction_id: str) -> None:
        self._memory_port.delete_by_transaction_id(transaction_id)

    def delete_all(self) -> None:
        self._memory_port.delete_all()

    def get_recent(self, seconds_range: int) -> list[TransactionOutput]:
        range_start = _local_datetime_now() - timedelta(seconds=seconds_range)
        transactions = self._memory_port.get_transactions_by_datetime(range_start)
        if not transactions:
            raise TransactionNotFoundError(
                f"Não foram encontradas transações para o intervalo de {seconds_range} segundos."
            )
        return [transaction_output(item) for item in transactions]


class StatisticsUseCase:
    def __init__(self, memory_port: TransactionMemoryPort) -> None:
        self._memory_port = memory_port

    def calculate(self, seconds_range: int) -> StatisticsItauOutput:
        range_start = _local_datetime_now() - timedelta(seconds=seconds_range)
        transactions = self._memory_port.get_transactions_by_datetime(range_start)
        amounts = [float(item.amount) for item in transactions if item.amount is not None]
        if not amounts:
            return StatisticsItauOutput(count=0, sum=0.0, avg=0.0, min=0.0, max=0.0)

        total = sum(amounts)
        average = total / len(amounts)
        return StatisticsItauOutput(
            count=len(amounts),
            sum=total,
            avg=average,
            min=min(amounts),
            max=average,
        )
