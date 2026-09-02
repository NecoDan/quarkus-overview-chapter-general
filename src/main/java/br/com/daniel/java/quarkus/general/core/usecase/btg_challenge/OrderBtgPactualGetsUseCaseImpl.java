package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderTotalAmountValueBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderTotalQuantityValuesBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.generics.PagedOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualNotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Singleton
@Slf4j
public class OrderBtgPactualGetsUseCaseImpl implements OrderBtgPactualGetsUseCase {

    @Inject
    OrderBtgPactualPort orderBtgPactualPort;

    @Override
    public OrderBtgPactualOutput getById(ObjectId id) {
        return OrderBtgPactualOutput.buildFrom(
                orderBtgPactualPort.getOrderById(id)
                        .orElseThrow(() ->
                                new OrderBtgPactualNotFoundException(
                                        "Nenhum Pedido localizado por meio do ID: %s.".formatted(id)
                                )
                        )
        );
    }

    @Override
    public PagedOutput<OrderBtgPactualOutput> getAllPageable(int pageIndex,
                                                             int pageSize,
                                                             boolean expandItems) {
        var pagedAndSortedBy = orderBtgPactualPort.findPagedAndSortedBy(
                pageIndex, pageSize, expandItems
        );

        var finalList = pagedAndSortedBy.getContent()
                .stream()
                .map(OrderBtgPactualOutput::buildFrom)
                .toList();

        return new PagedOutput<>(
                finalList,
                pagedAndSortedBy.getPageIndex(),
                pagedAndSortedBy.getPageSize(),
                pagedAndSortedBy.getTotalElements(),
                pagedAndSortedBy.getTotalPages(),
                pagedAndSortedBy.isHasNext(),
                pagedAndSortedBy.isHasPrevious()
        );
    }

    @Override
    public List<OrderBtgPactualOutput> getAllOrdersBy(UUID customerId) {
        var listOrdersAll = orderBtgPactualPort.getAllOrdersBy(customerId);

        if (CollectionUtils.isEmpty(listOrdersAll)) {
            log.warn("Não foram encontado(s) pedido(s) por meio do ID Client {} fornecido.", customerId);
            throw new OrderBtgPactualNotFoundException(
                    "Não foram encontado(s) pedido(s) por meio do ID Client %s fornecido.".formatted(customerId)
            );
        }

        return listOrdersAll.stream()
                .map(OrderBtgPactualOutput::buildFrom)
                .toList();
    }

    @Override
    public OrderTotalAmountValueBtgPactualOutput getTotalAmountBy(ObjectId id) {
        final var orderBtgPactual = orderBtgPactualPort.getOrderById(id)
                .orElseThrow(() ->
                        new OrderBtgPactualNotFoundException(
                                "Nenhum Pedido localizado por meio do ID: %s.".formatted(id)
                        )
                );

        return OrderTotalAmountValueBtgPactualOutput.buildFrom(orderBtgPactual.getToStringId(),
                orderBtgPactual.getOrderId(),
                orderBtgPactual.getTotalValue()
        );
    }

    @Override
    public OrderTotalQuantityValuesBtgPactualOutput getTotalQuantityOrdersBy(UUID customerId) {
        var listOrdersAll = getAllOrdersBy(customerId);

        var totalQuantity = listOrdersAll.size();
        var totalAmount = listOrdersAll.stream()
                .map(OrderBtgPactualOutput::totalValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderTotalQuantityValuesBtgPactualOutput.buildFrom(customerId,
                totalQuantity,
                totalAmount
        );
    }
}
