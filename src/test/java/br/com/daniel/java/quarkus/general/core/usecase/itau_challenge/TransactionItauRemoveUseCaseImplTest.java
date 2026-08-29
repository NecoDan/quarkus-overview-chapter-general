package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauMemoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionItauRemoveUseCaseImplTest {

    @Mock
    TransactionItauMemoryPort transactionItauMemoryPort;

    @InjectMocks
    TransactionItauRemoveUseCaseImpl useCase;

    @Test
    void deletesTransactionById() {
        useCase.deleteById("transaction-id");

        verify(transactionItauMemoryPort).deleteById("transaction-id");
    }

    @Test
    void deletesAllTransactions() {
        useCase.deleteAll();

        verify(transactionItauMemoryPort).deleteAll();
    }
}
