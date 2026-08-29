package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public record OrderBtgPactualOutput(String orderId,
                                    String customerId,
                                    BigDecimal totalValue,
                                    LocalDateTime createdAt,
                                    LocalDateTime updateAt,
                                    List<OrderItemBtgPactualOutput> items
) {
    public static OrderBtgPactualOutput empty() {
        return new OrderBtgPactualOutput(
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                BigDecimal.ZERO,
                null,
                null,
                Collections.emptyList()
        );
    }

    public static OrderBtgPactualOutput buildFrom(OrderBtgPactual orderBtgPactual) {
        try {
            var order = OrderBtgPactualOutput.empty();
            BeanUtils.copyProperties(order, orderBtgPactual);
            return order;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
   