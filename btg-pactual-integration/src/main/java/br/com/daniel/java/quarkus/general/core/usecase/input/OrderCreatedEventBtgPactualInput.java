package br.com.daniel.java.quarkus.general.core.usecase.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record OrderCreatedEventBtgPactualInput(@NotNull(message = "codigoPedido é obrigatório") @JsonProperty("codigoPedido") UUID orderId,
                                               @NotNull(message = "codigoCliente é obrigatório") @JsonProperty("codigoCliente") UUID customerId,
                                               @Valid @JsonProperty("itens") List<OrderItemCreatedEventBtgPactualInput> items
) {
    public OrderCreatedEventBtgPactualInput {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("codigoPedido é obrigatório");
        }

        if (Objects.isNull(customerId)) {
            throw new IllegalArgumentException("codigoCliente é obrigatório");
        }

        if (Objects.isNull(items)) {
            throw new IllegalArgumentException("itens do pedido são obrigatórios");
        }

        items = List.copyOf(items);
    }
}
