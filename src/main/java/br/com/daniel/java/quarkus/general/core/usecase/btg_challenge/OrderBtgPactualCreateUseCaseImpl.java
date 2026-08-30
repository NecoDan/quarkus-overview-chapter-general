package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderCreatedBtgPactualOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class OrderBtgPactualCreateUseCaseImpl implements OrderBtgPactualCreateUseCase {

    @Inject
    OrderBtgPactualPort orderBtgPactualPort;

    @Override
    public OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input) {
        log.info("Inicializando fluxo para criação de Pedido. Payload: {}", input);

        try {
            var orderBtgPactual = new OrderBtgPactual(input);

            var orderBtgPactualSaved = orderBtgPactualPort.saveOrder(orderBtgPactual)
                    .orElseThrow(() ->
                            new OrderBtgPactualCreateFailedException("Falha ao salvar novo Pedido")
                    );

            return OrderCreatedBtgPactualOutput.from(orderBtgPactualSaved.getOrderId());
        } catch (Exception e) {
            log.error("Erro ao criar um novo Pedido. Payload: {}. Erro: {}", input, e.getMessage());
            throw new OrderBtgPactualCreateFailedException("Erro ao criar um novo Pedido. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), e
            );
        }
    }
}
