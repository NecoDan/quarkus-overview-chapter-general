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
import java.util.stream.Collectors;

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
        this.customer = OrderCustomerBtgPactual.builder().customerId(customerId.toString()).build().assignCreatedAt();
    }

    public String getToStringId() {
        return this.id.toHexString();
    }

    public void defineDates() {
        this.createdAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
        assignDateUpdateAt();
    }

    public void assignDateUpdateAt() {
        this.updateAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public void assignCustomerCode(String customerId) {
        if (Objects.isNull(this.customer)) {
            this.customer = OrderCustomerBtgPactual.builder().customerId(customerId).build().assignCreatedAt();
            return;
        }

        this.customer.setCustomerId(customerId);
        this.customer.defineCreatedAt();
    }

    public void createItems(List<OrderItemBtgPactualInput> itemsInput) {
        var atomicIntegerValue = new AtomicInteger(1);

        itemsInput.forEach(itemInput -> {
            var itemNewCreated = new OrderItemBtgPactual(itemInput);
            itemNewCreated.setItem(atomicIntegerValue.getAndIncrement());
            addOrderItem(itemNewCreated);
        });
    }

    public void redistributeCreateNewItems(List<OrderItemBtgPactualInput> itemsInput) {
        if (CollectionUtils.isEmpty(itemsInput))
            throw new IllegalArgumentException("Lista de itens do pedido não pode ser nula ou vazia.");

        if (isOrderItemsInvalid()) this.items = new ArrayList<>();

        Set<String> setProductsItemsList = this.items
                .stream()
                .map(OrderItemBtgPactual::getProduct)
                .collect(Collectors.toSet());

        createdNewItemFrom(setProductsItemsList, itemsInput);
        updateItemsExisting(setProductsItemsList, itemsInput);
        calculateTotalValue();
    }

    private void updateItemsExisting(Set<String> setProductsItemsList,
                                     List<OrderItemBtgPactualInput> itemsInput) {

        var itemsInputNewUpdated = itemsInput.stream()
                .filter(itemNewInput -> setProductsItemsList.contains(itemNewInput.product()))
                .toList();

        itemsInputNewUpdated.forEach(itemInput ->
                this.items.forEach(itemExists -> {
                            if (isConditionUpdatedItem(itemInput, itemExists)) {
                                itemExists.setProduct(itemInput.product());
                                itemExists.setQuantity(itemInput.quantity());
                                itemExists.setPrice(itemInput.price());
                                itemExists.calculateItemTotalValue();
                            }
                        }
                )
        );
    }

    private void createdNewItemFrom(Set<String> setProductsItemsList,
                                    List<OrderItemBtgPactualInput> itemsInput) {

        var itemsInputNewCreated = itemsInput.stream()
                .filter(itemNewInput -> !setProductsItemsList.contains(itemNewInput.product()))
                .toList();

        itemsInputNewCreated.forEach(this::redistributeCreateNewItemFrom);
    }

    private boolean isConditionUpdatedItem(OrderItemBtgPactualInput itemInput,
                                           OrderItemBtgPactual itemExists) {
        return Objects.nonNull(itemInput) && Objects.nonNull(itemExists)
                && Objects.equals(itemInput.product(), itemExists.getProduct())
                && (!Objects.equals(itemInput.quantity(), itemExists.getQuantity())
                || !Objects.equals(itemInput.price(), itemExists.getPrice()));

    }

    private void redistributeCreateNewItemFrom(OrderItemBtgPactualInput itemInput) {
        var itemNewCreated = new OrderItemBtgPactual(itemInput);

        var lastItemValue = this.items.getLast().getItem();
        var atomicIntegerValue = new AtomicInteger(Objects.isNull(lastItemValue) ? 1 : lastItemValue);

        itemNewCreated.setItem(atomicIntegerValue.getAndIncrement());
        addOrderItem(itemNewCreated);
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

        this.totalValue = this.items
                .stream()
                .filter(Objects::nonNull)
                .map(OrderItemBtgPactual::calculateItemValue).
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isOrderItemsInvalid() {
        return CollectionUtils.isEmpty(this.items);
    }
}
