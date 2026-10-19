package br.com.daniel.java.quarkus.general.utils;

import lombok.Getter;

@Getter
public class BaseCaracteresUtils {

    private static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final String baseCharacters;

    public BaseCaracteresUtils() {
        baseCharacters = CHAR_POOL;
    }
}
