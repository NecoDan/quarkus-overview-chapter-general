package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public record OrderItemBtgPactualOutput(@JsonProperty("numeroItem") Integer item,
                                        @JsonProperty("nomeProduto") String product,
                                        @JsonProperty("qtde") Integer quantity,
                                        @JsonProperty("valorPreco") BigDecimal price) {

    public static List<OrderItemBtgPactualOutput> buildListFrom(final List<OrderItemBtgPactual> items) {
        return items.stream()
                .filter(Objects::nonNull)
                .filter(item -> Objects.nonNull(item.getItem()))
                .map(OrderItemBtgPactualOutput::buildFrom)
                .toList();
    }

    private static OrderItemBtgPactualOutput buildFrom(final OrderItemBtgPactual item) {
        return new OrderItemBtgPactualOutput(
                item.getItem(),
                item.getProduct(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
