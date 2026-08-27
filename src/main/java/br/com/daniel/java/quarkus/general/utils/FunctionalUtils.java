package br.com.daniel.java.quarkus.general.utils;

import io.smallrye.config.SmallRyeConfig;
import org.apache.commons.lang3.StringUtils;
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

import static org.apache.commons.lang3.StringUtils.*;

public final class FunctionalUtils {

    private FunctionalUtils() {
        throw new IllegalStateException("This is a utility class FunctionalUtils and cannot be instantiated");
    }

    public static final String BR_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private static final Locale PT_BR = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

    public static List<String> getActiveProfiles() {
        return ConfigProvider.getConfig().unwrap(SmallRyeConfig.class).getProfiles();
    }

    public static String formatCreationDate(LocalDateTime localDateTime) {
        return (Objects.isNull(localDateTime)) ? StringUtils.EMPTY : formatCreationDateBy(localDateTime);
    }

    public static String formatCreationDateBy(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(BR_DATETIME_FORMAT));
    }

    public static LocalDateTime onlyLocalDateTimeDefaultEnglish(String value) {
        if (!isStringValida(value))
            throw new IllegalStateException("Conteudo do valor p/ conversao de data/hora inválida e/ou inexistente.");

        if (value.length() > 19) {
            value = value.substring(0, 19);
        }

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime onlyLocalDateTimeBy(final String value,
                                                    final String format) {
        if (!isStringValida(value) || !isStringValida(format))
            throw new IllegalStateException("Conteudo do valor p/ conversao de data/hora inválida e/ou inexistente.");

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));
    }

    public static long onlyLongNumbers(String base) {
        return (isStringValida(base))
                ? Long.parseLong(onlyNumbers(base))
                : 0L;
    }

    public static String onlyNumbers(String base) {
        return (Objects.isNull(base)) ? StringUtils.EMPTY : base.replaceAll("\\D", StringUtils.EMPTY);
    }

    public static int onlyIntNumbers(String base) {
        return onlyIntegerNumbers(base);
    }

    public static boolean isStringValida(final String value) {
        return (isNotEmpty(value) && isNotBlank(value));
    }

    public static Integer onlyIntegerNumbers(String base) {
        return isStringValida(base)
                ? Integer.parseInt(base)
                : 0;
    }

    public static String formatDecimalNumberBy(Double value) {
        return formatDecimalNumber(BigDecimal.valueOf(value));
    }

    public static String formatDecimalNumber(BigDecimal value) {
        validateValorNumericoFormatCasasDecimais(value);
        value = value.setScale(2, RoundingMode.HALF_UP);

        var symbols = new DecimalFormatSymbols(PT_BR);
        symbols.setDecimalSeparator('.');

        var format = new DecimalFormat("##0.00", symbols);
        return format.format(value);
    }

    public static String formatCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static void validateValorNumericoFormatCasasDecimais(BigDecimal number) {
        if (Objects.isNull(number))
            throw new IllegalArgumentException("Valor numerico encontra-se inválido e/ou inexsitente.");
    }
}
