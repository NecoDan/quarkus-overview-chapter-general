package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderCreatedBtgPactualOutput;

public interface OrderBtgPactualCreateUseCase {
    OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input);

    void createOrderFrom(OrderCreatedEventBtgPactualInput input);
}
