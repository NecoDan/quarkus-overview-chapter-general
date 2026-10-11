package br.com.daniel.java.quarkus.general.config.handler.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseDataError(ApiErrorResponse data) {
}

