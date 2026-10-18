from datetime import datetime, timedelta, timezone
from decimal import Decimal

from fastapi.testclient import TestClient

from itau_integration.main import create_app


def transaction_payload(amount: str = "10.50") -> dict[str, str]:
    created_at = (datetime.now(timezone.utc) - timedelta(seconds=1)).isoformat()
    return {
        "amount": amount,
        "createdAt": created_at,
        "documentNumber": "12345678900",
        "creditCardToken": "card-token",
        "ignoredProperty": "ignored",
    }


def test_memory_creation_and_statistics(tmp_path) -> None:
    app = create_app(str(tmp_path / "transactions.db"), "test-password")
    client = TestClient(app)

    response = client.post("/itau/transactions/v1", json=transaction_payload())

    assert response.status_code == 201
    assert response.json()["amount"] == "10.50"
    assert response.json()["userDocument"] == "12345678900"
    assert response.json()["createdAt"]

    statistics = client.get("/itau/statistics/v1/", params={"intervaloBusca": 60})

    assert statistics.status_code == 200
    assert statistics.json() == {
        "count": 1,
        "sum": 10.5,
        "avg": 10.5,
        "min": 10.5,
        "max": 10.5,
    }


def test_persistent_creation_round_trips_and_encrypts_sensitive_fields(tmp_path) -> None:
    database_path = tmp_path / "transactions.db"
    app = create_app(str(database_path), "test-password")
    client = TestClient(app)

    created = client.post("/itau/transactions/v2", json=transaction_payload("20.25"))

    assert created.status_code == 201
    transaction_id = created.json()["id"]
    assert transaction_id.isdigit()
    assert client.get("/itau/transactions/v1").json()[0]["creditCardToken"] == "card-token"
    assert client.get(f"/itau/transactions/v1/{transaction_id}").json()["amount"] == "20.25"

    import sqlite3

    with sqlite3.connect(database_path) as connection:
        stored_values = connection.execute(
            "SELECT num_documento_usuario, token_cartao_credito FROM tb_itau_transaction"
        ).fetchone()

    assert "12345678900" not in stored_values[0]
    assert "card-token" not in stored_values[1]


def test_create_rejects_non_positive_amount_and_future_timestamp(tmp_path) -> None:
    app = create_app(str(tmp_path / "transactions.db"), "test-password")
    client = TestClient(app)

    negative_amount = client.post(
        "/itau/transactions/v1", json=transaction_payload("-1")
    )
    future_payload = transaction_payload()
    future_payload["createdAt"] = (datetime.now(timezone.utc) + timedelta(minutes=1)).isoformat()
    future_date = client.post("/itau/transactions/v1", json=future_payload)

    assert negative_amount.status_code == 422
    assert future_date.status_code == 422


def test_create_rejects_non_finite_amount(tmp_path) -> None:
    app = create_app(str(tmp_path / "transactions.db"), "test-password")
    client = TestClient(app)

    response = client.post(
        "/itau/transactions/v1", json=transaction_payload("NaN")
    )

    assert response.status_code == 422


def test_memory_delete_requires_existing_transaction(tmp_path) -> None:
    app = create_app(str(tmp_path / "transactions.db"), "test-password")
    client = TestClient(app)
    created = client.post("/itau/transactions/v1", json=transaction_payload())

    deleted = client.delete(f"/itau/transactions/v1/{created.json()['transactionId']}")
    missing = client.delete("/itau/transactions/v1/not-found")

    assert deleted.status_code == 204
    assert missing.status_code == 404


def test_amount_rounding_matches_java_half_up() -> None:
    from itau_integration.core.usecase.transaction_use_cases import _format_amount

    assert _format_amount(Decimal("1.235")) == "1.24"


def test_statistics_preserve_java_max_field_behavior(tmp_path) -> None:
    app = create_app(str(tmp_path / "transactions.db"), "test-password")
    client = TestClient(app)
    for amount in ("10.00", "20.00", "30.00"):
        client.post("/itau/transactions/v1", json=transaction_payload(amount))

    statistics = client.get("/itau/statistics/v1/", params={"intervaloBusca": 60})

    assert statistics.json() == {
        "count": 3,
        "sum": 60.0,
        "avg": 20.0,
        "min": 10.0,
        "max": 20.0,
    }
