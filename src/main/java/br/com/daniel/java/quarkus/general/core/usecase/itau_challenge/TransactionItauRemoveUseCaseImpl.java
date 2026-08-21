package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.core.port.TransactionItauMemoryPort;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class TransactionItauRemoveUseCaseImpl implements TransactionItauRemoveUseCase {

    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;

    @Override
    public void deleteById(final String transactionId) {
        log.info("Inicializando exclusão transação por meio do id da transação. Id da transação: {}", transactionId);
        transactionItauMemoryPort.deleteById(transactionId);
    }

    @Override
    public void deleteAll() {
        log.info("Inicializando a exclusão todas as transações existentes.");
        transactionItauMemoryPort.deleteAll();
    }
}
