package br.com.daniel.java.quarkus.general.config.handler;

import br.com.daniel.java.quarkus.general.config.handler.errors.ApiErrorResponse;
import br.com.daniel.java.quarkus.general.config.handler.errors.ResponseDataError;
import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import br.com.daniel.java.quarkus.general.utils.FunctionalUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Provider
public class HandlerAdviceController implements ExceptionMapper<Exception> {

    @Context
    HttpServerRequest request;

    @Inject
    Logger logger;

    @ConfigProperty(name = "apiquarkusgeneral.application.log-complete", defaultValue = "NULO") boolean showLogComplete;

    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }

    private Response mapExceptionToResponse(Exception exception) {
        return switch (exception) {
            case WebApplicationException webAppEx -> {
                var originalErrorResponse = webAppEx.getResponse();
                yield Response.fromResponse(originalErrorResponse)
                        .entity(originalErrorResponse.getStatusInfo().getReasonPhrase())
                        .build();
            }
            case IllegalArgumentException e -> Response.status(HttpResponseStatus.NOT_FOUND.code())
                    .entity(e.getMessage())
                    .build();
            case HttpException httpEx -> toResponseFromCustomizeApplication(httpEx);
            default -> toResponseInternalServerError(exception);
        };
    }

    public Response toResponseInternalServerError(final Exception throwable) {
        exposeLogErrorFrom(throwable);
        logger.fatalf(throwable, "Failed to process request to: {}", getUriComplete());
        log.error("Error: {} | Failed to process request to: {}", throwable, getUriComplete());

        final var httpStatus = getHttpStatusOrDefault(null);
        final var response = buildApiErrorResponse(throwable.getMessage(),
                throwable.getLocalizedMessage(),
                httpStatus
        );

        exposeLogErrorFromWith(response.messageLog(), httpStatus);
        return buildResponseFrom(httpStatus, response);
    }

    public Response toResponseFromCustomizeApplication(final HttpException throwable) {
        exposeLogErrorFrom(throwable);
        final var httpStatus = getHttpStatusOrDefault(throwable.getHttpStatus());

        final var response = buildApiErrorResponse(throwable.getMessage(),
                throwable.getLocalizedMessage(),
                throwable.getHttpStatus()
        );

        exposeLogErrorFromWith(response.messageLog(), httpStatus);
        return buildResponseFrom(httpStatus, response);
    }

    private Response buildResponseFrom(final HttpResponseStatus httpStatus,
                                       final ApiErrorResponse response) {
        return Response.status(httpStatus.code())
                .entity(new ResponseDataError(response))
                .build();
    }

    private HttpResponseStatus getHttpStatusOrDefault(final HttpResponseStatus httpStatus) {
        return (Objects.isNull(httpStatus)) ? HttpResponseStatus.INTERNAL_SERVER_ERROR : httpStatus;
    }

    private void exposeLogErrorFromWith(final String logMessage, final HttpResponseStatus httpStatus) {
        log.error("Erro na execucao do(s) recurso(s): {} | Http Status: {}", logMessage, httpStatus);
    }

    private ApiErrorResponse buildApiErrorResponse(final String message,
                                                   final String details,
                                                   final HttpResponseStatus httpStatus) {
        return ApiErrorResponse.builder()
                .message(message)
                .details(details.toUpperCase())
                .messageLog(StringUtils.EMPTY)
                .timestamp(FunctionalUtils.formatCreationDate(LocalDateTime.now()))
                .statusCode(isValidHttpStatus(httpStatus) ? String.valueOf(httpStatus.code()) : StringUtils.EMPTY)
                .status(getToStringHttpStatus(httpStatus))
                .httpMethodRequest(getHttMethodRequest())
                .path(getPathUri())
                .uri(getUriComplete())
                .build();
    }

    private void exposeLogErrorFrom(final Throwable throwable) {
        try {
            log.error(throwable.getMessage());
            final var first = Arrays.stream(throwable.getStackTrace()).findFirst();
            final var stackTraceElement = first.orElse(buildStackTraceElement());

            if (showLogComplete) {
                log.error("Error na execucao do recurso: {}", throwable.getMessage());
                log.error("Error ", throwable);
            } else {
                log.error("Error na execucao do recurso: {}", throwable.getMessage());
                log.error("Error de execucao | class: {} | line: {} | method_name: {}| file_name_class: {}.", stackTraceElement.getClassName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName(), stackTraceElement.getFileName());
            }
        } catch (Throwable e) {
            log.error("Falha ao obter detalhes do erro para log ", throwable);
        }
    }

    private StackTraceElement buildStackTraceElement() {
        return new StackTraceElement(StringUtils.EMPTY,
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                0
        );
    }

    private String getPathUri() {
        return request.path();
    }

    private String getUriComplete() {
        return request.absoluteURI();
    }

    private String getHttMethodRequest() {
        return request.method().name();
    }

    private String getToStringHttpStatus(final HttpResponseStatus httpStatus) {
        return isValidHttpStatus(httpStatus) ? httpStatus.code() + " " + httpStatus.reasonPhrase() : StringUtils.EMPTY;
    }

    private boolean isValidHttpStatus(final HttpResponseStatus httpStatus) {
        return Objects.nonNull(httpStatus);
    }
}
