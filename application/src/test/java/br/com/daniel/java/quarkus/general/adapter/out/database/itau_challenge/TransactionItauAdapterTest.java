package br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.repository.TransactionItauRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.itau_challenge.TransactionItauEntity;
import br.com.daniel.java.quarkus.general.core.domain.itau_challenge.TransactionItau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionItauAdapterTest {

    private final TransactionItauRepository repository = mock(TransactionItauRepository.class);
    private TransactionItauAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TransactionItauAdapter();
        adapter.transactionItauRepository = repository;
    }

    @Test
    void returnsAllEntitiesAsDomainTransactions() {
        var entity = TransactionItauEntity.builder().id(1L).amount(BigDecimal.TEN).build();
        when(repository.listAll()).thenReturn(List.of(entity));

        var result = adapter.getAllTransactions();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Test
    void persistsTransactionWithGeneratedIdentifier() {
        var transaction = TransactionItau.builder().amount(BigDecimal.TEN).build();

        adapter.createTransactionBy(transaction);

        var entityCaptor = org.mockito.ArgumentCaptor.forClass(TransactionItauEntity.class);
        verify(repository).persistAndFlush(entityCaptor.capture());
        assertNotNull(entityCaptor.getValue().getTransactionId());
    }

    @Test
    void returnsTransactionFoundById() {
        var entity = TransactionItauEntity.builder().id(4L).amount(BigDecimal.TEN).build();
        when(repository.findByIdOptional(4L)).thenReturn(Optional.of(entity));

        var result = adapter.getById(4L);

        assertTrue(result.isPresent());
        assertEquals(4L, result.get().getId());
    }

    @Test
    void returnsTransactionsCreatedAfterRange() {
        var range = OffsetDateTime.now().minusMinutes(1);
        var entity = TransactionItauEntity.builder().id(2L).amount(BigDecimal.TEN).build();
        when(repository.list("createdAt > ?1", range.toLocalDateTime())).thenReturn(List.of(entity));

        var result = adapter.getTransactionsByDateTime(range);

        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().getId());
    }

    @Test
    void delegatesDeletionOperationsToRepository() {
        adapter.deleteById(7L);
        adapter.deleteAll();

        verify(repository).deleteById(7L);
        verify(repository).deleteAll();
    }
}
