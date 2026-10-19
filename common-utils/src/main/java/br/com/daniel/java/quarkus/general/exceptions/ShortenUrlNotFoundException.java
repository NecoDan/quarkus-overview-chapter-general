package br.com.daniel.java.quarkus.general.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ShortenUrlNotFoundException extends HttpException {
    public ShortenUrlNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.NOT_FOUND;
    }
}
