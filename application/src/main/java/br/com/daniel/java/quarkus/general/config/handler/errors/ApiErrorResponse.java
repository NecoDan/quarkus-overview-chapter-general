package br.com.daniel.java.quarkus.general.config.handler.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiErrorResponse(String timestamp,
                               String status,
                               String message,
                               String messageLog,
                               String details,
                               String statusCode,
                               String httpMethodRequest,
                               String path,
                               String uri
) {
}

