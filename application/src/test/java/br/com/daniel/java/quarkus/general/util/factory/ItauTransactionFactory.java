package br.com.daniel.java.quarkus.general.util.factory;

import br.com.daniel.java.quarkus.general.core.domain.itau_challenge.TransactionItau;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.utils.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ItauTransactionFactory {

    private ItauTransactionFactory() {
        throw new IllegalStateException("Utility class ItauTransactionFactory");
    }

    public static TransactionItau buildMockTransactionItau() {
        return TransactionItau.builder()
                .id(RandomUtils.gerarValorRandomicoLong())
                .transactionId(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(100.00))
                .transactionValue(BigDecimal.valueOf(100.00).longValue())
                .createdAt(LocalDateTime.now())
                .rawUserDocument("08104138090")
                .rawCreditCardToken("Sollicitudin orci laoreet ornare consectetur per risus facilisis nunc blandit aptent, sit aenean litora nunc congue viverra dapibus per sagittis.")
                .build();
    }

    public static TransactionItau buildMockTransactionItauToSave() {
        return TransactionItau.builder()
                .amount(BigDecimal.valueOf(100.00))
                .createdAt(LocalDateTime.now())
                .transactionValue(BigDecimal.valueOf(100.00).longValue())
                .rawUserDocument("08104138090")
                .rawCreditCardToken("Sollicitudin orci laoreet ornare consectetur per risus facilisis nunc blandit aptent, sit aenean litora nunc congue viverra dapibus per sagittis.")
                .build();
    }

    public static TransactionItauInput buildMockTransactionItauRequestDTO() {
        return new TransactionItauInput(
                BigDecimal.valueOf(100.00),
                LocalDateTime.now().atOffset(java.time.ZoneOffset.UTC),
                "65767883041",
                "Sollicitudin orci laoreet ornare consectetur per risus facilisis nunc blandit aptent, sit aenean litora nunc congue viverra dapibus per sagittis."
        );
    }
}
