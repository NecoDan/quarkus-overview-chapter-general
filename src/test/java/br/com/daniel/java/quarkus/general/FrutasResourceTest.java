package br.com.daniel.java.quarkus.general;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class FrutasResourceTest {

    private static final String URI = "/frutas";

    @Test
    void testHelloEndpoint() {

        final var conteudo = "Hello RESTEasy";

        given()
                .when()
                .get(URI + "/hello")
                .then()
                .statusCode(200)
                .body(is(conteudo));
    }
}
