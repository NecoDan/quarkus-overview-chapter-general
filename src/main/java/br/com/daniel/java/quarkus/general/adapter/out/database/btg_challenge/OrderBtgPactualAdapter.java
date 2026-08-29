package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository.OrderBtgPactualRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class OrderBtgPactualAdapter implements OrderBtgPactualPort {

    @Inject
    OrderBtgPactualRepository repositoryOrder;

    @Override
    public Optional<OrderBtgPactual> getOrderById(UUID id) {
        log.info("Buscar pedido por ID: {}", id);
        return repositoryOrder.findByIdOptional(id.toString())
                .map(OrderBtgPactual::new);
    }

    @Override
    public OrderBtgPactual saveOrder(OrderBtgPactual orderBtgPactual) {
        log.info("Salvando novo pedito no banco de dados. Payload: {}", orderBtgPactual);
        var orderEntity = new OrderBtgPactualEntity(orderBtgPactual);

        repositoryOrder.persist(orderEntity);
        return new OrderBtgPactual(orderEntity);
    }
}
