package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderTotalAmountValueBtgPactualOutput(@JsonProperty("idPedido") String id,
                                                    @JsonProperty("idPedidoExterno") String orderId,
                                                    @JsonProperty("valorTotalPedido") BigDecimal totalAmount
) {

    public static OrderTotalAmountValueBtgPactualOutput buildFrom(String id,
                                                                  String orderId,
                                                                  BigDecimal totalAmount) {
        return new OrderTotalAmountValueBtgPactualOutput(id, orderId, totalAmount);
    }
}
   