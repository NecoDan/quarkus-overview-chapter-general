package br.com.daniel.java.quarkus.general.exceptions;

import com.google.common.base.Throwables;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.Serial;
import java.util.Objects;

public abstract class HttpException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4761135120608120535L;

    protected String rootCauseMessage;

    protected HttpException(String message) {
        super(message);
    }

    protected HttpException(String message, Throwable cause) {
        super(message, cause);
        final var rootCause = cause != null ? Throwables.getRootCause(cause) : null;
        if (Objects.nonNull(rootCause)) this.rootCauseMessage = rootCause.getMessage();
    }

    protected HttpException(){}

    public abstract HttpResponseStatus getHttpStatus();
}
