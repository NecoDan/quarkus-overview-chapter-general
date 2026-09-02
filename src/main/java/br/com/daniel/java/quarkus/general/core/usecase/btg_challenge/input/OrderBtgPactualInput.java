package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderBtgPactualInput(@NotNull @JsonProperty("codigoPedido") UUID orderId,
                                   @NotNull @JsonProperty("codigoCliente") UUID customerId,
                                   @JsonProperty("itens") List<OrderItemBtgPactualInput> items
) {
}
