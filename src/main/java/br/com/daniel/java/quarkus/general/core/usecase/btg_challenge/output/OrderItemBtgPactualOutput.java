package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record OrderItemBtgPactualOutput(String orderItemId,
                                        Integer item,
                                        String product,
                                        Integer quantity,
                                        BigDecimal price,
                                        boolean active,
                                        LocalDateTime createdAt) {
}
