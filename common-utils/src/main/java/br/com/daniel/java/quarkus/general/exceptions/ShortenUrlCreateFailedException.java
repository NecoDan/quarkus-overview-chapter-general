package br.com.daniel.java.quarkus.general.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ShortenUrlCreateFailedException extends HttpException {

    public ShortenUrlCreateFailedException(String message) {
        super(message);
    }

    public ShortenUrlCreateFailedException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.UNPROCESSABLE_ENTITY;
    }
}
