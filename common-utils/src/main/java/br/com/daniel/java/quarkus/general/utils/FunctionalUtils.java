package br.com.daniel.java.quarkus.general.utils;

import io.smallrye.config.SmallRyeConfig;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Classe utilitária que disponibiliza métodos auxiliares para conversão,
 * validação, formatação e manipulação de dados.
 *
 * <p>Os métodos desta classe são estáticos e abrangem operações relacionadas
 * a datas e horários, valores numéricos, strings, valores decimais e
 * formatação de CPF, além da consulta aos perfis ativos da aplicação.</p>
 *
 * <p>Não é permitida a criação de instâncias desta classe.</p>
 *
 * @author Daniel Santos
 */
public final class FunctionalUtils {

    private FunctionalUtils() {
        throw new IllegalStateException("This is a utility class FunctionalUtils and cannot be instantiated");
    }

    public static final String BR_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private static final Locale PT_BR = new Locale.Builder().setLanguage("pt").setRegion("BR").build();


    public static ObjectId uuidToObjectIdMongoDb(UUID uuid) {
        // 1. Convert UUID to string without hyphens (32 hex characters)
        String hexString = uuid.toString().replace("-", "");

        // 2. Truncate to 24 characters (required length for MongoDB ObjectId)
        String validObjectIdHex = hexString.substring(0, 24);

        // 3. Create the BSON ObjectId
        return new ObjectId(validObjectIdHex);
    }

    /**
     * Recupera os perfis (profiles) de configuração ativos na aplicação.
     *
     * @return Uma {@link List} contendo os nomes dos perfis ativos.
     */
    public static List<String> getActiveProfiles() {
        return ConfigProvider.getConfig().unwrap(SmallRyeConfig.class).getProfiles();
    }

    /**
     * Formata um objeto {@link LocalDateTime} para String utilizando o padrão brasileiro (dd/MM/yyyy HH:mm:ss).
     * Retorna uma String vazia caso a data informada seja nula.
     *
     * @param localDateTime Objeto contendo a data e hora a ser formatada.
     * @return A data formatada como {@link String} no padrão brasileiro ou {@link StringUtils#EMPTY} caso o parâmetro seja nulo.
     */
    public static String formatCreationDate(LocalDateTime localDateTime) {
        return (Objects.isNull(localDateTime)) ? StringUtils.EMPTY : formatCreationDateBy(localDateTime);
    }

    /**
     * Formata um objeto {@link LocalDateTime} utilizando diretamente o padrão brasileiro (dd/MM/yyyy HH:mm:ss).
     *
     * @param localDateTime Objeto contendo a data e hora a ser formatada.
     * @return A data formatada como {@link String}.
     */
    public static String formatCreationDateBy(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(BR_DATETIME_FORMAT));
    }

    /**
     * Converte uma {@link String} representando data e hora no formato inglês ("yyyy-MM-dd HH:mm:ss") para {@link LocalDateTime}.
     * Caso o texto possua mais de 19 caracteres, ele é truncado para considerar apenas a parte de data e hora.
     *
     * @param value Texto contendo a data no formato "yyyy-MM-dd HH:mm:ss".
     * @return O objeto {@link LocalDateTime} correspondente à data convertida.
     * @throws IllegalStateException Se a string for nula, vazia ou composta apenas por espaços.
     */
    public static LocalDateTime onlyLocalDateTimeDefaultEnglish(String value) {
        if (!isStringValida(value))
            throw new IllegalStateException("Conteudo do valor p/ conversao de data/hora inválida e/ou inexistente.");

        if (value.length() > 19) {
            value = value.substring(0, 19);
        }

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Converte uma {@link String} representando data e hora em um objeto {@link LocalDateTime} de acordo com o padrão informado.
     *
     * @param value  Texto contendo a data a ser convertida.
     * @param format Padrão de data esperado (ex: "yyyy-MM-dd", "dd/MM/yyyy HH:mm").
     * @return O objeto {@link LocalDateTime} correspondente à data convertida.
     * @throws IllegalStateException Se o valor ou o formato informados forem nulos ou inválidos.
     */
    public static LocalDateTime onlyLocalDateTimeBy(final String value, final String format) {
        if (!isStringValida(value) || !isStringValida(format))
            throw new IllegalStateException("Conteudo do valor p/ conversao de data/hora inválida e/ou inexistente.");

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));
    }

    /**
     * Extrai apenas os dígitos numéricos de uma string e os converte para o tipo {@code long}.
     *
     * @param base Texto a ser processado para extração numérica.
     * @return O valor convertido para {@code long}, ou {@code 0L} caso a string seja inválida ou vazia.
     */
    public static long onlyLongNumbers(String base) {
        return (isStringValida(base)) ? Long.parseLong(onlyNumbers(base)) : 0L;
    }

    /**
     * Remove todos os caracteres não numéricos de uma string.
     *
     * @param base Texto de origem.
     * @return Uma nova {@link String} contendo apenas dígitos de 0 a 9, ou {@link StringUtils#EMPTY} se a base for nula.
     */
    public static String onlyNumbers(String base) {
        return (Objects.isNull(base)) ? StringUtils.EMPTY : base.replaceAll("\\D", StringUtils.EMPTY);
    }

    /**
     * Converte o conteúdo numérico de uma string para o tipo primitivo {@code int}.
     *
     * @param base Texto contendo o número a ser convertido.
     * @return O valor inteiro convertido, ou {@code 0} caso a string seja inválida.
     */
    public static int onlyIntNumbers(String base) {
        return onlyIntegerNumbers(base);
    }

    /**
     * Verifica se uma string é válida (não é nula, não é vazia e não contém apenas espaços em branco).
     *
     * @param value Texto a ser validado.
     * @return {@code true} se a string contiver caracteres válidos; {@code false} caso contrário.
     */
    public static boolean isStringValida(final String value) {
        return (isNotEmpty(value) && isNotBlank(value));
    }

    /**
     * Converte o conteúdo numérico de uma string para um objeto {@link Integer}.
     *
     * @param base Texto contendo o número a ser convertido.
     * @return O objeto {@link Integer} convertido, ou {@code 0} caso a string seja inválida.
     */
    public static Integer onlyIntegerNumbers(String base) {
        return isStringValida(base) ? Integer.parseInt(base) : 0;
    }

    /**
     * Formata um valor numérico do tipo {@link Double} em uma representação textual decimal com 2 casas decimais.
     *
     * @param value Valor numérico {@link Double} a ser formatado.
     * @return O valor formatado como {@link String} com duas casas decimais e separador de ponto.
     * @throws IllegalArgumentException Se o parâmetro {@code value} for nulo.
     */
    public static String formatDecimalNumberBy(Double value) {
        return formatDecimalNumber(BigDecimal.valueOf(value));
    }

    /**
     * Formata um valor {@link BigDecimal} ajustando a escala para 2 casas decimais com arredondamento {@link RoundingMode#HALF_UP}.
     *
     * @param value O valor {@link BigDecimal} a ser formatado.
     * @return O valor formatado como {@link String} com duas casas decimais e ponto como separador.
     * @throws IllegalArgumentException Se o parâmetro {@code value} for nulo.
     */
    public static String formatDecimalNumber(BigDecimal value) {
        validateValorNumericoFormatCasasDecimais(value);
        value = value.setScale(2, RoundingMode.HALF_UP);

        var symbols = new DecimalFormatSymbols(PT_BR);
        symbols.setDecimalSeparator('.');

        var format = new DecimalFormat("##0.00", symbols);
        return format.format(value);
    }

    /**
     * Formata uma string com 11 dígitos numéricos no padrão visual de CPF (000.000.000-00).
     *
     * @param cpf String contendo os dígitos do CPF (com ou sem caracteres especiais).
     * @return O CPF formatado no padrão "XXX.XXX.XXX-XX".
     */
    public static String formatCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    /**
     * Valida se o objeto {@link BigDecimal} informado é nulo antes da formatação.
     *
     * @param number Objeto a ser verificado.
     * @throws IllegalArgumentException Se o parâmetro {@code number} for nulo.
     */
    private static void validateValorNumericoFormatCasasDecimais(BigDecimal number) {
        if (Objects.isNull(number))
            throw new IllegalArgumentException("Valor numerico encontra-se inválido e/ou inexsitente.");
    }
}
