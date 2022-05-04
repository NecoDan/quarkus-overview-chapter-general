package br.com.daniel.java.quarkus.general.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class RandomicoUtil {

    private static final int LIMIT_MIN_RANDOM_INTEGER = 1;
    private static final int LIMIT_MAX_RANDOM_INTEGER = 500000;

    private static final double LIMIT_MIN_RANDOM_DOUBLE = 1D;
    private static final double LIMIT_MAX_RANDOM_DOUBLE = 500000D;

    private static final String LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
    private static final Set<String> IDENTIFICADORES = new HashSet<>();

    private RandomicoUtil() {
    }

    private static Random getRandom() {
        return new Random();
    }

    private static int gerarValorRandomico() {
        return LIMIT_MIN_RANDOM_INTEGER + getRandom().nextInt(LIMIT_MAX_RANDOM_INTEGER);
    }

    public static Long gerarValorRandomicoLong() {
        return (long) gerarValorRandomico();
    }

    public static Integer gerarValorRandomicoInteger() {
        return gerarValorRandomico();
    }

    public static Integer gerarValorRandomicoAte(int max) {
        return LIMIT_MIN_RANDOM_INTEGER + getRandom().nextInt(max);
    }

    public static BigDecimal gerarValorRandomicoDecimal() {
        return getValorRandomicoDecimal(LIMIT_MAX_RANDOM_DOUBLE);
    }

    public static BigDecimal gerarValorRandomicoDecimalLimite(BigDecimal limite) {
        return (Objects.isNull(limite)) ? BigDecimal.ZERO : getValorRandomicoDecimal(limite.doubleValue());
    }

    private static BigDecimal getValorRandomicoDecimal(double limiteMax) {
        return BigDecimal.valueOf(LIMIT_MIN_RANDOM_DOUBLE + getRandom().nextDouble() * (limiteMax - LIMIT_MIN_RANDOM_DOUBLE))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static double gerarValorRandomicoDouble() {
        return gerarValorRandomicoDecimal().doubleValue();
    }

    public static double gerarValorRandomicoDoubleLimite(double limite) {
        return gerarValorRandomicoDecimalLimite(BigDecimal.valueOf(limite)).doubleValue();
    }

    public static LocalDate gerarDataRandomicaAte() {
        long minDay = LocalDate.of(1980, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2001, 12, 31).toEpochDay();

        long randoDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randoDay);
    }

    public static List<Integer> gerarNumerosAleatoriosPorIntervalo(int minValor, int maxValor, boolean repetidos) {
        int min = minValor;
        int max = maxValor;

        if (maxValor < minValor) {
            min = maxValor;
            max = minValor;
        }

        Random random = ThreadLocalRandom.current();
        return random.ints(max, 0, max)
                .sorted()
                .boxed()
                .distinct()
                .collect(Collectors.toList());
    }

    public static int mininmoSoma(List<Integer> numeros, int k) {
        numeros = numeros.stream().sorted().collect(Collectors.toList());

        for (int i = 0; i <= k; i++) {
            for (int j = 0; j < numeros.size(); j++) {
                if (numeros.get(j) < numeros.get(j + 1)) {
                    int menorValor = numeros.get(j);

                    if (menorValor != k) {
                        if (menorValor % 2 != 0) {
                            numeros.set(j, k);
                        } else {
                            numeros.set(j, (menorValor / 2));
                        }
                        break;
                    }
                }
            }
        }

        return numeros.stream().mapToInt(n -> n).sum();
    }

    public static List<Integer> numerosImparesPorIntervalo(int numeroInicial, int numeroFinal) {
        return (numeroInicial <= 0 || numeroFinal <= 0) ? Collections.emptyList() : getNumerosImparesPorIntervalo(numeroInicial, numeroFinal);
    }

    private static List<Integer> getNumerosImparesPorIntervalo(int numeroInicial, int numeroFinal) {
        int min = numeroInicial;
        int max = numeroFinal;

        if (numeroFinal < numeroInicial) {
            min = numeroFinal;
            max = numeroInicial;
        }

        List<Integer> valoresPorIntervalo = new ArrayList<>();
        for (int i = min; i <= max; i++)
            valoresPorIntervalo.add(i);

        return valoresPorIntervalo.stream().filter(num -> num % 2 != 0).collect(Collectors.toList());
    }

    public static List<Integer> generateListLimitedIntegerRandomValueFrom(int totalElements) {
        return (totalElements <= 0) ? Collections.emptyList() : generateLocalListLimitedIntegerRandomValueFrom(totalElements);
    }

    public static List<BigDecimal> generateListLimitedDecimalRandomValueFrom(int totalElements) {
        return (totalElements <= 0) ? Collections.emptyList() : generateLocalListLimitedDecimalRandomValueFrom(totalElements);
    }

    private static List<Integer> generateLocalListLimitedIntegerRandomValueFrom(int totalElements) {
        List<Integer> integerArrayList = new ArrayList<>();
        int increment = 0;
        do {
            integerArrayList.add(gerarValorRandomicoInteger());
            increment++;
        } while (increment < totalElements);
        return integerArrayList;
    }

    private static List<BigDecimal> generateLocalListLimitedDecimalRandomValueFrom(int totalElements) {
        List<BigDecimal> list = new ArrayList<>();
        int increment = 0;
        do {
            list.add(gerarValorRandomicoDecimal());
            increment++;
        } while (increment < totalElements);
        return list;
    }

    public static String gerarIdentidadeRandom() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = getRandom().nextInt(5) + 5;

            for (int i = 0; i < length; i++) {
                builder.append(LEXICON.charAt(getRandom().nextInt(LEXICON.length())));
            }

            if (IDENTIFICADORES.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }

        return builder.toString();
    }

}
