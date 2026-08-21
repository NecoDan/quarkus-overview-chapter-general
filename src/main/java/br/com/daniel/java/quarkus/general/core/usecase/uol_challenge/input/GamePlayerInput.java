package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record GamePlayerInput(@NotBlank(message = "O numero documento é obrigatorio") String name,
                              @Email @NotBlank(message = "Email deve ser valido e é obrigatorio") String email,
                              String phone,
                              @NotNull Integer codeHeroGroup
) {
}
