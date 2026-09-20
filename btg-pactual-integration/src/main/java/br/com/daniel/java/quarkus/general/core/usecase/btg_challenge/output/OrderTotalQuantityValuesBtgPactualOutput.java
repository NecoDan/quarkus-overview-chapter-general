package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderTotalQuantityValuesBtgPactualOutput(@JsonProperty("idCliente") UUID customerId,
                                                       @JsonProperty("totalPedidosFeitos") Integer totalQuantity,
                                                       @JsonProperty("valorTotal") BigDecimal totalAmount
) {

    public static OrderTotalQuantityValuesBtgPactualOutput buildFrom(UUID customerId,
                                                                     Integer totalQuantity,
                                                                     BigDecimal totalAmount) {
        return new OrderTotalQuantityValuesBtgPactualOutput(customerId, totalQuantity, totalAmount);
    }
}
   