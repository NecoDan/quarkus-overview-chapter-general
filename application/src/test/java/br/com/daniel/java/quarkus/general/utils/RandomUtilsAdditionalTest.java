package br.com.daniel.java.quarkus.general.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsAdditionalTest {

    @Test
    void generatesValuesWithConfiguredUpperLimits() {
        assertTrue(RandomUtils.gerarValorRandomicoLong() >= 1);
        assertTrue(RandomUtils.gerarValorRandomicoAte(10) >= 1);
        assertTrue(RandomUtils.gerarValorRandomicoAte(10) <= 10);
        assertTrue(RandomUtils.gerarValorRandomicoDouble() >= 1D);
        assertTrue(RandomUtils.gerarValorRandomicoDoubleLimite(10D) >= 1D);
        assertTrue(RandomUtils.gerarValorRandomicoDoubleLimite(10D) <= 10D);
    }

    @Test
    void generatesListsWithRequestedSizes() {
        assertEquals(3, RandomUtils.generateListLimitedIntegerRandomValueFrom(3).size());
        assertEquals(3, RandomUtils.generateListLimitedDecimalRandomValueFrom(3).size());
    }

    @Test
    void normalizesOddNumberRangeRegardlessOfOrder() {
        assertEquals(java.util.List.of(3, 5, 7),
                RandomUtils.numerosImparesPorIntervalo(7, 3));
    }

    @Test
    void returnsDecimalWithinExplicitLimit() {
        BigDecimal value = RandomUtils.gerarValorRandomicoDecimalLimite(BigDecimal.TEN);

        assertTrue(value.compareTo(BigDecimal.ONE) >= 0);
        assertTrue(value.compareTo(BigDecimal.TEN) <= 0);
    }
}
