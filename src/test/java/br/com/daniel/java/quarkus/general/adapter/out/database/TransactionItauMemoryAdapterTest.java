package br.com.daniel.java.quarkus.general.adapter.out.database;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.TransactionItauMemoryAdapter;
import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionItauMemoryAdapterTest {

    @Test
    void createTransactionInMemoryShouldAddTransactionToList() {
        // -- 01_Cenário
        final var transactionItauMemoryAdapter = new TransactionItauMemoryAdapter();
        final var transactionItau = new TransactionItau();

        // -- 02_Ação
        final var createdTransaction = transactionItauMemoryAdapter.createTransaction(transactionItau);

        // -- 03_Verificação_Validação
        assertNotNull(createdTransaction.getId());
        assertEquals(1, transactionItauMemoryAdapter.getAllTransactions().size());
    }

    @Test
    void getByIdInMemoryShouldReturnTransactionWhenIdExists() {
        // -- 01_Cenário
        final var transactionItauMemoryAdapter = new TransactionItauMemoryAdapter();
        final var transactionItau = new TransactionItau();
        final var createdTransactionExample = transactionItauMemoryAdapter.createTransaction(transactionItau);

        // -- 02_Ação
        var result = transactionItauMemoryAdapter.getById(createdTransactionExample.getTransactionId());

        // -- 03_Verificação_Validação
        assertTrue(result.isPresent());
        assertEquals(createdTransactionExample.getId(), result.get().getId());
    }

    @Test
    void getByIdInMemoryShouldReturnEmptyWhenIdDoesNotExist() {
        // -- 01_Cenário
        final var transactionItauMemoryAdapter = new TransactionItauMemoryAdapter();

        // -- 02_Ação
        var result = transactionItauMemoryAdapter.getById(UUID.randomUUID().toString());

        // -- 03_Verificação_Validação
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteByIdInMemoryShouldRemoveTransactionWhenIdExists() {
        // -- 01_Cenário
        final var transactionItauMemoryAdapter = new TransactionItauMemoryAdapter();
        final var transactionItau = new TransactionItau();
        final var createdTransactionItauExample = transactionItauMemoryAdapter.createTransaction(transactionItau);

        // -- 02_Ação
        transactionItauMemoryAdapter.deleteById(createdTransactionItauExample.getTransactionId());

        // -- 03_Verificação_Validação
        assertTrue(transactionItauMemoryAdapter.getById(createdTransactionItauExample.getTransactionId()).isEmpty());
        assertEquals(0, transactionItauMemoryAdapter.getAllTransactions().size());
    }

    @Test
    void deleteByIdInMemoryShouldThrowExceptionWhenIdDoesNotExist() {
        TransactionItauMemoryAdapter business = new TransactionItauMemoryAdapter();

        assertThrows(TransactionItauNotFoundException.class, () -> business.deleteById(UUID.randomUUID().toString()));
    }

    @Test
    void getTransactionsByDateTimeInMemoryShouldReturnTransactionsWithinDateRange() {
        // -- 01_Cenário
        final var transactionItauBusiness = new TransactionItauMemoryAdapter();

        final var transaction1 = new TransactionItau();
        transaction1.setCreatedAt(OffsetDateTime.now().minusDays(1).toLocalDateTime());

        final var transaction2 = new TransactionItau();
        transaction2.setCreatedAt(OffsetDateTime.now().minusHours(1).toLocalDateTime());
        transactionItauBusiness.createTransaction(transaction1);
        transactionItauBusiness.createTransaction(transaction2);

        final var filtroOffsetDateTime = OffsetDateTime.now().minusDays(2);

        // -- 02_Ação
        var result = transactionItauBusiness.getTransactionsByDateTime(filtroOffsetDateTime);

        // -- 03_Verificação_Validação
        assertEquals(2, result.size());
    }

    @Test
    void getTransactionsByDateTimeInMemoryShouldReturnEmptyWhenNoTransactionsInRange() {
        // -- 01_Cenário
        final var transactionItauBusiness = new TransactionItauMemoryAdapter();

        final var transaction = new TransactionItau();
        final var dtCriacao = OffsetDateTime.now().minusDays(3).toLocalDateTime();

        transaction.setCreatedAt(dtCriacao);
        transactionItauBusiness.createTransaction(transaction);

        final var filtroOffsetDateTime = OffsetDateTime.now().minusDays(1);

        // -- 02_Ação
        var result = transactionItauBusiness.getTransactionsByDateTime(filtroOffsetDateTime);

        // -- 03_Verificação_Validação
        assertTrue(result.isEmpty());
    }

    @Test
    void getTransactionsByDateTimeInMemoryShouldReturnEmptyWhenNoTransactionsExist() {
        // -- 01_Cenário
        final var transactionItauBusiness = new TransactionItauMemoryAdapter();

        // -- 02_Ação
        var result = transactionItauBusiness.getTransactionsByDateTime(OffsetDateTime.now());

        // -- 03_Verificação_Validação
        assertTrue(result.isEmpty());
    }
}