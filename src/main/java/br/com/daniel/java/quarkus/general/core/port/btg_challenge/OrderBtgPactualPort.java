package br.com.daniel.java.quarkus.general.core.port.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;

import java.util.Optional;
import java.util.UUID;

public interface OrderBtgPactualPort {
    Optional<OrderBtgPactual> getOrderById(UUID id);

    OrderBtgPactual saveOrder(OrderBtgPactual orderBtgPactual);
}
