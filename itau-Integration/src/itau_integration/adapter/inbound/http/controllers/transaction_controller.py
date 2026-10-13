from fastapi import APIRouter, HTTPException, Query, Response, status

from itau_integration.core.usecase.input import TransactionInput
from itau_integration.core.usecase.output import StatisticsItauOutput, TransactionOutput
from itau_integration.core.usecase.transaction_use_cases import (
    StatisticsUseCase,
    TransactionUseCases,
)
from itau_integration.exceptions import (
    TransactionNotFoundError,
    TransactionValidationError,
)


def create_transaction_router(use_cases: TransactionUseCases) -> APIRouter:
    router = APIRouter(prefix="/itau/transactions", tags=["Itaú transactions"])

    @router.post("/v1", response_model=TransactionOutput, status_code=status.HTTP_201_CREATED)
    def create(transaction_input: TransactionInput) -> TransactionOutput:
        try:
            return use_cases.create_transaction(transaction_input)
        except TransactionValidationError as error:
            raise HTTPException(status_code=422, detail=str(error)) from error

    @router.post("/v2", response_model=TransactionOutput, status_code=status.HTTP_201_CREATED)
    def create_persistent(transaction_input: TransactionInput) -> TransactionOutput:
        try:
            return use_cases.create_persistent_transaction(transaction_input)
        except TransactionValidationError as error:
            raise HTTPException(status_code=422, detail=str(error)) from error

    @router.get("/v1", response_model=list[TransactionOutput])
    def get_all() -> list[TransactionOutput]:
        return use_cases.get_all()

    @router.get("/v1/{transaction_id}", response_model=TransactionOutput)
    def get_by_id(transaction_id: str) -> TransactionOutput:
        try:
            return use_cases.get_by_id(transaction_id)
        except TransactionNotFoundError as error:
            raise HTTPException(status_code=404, detail=str(error)) from error

    @router.delete("/v1/{transaction_id}", status_code=status.HTTP_204_NO_CONTENT)
    def delete_by_id(transaction_id: str) -> Response:
        try:
            use_cases.delete_by_id(transaction_id)
        except TransactionNotFoundError as error:
            raise HTTPException(status_code=404, detail=str(error)) from error
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    @router.delete("/v1/", status_code=status.HTTP_204_NO_CONTENT)
    def delete_all() -> Response:
        try:
            use_cases.delete_all()
        except TransactionNotFoundError as error:
            raise HTTPException(status_code=404, detail=str(error)) from error
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    return router


def create_statistics_router(use_case: StatisticsUseCase) -> APIRouter:
    router = APIRouter(prefix="/itau/statistics", tags=["Itaú statistics"])

    @router.get("/v1/", response_model=StatisticsItauOutput)
    def get_statistics(
        intervaloBusca: int = Query(..., description="Intervalo de busca em segundos.")
    ) -> StatisticsItauOutput:
        return use_case.calculate(intervaloBusca)

    return router
