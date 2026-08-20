package br.com.daniel.java.quarkus.general.exceptions.api;

import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import io.netty.handler.codec.http.HttpResponseStatus;

public class TransactionItauCreateFailedException extends HttpException {

    public TransactionItauCreateFailedException(String message) {
        super(message);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.UNPROCESSABLE_ENTITY;
    }
}
