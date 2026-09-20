package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record OrderItemBtgPactualInput(@NotBlank(message = "produto é obrigatório") @JsonProperty("produto") String product,
                                       @NotNull(message = "quantidade é obrigatória") @Positive(message = "quantidade deve ser maior que zero") @JsonProperty("quantidade") Integer quantity,
                                       @NotNull(message = "preco é obrigatório") @DecimalMin(value = "0.01", inclusive = true, message = "preco deve ser maior que zero") @JsonProperty("preco") BigDecimal price
) {
    public OrderItemBtgPactualInput {
        if (Objects.nonNull(product) && product.isBlank()) {
            throw new IllegalArgumentException("produto é obrigatório");
        }

        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("produto é obrigatório");
        }

        if (Objects.isNull(quantity) || quantity <= 0) {
            throw new IllegalArgumentException("quantidade deve ser maior que zero");
        }

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("preco deve ser maior que zero");
        }
    }
}
