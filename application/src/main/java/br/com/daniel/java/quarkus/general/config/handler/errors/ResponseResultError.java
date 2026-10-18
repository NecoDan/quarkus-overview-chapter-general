package br.com.daniel.java.quarkus.general.config.handler.errors;

public record ResponseResultError(String timestamp,
                                  String status,
                                  String message,
                                  String details,
                                  String statusCode,
                                  String path
) {
}


