package br.com.daniel.java.quarkus.general.core.usecase.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record TransactionItauInput(@NotNull BigDecimal amount,
                                   @NotNull OffsetDateTime createdAt,
                                   @NotBlank(message = "O numero documento é obrigatorio") String documentNumber,
                                   @NotBlank(message = "O valor referente ao token do cartao de credito é obrigatorio") String creditCardToken
) {
}
