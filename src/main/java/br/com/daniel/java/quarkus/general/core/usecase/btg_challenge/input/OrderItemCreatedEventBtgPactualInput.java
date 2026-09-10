package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderItemCreatedEventBtgPactualInput(@NotNull @JsonProperty("produto") String product,
                                                   @NotNull @JsonProperty("quantidade") Integer quantity,
                                                   @NotNull @JsonProperty("preco") BigDecimal price
) {
}
