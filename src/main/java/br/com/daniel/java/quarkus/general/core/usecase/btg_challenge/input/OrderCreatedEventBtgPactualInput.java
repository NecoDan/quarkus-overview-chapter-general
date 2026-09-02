package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input;

import java.util.List;

public record OrderCreatedEventBtgPactualInput(String codigoPedido,
                                               String codigoCliente,
                                               List<OrderItemCreatedEventBtgPactualInput> itens) {
}
