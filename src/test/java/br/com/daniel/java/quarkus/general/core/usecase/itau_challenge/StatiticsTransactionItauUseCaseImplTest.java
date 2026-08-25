package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.TransactionItauMemoryAdapter;
import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatiticsTransactionItauUseCaseImplTest {

    @Mock
    TransactionItauMemoryAdapter transactionItauBusiness;

    @InjectMocks
    StatiticsTransactionItauUseCaseImpl useCase;

    @Test
    void calculatesStatisticsFromTransactionsInRange() {
        when(transactionItauBusiness.getTransactionsByDateTime(any())).thenReturn(List.of(
                TransactionItau.builder().amount(new BigDecimal("10.00")).build(),
                TransactionItau.builder().amount(new BigDecimal("20.00")).build(),
                TransactionItau.builder().amount(new BigDecimal("30.00")).build()
        ));

        var statistics = useCase.calculateStatistics(60);

        assertEquals(3L, statistics.count());
        assertEquals(60.0, statistics.sum());
        assertEquals(20.0, statistics.avg());
        assertEquals(10.0, statistics.min());
        assertEquals(30.0, statistics.max());
    }
}
