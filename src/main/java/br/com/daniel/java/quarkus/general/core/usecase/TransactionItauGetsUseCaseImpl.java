package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.port.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.core.port.TransactionItauPort;
import br.com.daniel.java.quarkus.general.core.usecase.output.TransactionItauOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

@Singleton
@Slf4j
public class TransactionItauGetsUseCaseImpl implements TransactionItauGetsUseCase {

    @Inject
    TransactionItauPort transactionItauPort;

    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;

    @Override
    public List<TransactionItauOutput> getAll() {
        log.info("Inicializando a busca todas as transações existentes.");

        return transactionItauPort.getAllTransactions()
                .stream()
                .map(TransactionItauOutput::from)
                .toList();
    }

    @Override
    public TransactionItauOutput getById(final String transactionId) {
        log.info("Inicializando busca transação por meio do id da transação. Id da transação: {}", transactionId);

        return TransactionItauOutput.from(
                transactionItauPort.getById(Long.valueOf(transactionId))
                        .orElseThrow(() ->
                                new TransactionItauNotFoundException(
                                        "Nenhuma transação encontrada por meio do id da transação %s.".formatted(transactionId)
                                )
                        )
        );
    }

    @Override
    public List<TransactionItauOutput> getAllTransactionsByRange(Integer secondsRange) {
        log.info("Inicializando busca de todas as transações por meio do período. Intervalo em segundos: {}", secondsRange);

        var dateTimeRange = OffsetDateTime.now().minusSeconds(secondsRange);
        var transactionItauList = transactionItauMemoryPort.getTransactionsByDateTime(dateTimeRange);

        if (transactionItauList.isEmpty()) {
            log.error("Não foram encontradas transações para o intervalo de %d segundos.".formatted(secondsRange));
            throw new TransactionItauNotFoundException("Não foram encontradas transações para o intervalo de %d segundos.".formatted(secondsRange));
        }

        return transactionItauList
                .stream()
                .map(TransactionItauOutput::from)
                .toList();
    }
}
