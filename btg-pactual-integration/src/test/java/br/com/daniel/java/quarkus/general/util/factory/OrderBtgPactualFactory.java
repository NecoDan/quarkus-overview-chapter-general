package br.com.daniel.java.quarkus.general.util.factory;

import br.com.daniel.java.quarkus.general.core.domain.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.OrderItemBtgPactual;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderItemCreatedEventBtgPactualInput;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class OrderBtgPactualFactory {

    private OrderBtgPactualFactory() {
        throw new IllegalStateException("Utility class OrderBtgPactualFactory");
    }

    public static final Faker FAKER_INSTANCE = Faker.instance();

    public static OrderBtgPactual buildMockOrderWithItems() {
        return OrderBtgPactual.builder()
                .id(new ObjectId("6a9833e9c636a5818504159e"))
                .orderId(UUID.randomUUID().toString())
                .customer(OrderCustomerBtgPactual.builder()
                        .customerId(UUID.randomUUID().toString())
                        .build())
                .totalValue(BigDecimal.valueOf(200.00))
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .items(getBuildMockItems())
                .build();
    }

    private static List<OrderItemBtgPactual> getBuildMockItems() {
        return List.of(
                OrderItemBtgPactual.builder()
                        .item(1)
                        .product(FAKER_INSTANCE.commerce().productName())
                        .quantity(2)
                        .price(BigDecimal.valueOf(50))
                        .createdAt(LocalDateTime.now())
                        .build(),
                OrderItemBtgPactual.builder()
                        .item(2)
                        .product(FAKER_INSTANCE.commerce().productName())
                        .quantity(2)
                        .price(BigDecimal.valueOf(50))
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public static OrderBtgPactual buildMockOrderNoItems() {
        return OrderBtgPactual.builder()
                .id(new ObjectId("6a983417c636a5818504159f"))
                .orderId(UUID.randomUUID().toString())
                .customer(OrderCustomerBtgPactual.builder()
                        .customerId(UUID.randomUUID().toString()).build())
                .totalValue(BigDecimal.ZERO).createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public static OrderCreatedEventBtgPactualInput buildMockEventOrderInputRecord(boolean isContainsItems) {
        var orderId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        return new OrderCreatedEventBtgPactualInput(orderId, customerId,
                isContainsItems ? buildMockEventOrderInputItemsRecord() : Collections.emptyList()
        );
    }

    public static List<OrderItemCreatedEventBtgPactualInput> buildMockEventOrderInputItemsRecord() {
        final var itemsTotalCrated = RandomUtils.secureStrong().randomInt(1, 20);

        List<OrderItemCreatedEventBtgPactualInput> itemsInputRecord = new ArrayList<>();
        for (int i = 0; i < itemsTotalCrated; i++) {
            var productName = FAKER_INSTANCE.commerce().productName();
            var quantity = RandomUtils.secureStrong().randomInt(1, 100);
            var price = BigDecimal.valueOf(RandomUtils.secureStrong().randomDouble(10, 100));

            itemsInputRecord.add(new OrderItemCreatedEventBtgPactualInput(productName, quantity, price));
        }

        return itemsInputRecord;
    }

    public static OrderBtgPactualInput buildMockOrderInputRecord(boolean isContainsItems) {
        var orderId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        return new OrderBtgPactualInput(orderId, customerId,
                isContainsItems ? buildMockOrderInputItemsRecord() : Collections.emptyList()
        );
    }

    public static List<OrderItemBtgPactualInput> buildMockOrderInputItemsRecord() {
        final var itemsTotalCrated = RandomUtils.secureStrong().randomInt(1, 20);

        List<OrderItemBtgPactualInput> itemsInputRecord = new ArrayList<>();
        for (int i = 0; i < itemsTotalCrated; i++) {
            var productName = FAKER_INSTANCE.commerce().productName();
            var quantity = RandomUtils.secureStrong().randomInt(1, 100);
            var price = BigDecimal.valueOf(RandomUtils.secureStrong().randomDouble(10, 100));

            itemsInputRecord.add(new OrderItemBtgPactualInput(productName, quantity, price));
        }

        return itemsInputRecord;
    }

}
