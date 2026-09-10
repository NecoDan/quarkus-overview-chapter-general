package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.mappers.btg_challenge.OrderBtgPactualMapper;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderCreatedBtgPactualOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Singleton
@Slf4j
public class OrderBtgPactualCreateUseCaseImpl implements OrderBtgPactualCreateUseCase {

    @Inject OrderBtgPactualPort orderBtgPactualPort;

    @Inject OrderBtgPactualMapper orderBtgPactualMapper;

    @Override
    public OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input) {
        log.info("Inicializando fluxo para criação de Pedido. Payload: {}", input);

        try {
            var orderBtgPactual = new OrderBtgPactual(input);
            var orderBtgPactualSaved = orderBtgPactualPort.saveOrder(orderBtgPactual);

            return OrderCreatedBtgPactualOutput.from(orderBtgPactualSaved.get().getToStringId());
        } catch (Exception e) {
            log.error("Erro ao criar um novo Pedido. Payload: {}. Erro: {}", input, e.getMessage());
            throw new OrderBtgPactualCreateFailedException("Erro ao criar um novo Pedido. Payload: %s. Erro: %s".formatted(input, e.getMessage()), e);
        }
    }

    @Override
    public void createOrderFrom(OrderCreatedEventBtgPactualInput input) {
        log.info("Inicializando fluxo para criação de Pedido a partir do evento. Payload: {}", input);

        try {
            var optionalOrderBtgPactual = orderBtgPactualPort.getOrderByOrderIdExternal(input.orderId());

            if (optionalOrderBtgPactual.isPresent()) {
                var orderBtgPactualExists = optionalOrderBtgPactual.get();

                log.warn("Pedido existente e criado: idPedido{}: | idPedidoExterno: {} | idCliente: {}.", orderBtgPactualExists.getId(),
                        orderBtgPactualExists.getOrderId(), orderBtgPactualExists.getCustomer().getCustomerId()
                );

                createOrderFromExists(orderBtgPactualExists, input);
                return;
            }

            var orderBtgPactualInput = orderBtgPactualMapper.toOrderBtgPactualInput(input);
            var orderBtgPactual = new OrderBtgPactual(orderBtgPactualInput);

            orderBtgPactualPort.saveOrder(orderBtgPactual);
        } catch (Exception e) {
            log.error("Erro ao criar Pedido a partir do evento. Payload: {}. Erro: {}", input, e.getMessage());
            throw new OrderBtgPactualCreateFailedException("Erro ao criar Pedido a partir do evento. Payload: %s. Erro: %s".formatted(input, e.getMessage()), e);
        }
    }

    private void createOrderFromExists(OrderBtgPactual orderBtgPactualExists,
                                       OrderCreatedEventBtgPactualInput input) {

        orderBtgPactualExists.assignCustomerCode(input.customerId().toString());
        orderBtgPactualExists.assignDateUpdateAt();

        var orderItemBtgPactualList = orderBtgPactualMapper.toOrderItemBtgPactualList(input.items());
        orderBtgPactualExists.redistributeCreateNewItems(orderItemBtgPactualList);

        orderBtgPactualPort.saveOrder(orderBtgPactualExists);
    }
}
