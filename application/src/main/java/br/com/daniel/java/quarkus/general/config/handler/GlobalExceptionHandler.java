package br.com.daniel.java.quarkus.general.config.handler;

import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import jakarta.ws.rs.core.Response;
//import org.jboss.resteasy.reactive.ServerExceptionMapper;

public class GlobalExceptionHandler {

    // Captura a exceção 'RecursoNaoEncontradoException' em toda a aplicação
    //    @ServerExceptionMapper
    public Response handleRecursoNaoEncontrado(TransactionItauNotFoundException exception) {
        ErrorResponse erro = new ErrorResponse(404, exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(erro)
                .build();
    }

    // Exemplo genérico para capturar qualquer outra RuntimeException não tratada
//    @ServerExceptionMapper
    public Response handleGenericException(Exception exception) {
        ErrorResponse erro = new ErrorResponse(500, "Erro interno inesperado no servidor.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(erro)
                .build();
    }

    // DTO auxiliar para o corpo da resposta de erro
    public record ErrorResponse(int status, String message) {
    }
}
