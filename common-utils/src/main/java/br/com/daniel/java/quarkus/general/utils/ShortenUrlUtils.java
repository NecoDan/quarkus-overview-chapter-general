package br.com.daniel.java.quarkus.general.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;

/**
 * Classe utilitária para gerar URLs encurtadas e chaves aleatórias.
 * <p>
 * Esta classe fornece métodos para gerar strings aleatórias, converter números
 * entre a base 10 e a base 62, e garantir uma aleatoriedade segura.
 * <p>
 * Esta classe não pode ser instanciada.
 */
public final class ShortenUrlUtils {

    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    private static final String BASE_CHARACTERS = new BaseCaracteresUtils().getBaseCharacters();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Integer BASE = 62;

    private ShortenUrlUtils() {
        throw new IllegalStateException("This is a utility class ShortenUrlUtils and cannot be instantiated");
    }

    /**
     * Gera um link de URL encurtada com uma string alfanumérica aleatória.
     *
     * @param minLength o comprimento mínimo da string gerada
     * @param maxLength o comprimento máximo da string gerada
     * @return uma string alfanumérica aleatória com comprimento entre minLength e maxLength
     */
    public static String generateShortenUrlLink(final int minLength, final int maxLength) {
        return RandomStringUtils.secureStrong().nextAlphanumeric(minLength, maxLength);
    }

    /**
     * Gera uma chave aleatória usando os caracteres base.
     *
     * @param minLength o comprimento mínimo da chave gerada
     * @param maxLength o comprimento máximo da chave gerada
     * @return uma chave aleatória com comprimento entre minLength e maxLength
     */
    public static String generateRandomKey(final int minLength, final int maxLength) {
        int length = minLength + SECURE_RANDOM.nextInt(maxLength - minLength + 1);
        var strBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            strBuilder.append(
                    BASE_CHARACTERS.charAt(SECURE_RANDOM.nextInt(BASE_CHARACTERS.length()))
            );
        }

        return strBuilder.toString();
    }

    /**
     * Gera uma chave aleatória usando os caracteres base (implementação alternativa).
     *
     * @param minLength o comprimento mínimo da chave gerada
     * @param maxLength o comprimento máximo da chave gerada
     * @return uma chave aleatória com comprimento entre minLength e maxLength
     */
    public static String generateRandomKeyFrom(final int minLength, final int maxLength) {
        int length = minLength + SECURE_RANDOM.nextInt((maxLength - minLength) + 1);
        var strBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(BASE_CHARACTERS.length());
            strBuilder.append(BASE_CHARACTERS.charAt(index));
        }

        return strBuilder.toString();
    }

    /**
     * Converte um número na base 10 (como string) para uma string codificada em base 62.
     *
     * @param input o número na base 10 como string
     * @return a string codificada em base 62
     */
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

    /**
     * Converte uma string codificada em base 62 para um número na base 10 (como string).
     *
     * @param input a string codificada em base 62
     * @return o número na base 10 como string
     */
    public static String toBase10(String input) {
        int aux = 0;

        for (int i = input.length() - 1, exp = 0; i >= 0; i--, exp++) {
            aux += (int) (Math.pow(62, exp) * BASE_CHARACTERS.indexOf(input.charAt(i)));
        }

        return String.valueOf(aux);
    }
}