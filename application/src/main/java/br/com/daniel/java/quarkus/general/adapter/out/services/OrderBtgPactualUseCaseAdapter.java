package br.com.daniel.java.quarkus.general.adapter.out.services;

import br.com.daniel.java.quarkus.general.core.domain.PagedOutput;
import br.com.daniel.java.quarkus.general.core.usecase.OrderBtgPactualCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.OrderBtgPactualGetsUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderCreatedBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalAmountValueBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalQuantityValuesBtgPactualOutput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.UUID;

@ApplicationScoped
@Slf4j
public class OrderBtgPactualUseCaseAdapter {

    @Inject
    OrderBtgPactualCreateUseCase orderBtgPactualCreateUseCase;

    @Inject
    OrderBtgPactualGetsUseCase orderBtgPactualGetsUseCase;

    public OrderCreatedBtgPactualOutput createOrder(OrderBtgPactualInput input) {
        log.info("BTG_PACTUAL_CHALLENGE - Criando um novo pedido com os dados fornecidos no corpo da requisição.");
        return orderBtgPactualCreateUseCase.createOrder(input);
    }

    public OrderBtgPactualOutput getById(ObjectId id) {
        log.info("BTG_PACTUAL_CHALLENGE - Obtendo pedido pelo ID: {}", id);
        return orderBtgPactualGetsUseCase.getById(id);
    }

    public PagedOutput<OrderBtgPactualOutput> getAllPageable(int pageIndex, int pageSize, boolean expandItems) {
        log.info("BTG_PACTUAL_CHALLENGE - Obtendo todos os pedidos de forma paginada. " +
                "Página: {}, Tamanho da página: {}, Expandir itens: {}", pageIndex, pageSize, expandItems
        );
        return orderBtgPactualGetsUseCase.getAllPageable(pageIndex, pageSize, expandItems);
    }

    public PagedOutput<OrderBtgPactualOutput> getAllOrdersPageableByCustomer(UUID customerId, int pageIndex, int pageSize, boolean expandItems) {
        log.info("BTG_PACTUAL_CHALLENGE - Obtendo todos os pedidos do cliente {} de forma paginada. " +
                "Página: {}, Tamanho da página: {}, Expandir itens: {}", customerId, pageIndex, pageSize, expandItems
        );
        return orderBtgPactualGetsUseCase.getAllOrdersPageableByCustomer(customerId, pageIndex, pageSize, expandItems);
    }

    public OrderTotalAmountValueBtgPactualOutput getTotalAmountBy(ObjectId id) {
        log.info("BTG_PACTUAL_CHALLENGE - Obtendo o valor total do pedido pelo ID: {}", id);
        return orderBtgPactualGetsUseCase.getTotalAmountBy(id);
    }

    public OrderTotalQuantityValuesBtgPactualOutput getTotalQuantityOrdersBy(UUID customerId) {
        log.info("BTG_PACTUAL_CHALLENGE - Obtendo a quantidade total de pedidos do cliente: {}", customerId);
        return orderBtgPactualGetsUseCase.getTotalQuantityOrdersBy(customerId);
    }
}
