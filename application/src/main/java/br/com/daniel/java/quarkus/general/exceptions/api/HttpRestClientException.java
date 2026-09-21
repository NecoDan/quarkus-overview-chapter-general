package br.com.daniel.java.quarkus.general.exceptions.api;

import br.com.daniel.java.quarkus.general.exceptions.HttpException;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpRestClientException extends HttpException {

    public HttpRestClientException(String message) {
        super(message);
    }

    public HttpRestClientException(String message, Exception e) {
        super(message, e);
    }

    public HttpRestClientException(Exception e) {
        super(e);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR;
    }
}
