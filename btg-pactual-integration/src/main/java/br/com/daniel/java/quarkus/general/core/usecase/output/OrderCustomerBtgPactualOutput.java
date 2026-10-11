package br.com.daniel.java.quarkus.general.core.usecase.output;

import br.com.daniel.java.quarkus.general.core.domain.OrderCustomerBtgPactual;
import com.fasterxml.jackson.annotation.JsonProperty;


public record OrderCustomerBtgPactualOutput(@JsonProperty("idCliente") String customerId) {

    public static OrderCustomerBtgPactualOutput createFrom(OrderCustomerBtgPactual customer) {
        return new OrderCustomerBtgPactualOutput(customer.getCustomerId());
    }
}
