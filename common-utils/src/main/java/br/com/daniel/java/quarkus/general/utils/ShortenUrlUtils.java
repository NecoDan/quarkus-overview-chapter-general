package br.com.daniel.java.quarkus.general.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;

public final class ShortenUrlUtils {

    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    private static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String BASE_CHARACTERS = new BaseCaracteresUtils().getBaseCharacters();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Integer BASE = 62;

    private ShortenUrlUtils() {
        throw new IllegalStateException("This is a utility class ShortenUrlUtils and cannot be instantiated");
    }

    public static String generateShortenUrlLink(final int minLength, final int maxLength) {
        return RandomStringUtils.secureStrong().nextAlphanumeric(minLength, maxLength);
    }

    public static String generateRandomKey(final int minLength, final int maxLength) {
        int length = minLength + SECURE_RANDOM.nextInt(maxLength - minLength + 1);
        var strBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            strBuilder.append(
                    CHAR_POOL.charAt(SECURE_RANDOM.nextInt(CHAR_POOL.length()))
            );
        }

        return strBuilder.toString();
    }

    public static String generateRandomKeyFrom(final int minLength, final int maxLength) {
        int length = minLength + SECURE_RANDOM.nextInt((maxLength - minLength) + 1);
        var strBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(CHAR_POOL.length());
            strBuilder.append(CHAR_POOL.charAt(index));
        }

        return strBuilder.toString();
    }

    public static String toBase62(String input) {
        int inputValue = Integer.parseInt(input);

        if (inputValue == 0)
            return "0";

        var output = StringUtils.EMPTY;
        while (inputValue > 0) {
            int remain = inputValue % BASE;
            output += BASE_CHARACTERS.charAt(remain);
            inputValue /= BASE;
        }

        while (output.length() < 5) {
            output = "0".concat(output);
        }

        return output;
    }

    public static String toBase10(String input) {
        Integer aux = 0;

        for (int i = input.length() - 1, exp = 0; i >= 0; i--, exp++) {
            aux += (int) (Math.pow(62, exp) * BASE_CHARACTERS.indexOf(input.charAt(i)));
        }

        return String.valueOf(aux);
    }
}
