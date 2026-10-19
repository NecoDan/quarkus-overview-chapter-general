package br.com.daniel.java.quarkus.general.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public final class ShortenUrlUtils {

    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    private static String BASE_CHARACTERS = StringUtils.EMPTY;
    public static final Integer BASE = 62;

    private ShortenUrlUtils() {
        throw new IllegalStateException("This is a utility class ShortenUrlUtils and cannot be instantiated");
    }

    public static String toBase62(String input) {
        initialize();
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

    public static String generateShortenUrlLink(final int minLength, final int maxLength) {
        return RandomStringUtils.secureStrong().nextAlphanumeric(minLength, maxLength);
    }

    private static void initialize() {
        BASE_CHARACTERS = StringUtils.EMPTY;
        for (char i = '0'; i <= '9'; i++) BASE_CHARACTERS += i;
        for (char i = 'a'; i <= 'z'; i++) BASE_CHARACTERS += i;
        for (char i = 'A'; i <= 'Z'; i++) BASE_CHARACTERS += i;
    }
}
