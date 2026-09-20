package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauPort;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.exceptions.EntityCreateFailedException;
import br.com.daniel.java.quarkus.general.util.factory.ItauTransactionFactory;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionItauCreateUseCaseImplV2Test {


    @Mock
    TransactionItauMemoryPort transactionItauMemoryPort;

    @Mock
    TransactionItauPort transactionItauPort;

    @InjectMocks
    TransactionItauCreateUseCaseImpl transactionItauCreateUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Deve criar transaction retorna DTO")
    void createTransactionAndReturnAsDTO() {
        // -- 01_Cenário
        var transactionItau = ItauTransactionFactory.buildMockTransactionItauToSave();
        transactionItau.setId(RandomUtils.secure().randomLong(100, 10000L));
        transactionItau.setTransactionId(UUID.randomUUID().toString());

        when(transactionItauMemoryPort.createTransaction(any()))
                .thenReturn(transactionItau);

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
        var transactionDTOVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItau = ItauTransactionFactory.buildMockTransactionItauToSave();
        transactionItau.setId(RandomUtils.secure().randomLong(100, 10000L));
        transactionItau.setTransactionId(UUID.randomUUID().toString());
        transactionItau.setRawUserDocument(transactionDTOVar1.documentNumber());

        when(transactionItauPort.createTransaction(any()))
                .thenReturn(transactionItau);

        // -- 02_Ação
        var result = transactionItauCreateUseCase.createNewTransaction(transactionDTOVar1);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertTrue(Long.parseLong(result.id()) > BigDecimal.ZERO.intValue());

        assertNotNull(result.transactionId());
        assertTrue(StringUtils.isNotEmpty(result.id()));
        assertNotNull(UUID.fromString(result.transactionId()));

        assertEquals(transactionDTOVar1.documentNumber(), result.userDocument());
        assertEquals(transactionDTOVar1.creditCardToken(), result.creditCardToken());
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
        var offsetDateTimeNow = OffsetDateTime.now();
        var transactionItauInputVar1 = ItauTransactionFactory.buildMockTransactionItauRequestDTO();

        var transactionItau = ItauTransactionFactory.buildMockTransactionItauToSave();
        transactionItau.setId(RandomUtils.secure().randomLong(100, 10000L));
        transactionItau.setTransactionId(UUID.randomUUID().toString());
        transactionItau.setRawUserDocument(transactionItauInputVar1.documentNumber());
        transactionItau.setCreatedAt(offsetDateTimeNow.toLocalDateTime());
        transactionItau.setAmount(transactionItauInputVar1.amount());

        when(transactionItauMemoryPort.createTransaction(any()))
                .thenReturn(transactionItau);

        var transactionItauInputVar2 = new TransactionItauInput(
                transactionItauInputVar1.amount(),
                offsetDateTimeNow,
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