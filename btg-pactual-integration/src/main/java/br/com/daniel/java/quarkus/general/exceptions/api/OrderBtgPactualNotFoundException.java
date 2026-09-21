package br.com.daniel.java.quarkus.general.exceptions.api;

import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import io.netty.handler.codec.http.HttpResponseStatus;

public class OrderBtgPactualNotFoundException extends HttpException {

    public OrderBtgPactualNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.NOT_FOUND;
    }
}
