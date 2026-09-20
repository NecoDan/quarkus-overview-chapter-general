package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.itau_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.TransactionItauCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.TransactionItauGetsUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.TransactionItauRemoveUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.TransactionItauOutput;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionItauControllerTest {

    private final TransactionItauCreateUseCase createUseCase = mock(TransactionItauCreateUseCase.class);
    private final TransactionItauGetsUseCase getsUseCase = mock(TransactionItauGetsUseCase.class);
    private final TransactionItauRemoveUseCase removeUseCase = mock(TransactionItauRemoveUseCase.class);
    private TransactionItauController controller;

    @BeforeEach
    void setUp() {
        controller = new TransactionItauController();
        controller.transactionCreateUseCase = createUseCase;
        controller.transactionGetsUseCase = getsUseCase;
        controller.transactionRemoveUseCase = removeUseCase;
    }

    @Test
    void createsMemoryTransactionWithCreatedStatus() {
        var input = input();
        var output = output("1");
        when(createUseCase.createTransaction(input)).thenReturn(output);

        var response = controller.create(input);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertSame(output, response.getEntity());
        verify(createUseCase).createTransaction(input);
    }

    @Test
    void createsPersistentTransactionWithCreatedStatus() {
        var input = input();
        var output = output("2");
        when(createUseCase.createNewTransaction(input)).thenReturn(output);

        var response = controller.createTransaction(input);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertSame(output, response.getEntity());
        verify(createUseCase).createNewTransaction(input);
    }

    @Test
    void returnsAllTransactions() {
        var transactions = List.of(output("1"), output("2"));
        when(getsUseCase.getAll()).thenReturn(transactions);

        var response = controller.getAll();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertSame(transactions, response.getEntity());
    }

    @Test
    void returnsTransactionById() {
        var output = output("10");
        when(getsUseCase.getById("10")).thenReturn(output);

        var response = controller.getById("10");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertSame(output, response.getEntity());
        verify(getsUseCase).getById("10");
    }

    @Test
    void deletesTransactionById() {
        var response = controller.delete("transaction-id");

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(removeUseCase).deleteById("transaction-id");
    }

    @Test
    void deletesAllTransactions() {
        var response = controller.deleteAll();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(removeUseCase).deleteAll();
    }

    private static TransactionItauInput input() {
        return new TransactionItauInput(
                BigDecimal.TEN, OffsetDateTime.now().minusSeconds(1), "123", "token");
    }

    private static TransactionItauOutput output(String id) {
        return new TransactionItauOutput(id, "transaction-" + id, "10.00", "123", "token", "");
    }
}
