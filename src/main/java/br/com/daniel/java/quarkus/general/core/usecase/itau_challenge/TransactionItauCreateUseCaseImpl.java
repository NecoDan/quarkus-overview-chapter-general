package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauPort;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.TransactionItauOutput;
import br.com.daniel.java.quarkus.general.exceptions.EntityCreateFailedException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class TransactionItauCreateUseCaseImpl implements TransactionItauCreateUseCase {

    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;

    @Inject
    TransactionItauPort transactionItauPort;

    @Override
    public TransactionItauOutput createTransaction(TransactionItauInput input) {
        log.info("Inicializando processamento de criação de transação. Payload: {}", input);
        validate(input);

        try {
            return TransactionItauOutput.from(
                    transactionItauMemoryPort.createTransaction(new TransactionItau(input))
            );
        } catch (Exception e) {
            log.error("Falha ao criar a transação. Payload: {}. Erro: {}", input, e.getMessage());
            throw new EntityCreateFailedException("Falha ao criar a transação. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), e
            );
        }
    }

    @Override
    public TransactionItauOutput createNewTransaction(TransactionItauInput input) {
        log.info("Inicializando processamento de criação de uma nova transação. Payload: {}", input);
        validate(input);

        try {
            return TransactionItauOutput.from(
                    transactionItauPort.createTransaction(new TransactionItau(input))
            );
        } catch (Exception e) {
            log.error("Falha ao criar a transação. Payload: {}. Erro: {}", input, e.getMessage());
            throw new EntityCreateFailedException("Falha ao criar a transação. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), e
            );
        }
    }

    @Override
    public boolean isCreatedAtInvalid(TransactionItauInput input) {
        return input.createdAt() == null || input.createdAt().isAfter(OffsetDateTime.now());
    }

    private void validate(TransactionItauInput input) {
        if (input.amount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("O valor da transação deve ser maior que zero. Payload: {}", input);
            throw new EntityCreateFailedException("O valor da transação deve ser maior que zero.");
        }

        if (isCreatedAtInvalid(input)) {
            log.error("A data de criação da transação é obrigatória. Data e hora maiores que o momento atual não são permitidos. Payload: {}", input);
            throw new EntityCreateFailedException("A data/hora de criação da transação é obrigatória. Data e hora maiores que data atual não são permitidos.");
        }
    }
}
