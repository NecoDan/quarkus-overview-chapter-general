package br.com.daniel.java.quarkus.general.exceptions.api;

import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import io.netty.handler.codec.http.HttpResponseStatus;

public class TransactionItauNotFoundException extends HttpException {

    public TransactionItauNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.NOT_FOUND;
    }
}
