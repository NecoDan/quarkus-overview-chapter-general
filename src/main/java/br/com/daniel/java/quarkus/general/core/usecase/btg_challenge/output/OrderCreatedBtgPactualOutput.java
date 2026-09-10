package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderCreatedBtgPactualOutput(@JsonProperty("respostaSucesso") String respostaSucesso
) {
    public static OrderCreatedBtgPactualOutput from(String id) {
        return new OrderCreatedBtgPactualOutput("Pedido %s criado e salvo com sucesso.".formatted(id)
        );
    }
}
