package br.com.daniel.java.quarkus.general.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsTest {

    @Test
    void gerarValorRandomicoIntegerWithinLimits() {
        int value = RandomUtils.gerarValorRandomicoInteger();
        assertTrue(value >= 1 && value <= 500000);
    }

    @Test
    void gerarValorRandomicoDecimalWithinLimits() {
        BigDecimal value = RandomUtils.gerarValorRandomicoDecimal();
        assertTrue(value.compareTo(BigDecimal.valueOf(1)) >= 0 && value.compareTo(BigDecimal.valueOf(500000)) <= 0);
    }

    @Test
    void gerarValorRandomicoDecimalLimiteHandlesNull() {
        BigDecimal value = RandomUtils.gerarValorRandomicoDecimalLimite(null);
        assertEquals(BigDecimal.ZERO, value);
    }

    @Test
    void gerarDataRandomicaAteWithinRange() {
        LocalDate date = RandomUtils.gerarDataRandomicaAte();
        assertTrue(date.isAfter(LocalDate.of(1979, 12, 31)) && date.isBefore(LocalDate.of(2002, 1, 1)));
    }

    @Test
    void numerosImparesPorIntervaloReturnsEmptyForInvalidRange() {
        List<Integer> result = RandomUtils.numerosImparesPorIntervalo(-5, -1);
        assertTrue(result.isEmpty());
    }

    @Test
    void numerosImparesPorIntervaloReturnsOddNumbers() {
        List<Integer> result = RandomUtils.numerosImparesPorIntervalo(1, 10);
        assertEquals(List.of(1, 3, 5, 7, 9), result);
    }

    @Test
    void generateListLimitedIntegerRandomValueFromHandlesZeroElements() {
        List<Integer> result = RandomUtils.generateListLimitedIntegerRandomValueFrom(0);
        assertTrue(result.isEmpty());
    }

    @Test
    void generateListLimitedDecimalRandomValueFromHandlesNegativeElements() {
        List<BigDecimal> result = RandomUtils.generateListLimitedDecimalRandomValueFrom(-5);
        assertTrue(result.isEmpty());
    }

    @Test
    void gerarIdentidadeRandomGeneratesUniqueIdentifiers() {
        String id1 = RandomUtils.gerarIdentidadeRandom();
        String id2 = RandomUtils.gerarIdentidadeRandom();
        assertNotEquals(id1, id2);
    }
}