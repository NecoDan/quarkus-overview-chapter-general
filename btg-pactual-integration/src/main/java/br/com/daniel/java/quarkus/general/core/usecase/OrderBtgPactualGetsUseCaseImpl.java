package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.domain.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.port.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalAmountValueBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalQuantityValuesBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.domain.PagedOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualNotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
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
        var pagedAllOrders = orderBtgPactualPort.findPagedAndSortedBy(
                pageIndex, pageSize, expandItems
        );

        return getOrderBtgPactualOutputPagedOutput(pagedAllOrders);
    }

    @Override
    public PagedOutput<OrderBtgPactualOutput> getAllOrdersPageableByCustomer(UUID customerId,
                                                                             int pageIndex,
                                                                             int pageSize,
                                                                             boolean expandItems) {
        if (Objects.isNull(customerId)) {
            throw new IllegalArgumentException("customerId é obrigatório");
        }

        if (pageIndex < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("page e size devem ser valores válidos");
        }

        var pagedAllOrders = orderBtgPactualPort.getAllOrdersPageableByCustomer(customerId, pageIndex,
                pageSize, expandItems
        );

        return getOrderBtgPactualOutputPagedOutput(pagedAllOrders);
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
        if (Objects.isNull(customerId)) {
            throw new IllegalArgumentException("customerId é obrigatório");
        }

        var listOrdersAll = orderBtgPactualPort.getAllOrdersBy(customerId);

        if (CollectionUtils.isEmpty(listOrdersAll)) {
            log.warn("Não foram encontado(s) pedido(s) por meio do ID Cliente {} fornecido.", customerId);
            throw new OrderBtgPactualNotFoundException(
                    "Não foram encontado(s) pedido(s) por meio do ID Cliente %s fornecido.".formatted(customerId)
            );
        }

        var totalQuantity = listOrdersAll.size();
        var totalAmount = listOrdersAll.stream()
                .map(OrderBtgPactual::getTotalValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderTotalQuantityValuesBtgPactualOutput.buildFrom(customerId,
                totalQuantity,
                totalAmount
        );
    }

    private static PagedOutput<OrderBtgPactualOutput> getOrderBtgPactualOutputPagedOutput(PagedOutput<OrderBtgPactual> pagedAndSortedBy) {
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
}
