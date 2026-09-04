package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.domain.itau_challenge.TransactionItau;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionItauGetsUseCaseImplTest {

    @Mock
    TransactionItauMemoryPort transactionItauMemoryPort;

    @InjectMocks
    TransactionItauGetsUseCaseImpl useCase;

    @Test
    void returnsTransactionsWithinRequestedRange() {
        var transaction = TransactionItau.builder()
                .id(1L)
                .transactionId("id-1")
                .amount(new BigDecimal("10.50"))
                .createdAt(OffsetDateTime.now().minusSeconds(10).toLocalDateTime())
                .build();
        when(transactionItauMemoryPort.getTransactionsByDateTime(any())).thenReturn(List.of(transaction));

        var result = useCase.getAllTransactionsByRange(60);

        assertEquals(1, result.size());
        assertEquals("id-1", result.getFirst().transactionId());

        var rangeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        verify(transactionItauMemoryPort).getTransactionsByDateTime(rangeCaptor.capture());
        assertTrue(rangeCaptor.getValue().isBefore(OffsetDateTime.now()));
    }

    @Test
    void throwsWhenNoTransactionsExistWithinRequestedRange() {
        when(transactionItauMemoryPort.getTransactionsByDateTime(any())).thenReturn(List.of());

        assertThrows(TransactionItauNotFoundException.class,
                () -> useCase.getAllTransactionsByRange(60));
    }
}
