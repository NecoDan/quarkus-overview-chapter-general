package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public record OrderCustomerBtgPactualOutput(@JsonProperty("idCliente") String customerId) {

    public static OrderCustomerBtgPactualOutput createFrom(OrderCustomerBtgPactual customer) {
        return new OrderCustomerBtgPactualOutput(customer.getCustomerId());
    }
}
