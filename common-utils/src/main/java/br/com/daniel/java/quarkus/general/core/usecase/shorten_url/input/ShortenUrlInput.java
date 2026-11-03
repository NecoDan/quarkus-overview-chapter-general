package br.com.daniel.java.quarkus.general.core.usecase.shorten_url.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ShortenUrlInput(@NotBlank(message = "A url com o link é obrigatório") String linkUrl) {
}
