package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderCreatedBtgPactualOutput;

public interface OrderBtgPactualCreateUseCase {
    OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input);
}
