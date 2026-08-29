package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderBtgPactualOutput;

public interface OrderBtgPactualGetsUseCase {
    OrderBtgPactualOutput getById(String id);
}
