package br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ShortenUrlOutput(@JsonProperty("id") Long id,
                               @JsonProperty("url_link_origem") String originalUrlLink,
                               @JsonProperty("url_link_encurtado") String shortenUrlLink,
                               @JsonProperty("data_criacao") LocalDateTime createdAt,
                               @JsonProperty("data_atualizacao") LocalDateTime updatedAt,
                               @JsonProperty("status_ativo") Boolean active) {
}
