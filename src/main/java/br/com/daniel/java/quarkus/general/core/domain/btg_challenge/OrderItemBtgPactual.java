package br.com.daniel.java.quarkus.general.core.domain.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderItemBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemBtgPactual implements Serializable {

    private String orderItemId;
    private Integer item;
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalItemValue;
    private Boolean active;
    private LocalDateTime createdAt;

    public OrderItemBtgPactual(OrderItemBtgPactualInput input) {
        try {
            BeanUtils.copyProperties(this, input);

            this.orderItemId = UUID.randomUUID().toString();
            this.product = input.product();
            this.quantity = input.quantity();
            this.price = input.price();
            this.active = Boolean.TRUE;

            calculateItemTotalValue();
            defineCreatedAt();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public OrderItemBtgPactual(OrderItemBtgPactualEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public void defineCreatedAt() {
        this.createdAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public void calculateItemTotalValue() {
        this.totalItemValue = isValidParamsCalculateItemValue()
                ? calculateItemValueFinally()
                : BigDecimal.ZERO;
    }

    public BigDecimal calculateItemValue() {
        if (isTotalItemValueNotCalculated()) {
            this.totalItemValue = isValidParamsCalculateItemValue()
                    ? calculateItemValueFinally()
                    : BigDecimal.ZERO;
        }

        return this.totalItemValue;
    }

    private boolean isTotalItemValueNotCalculated() {
        return Objects.isNull(this.totalItemValue) || this.totalItemValue.compareTo(BigDecimal.ZERO) <= 0;
    }

    private boolean isValidParamsCalculateItemValue() {
        return Objects.nonNull(this.price) && Objects.nonNull(this.quantity);
    }

    private BigDecimal calculateItemValueFinally() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
