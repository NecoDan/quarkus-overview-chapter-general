package br.com.daniel.java.quarkus.general.util.factory;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class OrderBtgPactualFactory {

    private OrderBtgPactualFactory() {
        throw new IllegalStateException("Utility class OrderBtgPactualFactory");
    }

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
                .items(List.of(
                        OrderItemBtgPactual.builder()
                                .item(1)
                                .product("Product1")
                                .quantity(2)
                                .price(BigDecimal.valueOf(50))
                                .createdAt(LocalDateTime.now())
                                .build(),
                        OrderItemBtgPactual.builder()
                                .item(2)
                                .product("Product2")
                                .quantity(2)
                                .price(BigDecimal.valueOf(50))
                                .createdAt(LocalDateTime.now())
                                .build()
                ))
                .build();
    }

    public static OrderBtgPactual buildMockOrderNoItems() {
        return OrderBtgPactual.builder()
                .id(new ObjectId("6a983417c636a5818504159f"))
                .orderId(UUID.randomUUID().toString())
                .customer(OrderCustomerBtgPactual.builder()
                        .customerId(UUID.randomUUID().toString())
                        .build())
                .totalValue(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

}
