package br.com.daniel.java.quarkus.general.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalUtilsTest {

    @Test
    void formatsCreationDatesAndHandlesNull() {
        var date = LocalDateTime.of(2026, 8, 25, 14, 30, 45);

        assertEquals("25/08/2026 14:30:45", FunctionalUtils.formatCreationDate(date));
        assertEquals("25/08/2026 14:30:45", FunctionalUtils.formatCreationDateBy(date));
        assertEquals("", FunctionalUtils.formatCreationDate(null));
    }

    @Test
    void formatsNumbersWithTwoDecimalPlaces() {
        assertEquals("10.24", FunctionalUtils.formatDecimalNumber(new BigDecimal("10.235")));
        assertEquals("10.50", FunctionalUtils.formatDecimalNumberBy(10.5));
    }

    @Test
    void rejectsNullNumericValue() {
        var error = assertThrows(IllegalArgumentException.class,
                () -> FunctionalUtils.formatDecimalNumber(null));

        assertEquals("Valor numerico encontra-se inválido e/ou inexsitente.", error.getMessage());
    }

    @Test
    void formatsCpfWithOrWithoutPunctuation() {
        assertEquals("123.456.789-01", FunctionalUtils.formatCpf("12345678901"));
        assertEquals("123.456.789-01", FunctionalUtils.formatCpf("123.456.789-01"));
    }
}
