package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualNotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Singleton
@Slf4j
public class OrderBtgPactualGetsUseCaseImpl implements OrderBtgPactualGetsUseCase {

    @Inject
    OrderBtgPactualPort orderBtgPactualPort;

    @Override
    public OrderBtgPactualOutput getById(String id) {
        return OrderBtgPactualOutput.buildFrom(
                orderBtgPactualPort.getOrderById(UUID.fromString(id))
                        .orElseThrow(() ->
                                new OrderBtgPactualNotFoundException(
                                        "Nenhum Pedido localizado por meio do ID: %s.".formatted(id)
                                )
                        )
        );
    }
}
