package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.TransactionItauAdapter;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.TransactionItauOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import br.com.daniel.java.quarkus.general.util.factory.ItauTransactionFactory;
import br.com.daniel.java.quarkus.general.utils.RandomUtils;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TransactionItauGetsUseCase")
@Transactional
class TransactionItauGetsUseCaseImplV2Test {

    @Inject
    TransactionItauGetsUseCaseImpl transactionItauGetsUseCase;

    @Inject
    TransactionItauAdapter transactionItauAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
    @DisplayName("Deve retornar todas transactions como DTOs")
    void returnAllTransactionsAsDTOs() {
        // -- 01_Cenário
        transactionItauAdapter.deleteAll();

        transactionItauAdapter.createTransactionBy(
                ItauTransactionFactory.buildMockTransactionItauToSave()
        );

        transactionItauAdapter.createTransactionBy(
                ItauTransactionFactory.buildMockTransactionItauToSave()
        );

        // -- 02_Ação
        var transactionItauOutputList = transactionItauGetsUseCase.getAll();

        // -- 03_Verificação_Validação
        assertNotNull(transactionItauOutputList);
        assertFalse(CollectionUtils.isEmpty(transactionItauOutputList));
        assertEquals(2, transactionItauOutputList.size());
    }

//    @Test
    @DisplayName("Deve retornar transaction por ID como DTO")
    void returnTransactionByIdAsDTO() {
        // -- 01_Cenário
        var transactionItauInput = ItauTransactionFactory.buildMockTransactionItauToSave();
        var transactionCreated = transactionItauAdapter.createTransaction(transactionItauInput);

        // -- 02_Ação
        var result = transactionItauGetsUseCase.getById(String.valueOf(transactionCreated.getId()));

        // -- 03_Verificação_Validação
        assertNotNull(result);
    }

//    @Test
    @DisplayName("Deve lançar exception não encontrar transaction por ID")
    void throwExceptionWhenTransactionIdNotFound() {
        // -- 01_Cenário
        var transactionId = String.valueOf(RandomUtils.gerarValorRandomicoLong());

        // -- 02_Ação
        var exception = assertThrows(TransactionItauNotFoundException.class,
                () -> transactionItauGetsUseCase.getById(transactionId)
        );

        // -- 03_Verificação_Validação
        assertEquals(
                String.format("Nenhuma transação encontrada por meio do id da transação %s.", transactionId),
                exception.getMessage()
        );
    }

//    @Test
    @DisplayName("Deve retornar as transações dentro do intervalo informado")
    void returnTransactionsWithinRange() {
        // -- 01_Cenário
        transactionItauAdapter.deleteAll();

        final var secondsRange = 60;
        final var dateTimeRange = OffsetDateTime.now().minusSeconds(secondsRange);

        var transactionItauInput = ItauTransactionFactory.buildMockTransactionItauToSave();
        transactionItauInput.setCreatedAt(dateTimeRange.plusSeconds(30).toLocalDateTime());
        transactionItauAdapter.createTransactionBy(transactionItauInput);

        // -- 02_Ação
        var transactionItauOutputList = transactionItauGetsUseCase.getAllTransactionsByRange(secondsRange);

        // -- 03_Verificação_Validação
        assertNotNull(transactionItauOutputList);
        assertFalse(CollectionUtils.isEmpty(transactionItauOutputList));
        assertEquals(1, transactionItauOutputList.size());
    }

//    @Test
    @DisplayName("Deve lançar uma exceção quando nenhuma transação for encontrada no intervalo")
    void throwExceptionWhenNoTransactionsFound() {
        // -- 01_Cenário
        var secondsRange = 60;

        // -- 02_Ação
        assertThrows(TransactionItauNotFoundException.class,
                () -> transactionItauGetsUseCase.getAllTransactionsByRange(secondsRange)
        );

        // -- 03_Verificação_Validação
    }

//    @Test
    @DisplayName("Should filter out transactions outside the given range")
    void filterOutTransactionsOutsideRange() {
        // -- 01_Cenário
        var secondsRange = 60;
        var dateTimeRange = OffsetDateTime.now().minusSeconds(secondsRange);

        var transaction1 = ItauTransactionFactory.buildMockTransactionItau();
        transaction1.setCreatedAt(dateTimeRange.minusSeconds(10).toLocalDateTime());

        var transaction2 = ItauTransactionFactory.buildMockTransactionItau();
        transaction2.setCreatedAt(dateTimeRange.plusSeconds(10).toLocalDateTime());


        // -- 02_Ação
        var result = transactionItauGetsUseCase.getAllTransactionsByRange(secondsRange);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertEquals(2, result.size());

        Optional<TransactionItauOutput> optionalDTO = result.stream().findFirst();
        assertNotNull(optionalDTO);
        assertTrue(optionalDTO.isPresent());

        final var transactionItauResponseDTO = optionalDTO.get();
        assertNotNull(transactionItauResponseDTO);
    }

    //    @Test
//    @DisplayName("Deve excluir transaction por ID")
//    void deleteTransactionById() {
//        // -- 01_Cenário
//        var transactionId = UUID.randomUUID().toString();
//
//        doNothing().when(transactionItauMemoryPort)
//                .deleteById(transactionId);
//
//        // -- 02_Ação
//        assertDoesNotThrow(() -> transactionItauRemoveUseCase.deleteById(transactionId));
//
//        // -- 03_Verificação_Validação
//        verify(transactionItauMemoryPort, times(1))
//                .deleteById(transactionId);
//    }

//    @Test
//    @DisplayName("Deve excluir todas as transações existentes")
//    void deleteAllTransactions() {
//        // -- 01_Cenário
//        doNothing().when(transactionItauMemoryPort).deleteAll();
//
//        // -- 02_Ação
//        assertDoesNotThrow(() -> transactionItauRemoveUseCase.deleteAll());
//
//        // -- 03_Verificação_Validação
//        verify(transactionItauMemoryPort, times(1)).deleteAll();
//    }

}