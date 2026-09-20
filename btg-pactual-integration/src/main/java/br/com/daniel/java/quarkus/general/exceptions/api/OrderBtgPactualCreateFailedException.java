package br.com.daniel.java.quarkus.general.exceptions.api;

import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import io.netty.handler.codec.http.HttpResponseStatus;

public class OrderBtgPactualCreateFailedException extends HttpException {

    public OrderBtgPactualCreateFailedException(String message) {
        super(message);
    }

    public OrderBtgPactualCreateFailedException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.UNPROCESSABLE_ENTITY;
    }
}
