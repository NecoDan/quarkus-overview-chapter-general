package br.com.daniel.java.quarkus.general.core.mappers.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderItemBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OrderBtgPactualStaticMapper {

    private OrderBtgPactualStaticMapper() {
        throw new IllegalStateException("This is a utility class OrderBtgPactualMapper and cannot be instantiated");
    }

    public static OrderBtgPactual buildOrderBtgPactual(OrderBtgPactualEntity entity,
                                                       boolean withItems) {
        return OrderBtgPactual.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .customer(OrderCustomerBtgPactual.builder()
                        .customerId(entity.getCustomer().getCustomerId())
                        .build())
                .totalValue(entity.getTotalValue())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdateAt())
                .items(withItems ? buildListOrderItemBtgPactual(entity.getItems()) : Collections.emptyList())
                .build();
    }

    public static List<OrderItemBtgPactual> buildListOrderItemBtgPactual(List<OrderItemBtgPactualEntity> items) {
        List<OrderItemBtgPactual> itemsList = new ArrayList<>();

        items.forEach(orderItemBtgPactualEntity -> itemsList.add(
                        buildOrderItemBtgPactual(orderItemBtgPactualEntity)
                )
        );

        return itemsList;
    }

    public static OrderItemBtgPactual buildOrderItemBtgPactual(OrderItemBtgPactualEntity itemEntity) {
        return OrderItemBtgPactual.builder()
                .item(itemEntity.getItem())
                .product(itemEntity.getProduct())
                .quantity(itemEntity.getQuantity())
                .price(itemEntity.getPrice())
                .totalItemValue(itemEntity.getTotalItemValue())
                .build();
    }
}
