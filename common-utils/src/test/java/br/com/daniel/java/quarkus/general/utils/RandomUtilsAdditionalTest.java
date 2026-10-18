package br.com.daniel.java.quarkus.general.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class RandomUtilsAdditionalTest {

    @Test
    void generatesValuesWithConfiguredUpperLimits() {
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoLong() >= 1);
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoAte(10) >= 1);
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoAte(10) <= 10);
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoDouble() >= 1D);
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoDoubleLimite(10D) >= 1D);
        Assertions.assertTrue(RandomUtils.gerarValorRandomicoDoubleLimite(10D) <= 10D);
    }

    @Test
    void generatesListsWithRequestedSizes() {
        Assertions.assertEquals(3, RandomUtils.generateListLimitedIntegerRandomValueFrom(3).size());
        Assertions.assertEquals(3, RandomUtils.generateListLimitedDecimalRandomValueFrom(3).size());
    }

    @Test
    void normalizesOddNumberRangeRegardlessOfOrder() {
        Assertions.assertEquals(java.util.List.of(3, 5, 7),
                RandomUtils.numerosImparesPorIntervalo(7, 3));
    }

    @Test
    void returnsDecimalWithinExplicitLimit() {
        BigDecimal value = RandomUtils.gerarValorRandomicoDecimalLimite(BigDecimal.TEN);

        Assertions.assertTrue(value.compareTo(BigDecimal.ONE) >= 0);
        Assertions.assertTrue(value.compareTo(BigDecimal.TEN) <= 0);
    }
}
