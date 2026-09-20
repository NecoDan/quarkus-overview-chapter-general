package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.domain.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.mappers.OrderBtgPactualMapper;
import br.com.daniel.java.quarkus.general.core.port.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderCreatedBtgPactualOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class OrderBtgPactualCreateUseCaseImpl implements OrderBtgPactualCreateUseCase {

    @Inject OrderBtgPactualPort orderBtgPactualPort;

    @Inject OrderBtgPactualMapper orderBtgPactualMapper;

    @Override
    public OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input) {
        if (input == null) {
            throw new IllegalArgumentException("payload do pedido não pode ser nulo");
        }

        log.info("Inicializando fluxo para criação de Pedido. Payload: {}", input);

        try {
            var orderBtgPactual = new OrderBtgPactual(input);
            var orderBtgPactualSaved = orderBtgPactualPort.saveOrder(orderBtgPactual);

            return OrderCreatedBtgPactualOutput.from(orderBtgPactualSaved.get().getToStringId());
        } catch (IllegalArgumentException e) {
            log.error("Payload inválido ao criar um novo Pedido. Payload: {}. Erro: {}", input, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro ao criar um novo Pedido. Payload: {}. Erro: {}", input, e.getMessage());
            throw new OrderBtgPactualCreateFailedException("Erro ao criar um novo Pedido. Payload: %s. Erro: %s".formatted(input, e.getMessage()), e);
        }
    }

    @Override
    public void createOrderFrom(OrderCreatedEventBtgPactualInput input) {
        if (input == null) {
            throw new IllegalArgumentException("payload do pedido não pode ser nulo");
        }

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
        } catch (IllegalArgumentException e) {
            log.error("Payload inválido ao criar Pedido a partir do evento. Payload: {}. Erro: {}", input, e.getMessage());
            throw e;
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
