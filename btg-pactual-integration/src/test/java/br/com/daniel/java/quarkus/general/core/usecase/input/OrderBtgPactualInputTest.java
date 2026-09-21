package br.com.daniel.java.quarkus.general.core.usecase.input;

import br.com.daniel.java.quarkus.general.util.factory.OrderBtgPactualFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderBtgPactualInputTest {

    @Test
    void shouldCreateOrderBtgPactualInputWhenAllFieldsAreValid() {
        final var orderId = UUID.randomUUID();
        final var customerId = UUID.randomUUID();

        assertDoesNotThrow(() -> new OrderBtgPactualInput(orderId,
                        customerId,
                        OrderBtgPactualFactory.buildMockOrderInputItemsRecord()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        final var customerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new OrderBtgPactualInput(null,
                        customerId,
                        OrderBtgPactualFactory.buildMockOrderInputItemsRecord()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        final var orderId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new OrderBtgPactualInput(
                        orderId,
                        null,
                        OrderBtgPactualFactory.buildMockOrderInputItemsRecord()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenItemsIsNull() {
        var orderId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new OrderBtgPactualInput(orderId,
                        customerId,
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenItemsContainNullElement() {
        var orderId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        var items = OrderBtgPactualFactory.buildMockOrderInputItemsRecord();
        items.add(null);

        assertThrows(NullPointerException.class,
                () -> new OrderBtgPactualInput(orderId, customerId, items)
        );
    }
}