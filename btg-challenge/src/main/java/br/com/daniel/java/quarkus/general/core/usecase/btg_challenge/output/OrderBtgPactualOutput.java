package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.utils.FunctionalUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderBtgPactualOutput(@JsonProperty("idPedido") String id,
                                    @JsonProperty("idPedidoExterno") String orderId,
                                    @JsonProperty("cliente") OrderCustomerBtgPactualOutput customer,
                                    @JsonProperty("valorTotal") BigDecimal totalValue,
                                    @JsonProperty("dataCriacao") String createdAt,
                                    @JsonProperty("dataAtualizacao") String updateAt,
                                    @JsonProperty("itens") List<OrderItemBtgPactualOutput> items
) {
    public static OrderBtgPactualOutput buildFrom(final OrderBtgPactual orderBtgPactual) {
        final var itens = CollectionUtils.isEmpty(orderBtgPactual.getItems())
                ? null
                : OrderItemBtgPactualOutput.buildListFrom(orderBtgPactual.getItems());

        return new OrderBtgPactualOutput(
                orderBtgPactual.getToStringId(),
                orderBtgPactual.getOrderId(),
                OrderCustomerBtgPactualOutput.createFrom(orderBtgPactual.getCustomer()),
                orderBtgPactual.getTotalValue(),
                FunctionalUtils.formatCreationDateBy(orderBtgPactual.getCreatedAt()),
                FunctionalUtils.formatCreationDateBy(orderBtgPactual.getUpdateAt()),
                itens
        );
    }
}
   