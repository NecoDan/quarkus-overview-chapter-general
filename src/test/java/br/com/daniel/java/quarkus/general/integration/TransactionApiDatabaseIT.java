package br.com.daniel.java.quarkus.general.integration;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.repository.TransactionItauRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class TransactionApiDatabaseIT {

    @Inject
    TransactionItauRepository repository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsTransactionThroughApiAndReadsItFromDatabase() {
        var payload = """
                {
                  "amount": 125.50,
                  "createdAt": "2026-09-16T18:00:00-03:00",
                  "documentNumber": "12345678900",
                  "creditCardToken": "token-integration-test"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/itau/transactions/v2")
                .then()
                .statusCode(201)
                .body("amount", equalTo("125.50"))
                .body("userDocument", equalTo("12345678900"))
                .body("creditCardToken", equalTo("token-integration-test"));

        given()
                .when()
                .get("/itau/transactions/v1")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].amount", equalTo("125.50"))
                .body("[0].userDocument", equalTo("12345678900"));
    }

    @Test
    void rejectsInvalidTransactionBeforePersistingIt() {
        var payload = """
                {
                  "amount": null,
                  "createdAt": "2026-09-16T18:00:00-03:00",
                  "documentNumber": "",
                  "creditCardToken": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/itau/transactions/v2")
                .then()
                .statusCode(400);

        given()
                .when()
                .get("/itau/transactions/v1")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}
