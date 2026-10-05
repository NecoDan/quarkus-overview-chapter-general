package br.com.daniel.java.quarkus.general.exceptions;

import io.netty.handler.codec.http.HttpResponseStatus;

public class GamePlayerUolCreateFailedException extends HttpException {

    public GamePlayerUolCreateFailedException(String message) {
        super(message);
    }

    public GamePlayerUolCreateFailedException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public HttpResponseStatus getHttpStatus() {
        return HttpResponseStatus.UNPROCESSABLE_ENTITY;
    }
}
