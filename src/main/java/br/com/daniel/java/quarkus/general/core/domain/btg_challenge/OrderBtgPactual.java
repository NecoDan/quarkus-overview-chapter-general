package br.com.daniel.java.quarkus.general.core.domain.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBtgPactual implements Serializable {

    private ObjectId id;
    private String orderId;
    private OrderCustomerBtgPactual customer;
    private BigDecimal totalValue;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private List<OrderItemBtgPactual> items;

    public OrderBtgPactual(OrderBtgPactualInput input) {
        try {
            BeanUtils.copyProperties(this, input);
            this.orderId = input.orderId().toString();

            defineCustomer(input.customerId());
            createItems(input.items());
            calculateTotalValue();
            defineDates();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    private void defineCustomer(UUID customerId) {
        this.customer = OrderCustomerBtgPactual.builder()
                .customerId(customerId.toString())
                .build()
                .defineCreatedAt();
    }

    public String getToStringId() {
        return this.id.toHexString();
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
        });
    }

    public void addOrderItem(OrderItemBtgPactual item) {
        if (isOrderItemsInvalid()) this.items = new ArrayList<>();
        this.items.add(item);
    }

    public void addAllOrderItem(Collection<OrderItemBtgPactual> items) {
        if (isOrderItemsInvalid()) this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    public void calculateTotalValue() {
        if (isOrderItemsInvalid()) {
            this.totalValue = BigDecimal.ZERO;
            return;
        }

        this.totalValue = this.items.stream()
                .filter(Objects::nonNull)
                .map(OrderItemBtgPactual::calculateItemValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isOrderItemsInvalid() {
        return CollectionUtils.isEmpty(this.items);
    }
}
