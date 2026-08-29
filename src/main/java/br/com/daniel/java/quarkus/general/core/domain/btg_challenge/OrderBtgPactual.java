package br.com.daniel.java.quarkus.general.core.domain.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBtgPactual implements Serializable {

    private String orderId;
    private String customerId;
    private BigDecimal totalValue;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private List<OrderItemBtgPactual> items;

    public OrderBtgPactual(OrderBtgPactualInput input) {
        try {
            BeanUtils.copyProperties(this, input);
            this.customerId = input.customerId().toString();

            createItems(input.items());
            calculateTotalValue();
            defineDates();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public OrderBtgPactual(OrderBtgPactualEntity orderEntity) {
        try {
            BeanUtils.copyProperties(this, orderEntity);

            if (Objects.isNull(this.orderId)) {
                this.orderId = orderEntity.getOrderId();
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public void defineDates() {
        this.createdAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
        this.updateAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    private void createItems(List<OrderItemBtgPactualInput> items) {
        var atomicIntegerValue = new AtomicInteger(1);

        items.forEach(itemInput -> {
                    var itemNewCreated = new OrderItemBtgPactual(itemInput);
                    itemNewCreated.setItem(atomicIntegerValue.getAndIncrement());
                    addOrderItem(itemNewCreated);
                }
        );
    }

    public void addOrderItem(OrderItemBtgPactual item) {
        if (isOrderItemsInvalid())
            this.items = new ArrayList<>();
        this.items.add(item);
    }

    public void addAllOrderItem(Collection<OrderItemBtgPactual> items) {
        if (isOrderItemsInvalid())
            this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    public void calculateTotalValue() {
        if (isOrderItemsInvalid()) {
            this.totalValue = BigDecimal.ZERO;
            return;
        }

        this.totalValue = this.items
                .stream()
                .filter(Objects::nonNull)
                .map(OrderItemBtgPactual::calculateItemValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isOrderItemsInvalid() {
        return CollectionUtils.isEmpty(this.items);
    }
}
