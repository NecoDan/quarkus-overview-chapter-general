package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauPort;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.exceptions.EntityCreateFailedException;
import br.com.daniel.java.quarkus.general.util.factory.ItauTransactionFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TransactionItauCreateUseCase")
@Transactional
class TransactionItauCreateUseCaseImplTest {

    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;

    @Inject
    TransactionItauPort transactionItauPort;

    @Inject
    TransactionItauCreateUseCaseImpl transactionItauCreateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar transaction retorna DTO")
    void createTransactionAndReturnAsDTO() {
        // -- 01_Cenário
        var transactionDTO = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        // -- 02_Ação
        var result = transactionItauCreateUseCase.createTransaction(transactionDTO);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertTrue(Long.parseLong(result.id()) > BigDecimal.ZERO.intValue());

        assertNotNull(result.transactionId());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertNotNull(UUID.fromString(result.transactionId()));
    }

    @Test
    @DisplayName("Deve criar uma nova transaction retorna DTO")
    void createNewTransactionAndReturnAsDTO() {
        // -- 01_Cenário
        var transactionDTO = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        // -- 02_Ação
        var result = transactionItauCreateUseCase.createNewTransaction(transactionDTO);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertTrue(Long.parseLong(result.id()) > BigDecimal.ZERO.intValue());

        assertNotNull(result.transactionId());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertNotNull(UUID.fromString(result.transactionId()));

        assertEquals(transactionDTO.documentNumber(), result.userDocument());
        assertEquals(transactionDTO.creditCardToken(), result.creditCardToken());
    }

    @Test
    @DisplayName("Deve lançar exception quando o valor da transação for menor ou igual a zero")
    void throwExceptionWhenTransactionAmountIsZeroOrNegative() {
        // -- 01_Cenário
        var transactionItauInputVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItauInputVar2 = new TransactionItauInput(BigDecimal.ZERO,
                transactionItauInputVar1.createdAt(),
                transactionItauInputVar1.documentNumber(),
                transactionItauInputVar1.creditCardToken()
        );

        // -- 02_Ação
        var exception = assertThrows(EntityCreateFailedException.class,
                () -> transactionItauCreateUseCase.createTransaction(transactionItauInputVar2)
        );

        // -- 03_Verificação_Validação
        assertEquals("O valor da transação deve ser maior que zero.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception quando a data de criação for nula")
    void throwExceptionWhenTransactionCreatedAtIsNull() {
        // -- 01_Cenário
        var transactionItauInputVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItauInputVar2 = new TransactionItauInput(transactionItauInputVar1.amount(),
                null,
                transactionItauInputVar1.documentNumber(),
                transactionItauInputVar1.creditCardToken()
        );

        // -- 02_Ação
        var exception = assertThrows(EntityCreateFailedException.class,
                () -> transactionItauCreateUseCase.createTransaction(transactionItauInputVar2)
        );

        // -- 03_Verificação_Validação
        assertEquals("A data/hora de criação da transação é obrigatória. Data e hora maiores que data " +
                "atual não são permitidos.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception quando a data de criação for maior que a data atual")
    void throwExceptionWhenTransactionCreatedAtIsInFuture() {
        // -- 01_Cenário
        var transactionItauInputVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItauInputVar2 = new TransactionItauInput(
                transactionItauInputVar1.amount(),
                OffsetDateTime.now().plusDays(1),
                transactionItauInputVar1.documentNumber(),
                transactionItauInputVar1.creditCardToken()
        );

        // -- 02_Ação
        var exception = assertThrows(EntityCreateFailedException.class,
                () -> transactionItauCreateUseCase.createTransaction(transactionItauInputVar2)
        );

        // -- 03_Verificação_Validação
        assertEquals("A data/hora de criação da transação é obrigatória. Data e hora maiores que data " +
                "atual não são permitidos.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar transação com valor válido e data válida")
    void createTransactionWithValidAmountAndCreatedAt() {
        // -- 01_Cenário
        var transactionItauInputVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItauInputVar2 = new TransactionItauInput(
                transactionItauInputVar1.amount(),
                OffsetDateTime.now(),
                transactionItauInputVar1.documentNumber(),
                transactionItauInputVar1.creditCardToken()
        );

        // -- 02_Ação
        var result = transactionItauCreateUseCase.createTransaction(transactionItauInputVar2);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertTrue(Long.parseLong(result.id()) > BigDecimal.ZERO.intValue());

        assertNotNull(result.transactionId());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertNotNull(UUID.fromString(result.transactionId()));

        assertEquals(transactionItauInputVar2.documentNumber(), result.userDocument());
        assertEquals(transactionItauInputVar2.creditCardToken(), result.creditCardToken());
    }
}