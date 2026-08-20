package br.com.daniel.java.quarkus.general.adapter.out.database;

import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import br.com.daniel.java.quarkus.general.core.port.TransactionItauMemoryPort;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauCreateFailedException;
import br.com.daniel.java.quarkus.general.exceptions.api.TransactionItauNotFoundException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class TransactionItauMemoryAdapter implements TransactionItauMemoryPort {

    private final List<TransactionItau> transactionItauList = new ArrayList<>();

    @Override
    public List<TransactionItau> getAllTransactions() {
        log.info("Buscando todas as transações existentes.");
        return this.transactionItauList;
    }

    @Override
    public List<TransactionItau> getTransactionsByDateTime(OffsetDateTime dateTimeRange) {
        log.info("Buscandos transações existentes criadas no intervalo de {}", dateTimeRange);

        return this.getAllTransactions()
                .stream()
                .filter(transactionItau -> transactionItau.getCreatedAt().isAfter(dateTimeRange.toLocalDateTime()))
                .toList();
    }

    @Override
    public TransactionItau createTransaction(TransactionItau transactionItau) {
        log.info("Criando uma nova transação. Payload: {}", transactionItau);

        final var transactionId = UUID.randomUUID().toString();
        transactionItau.setId(RandomUtils.secureStrong().randomLong(1, 1000000));
        transactionItau.setTransactionId(transactionId);
        this.transactionItauList.add(transactionItau);

        return getById(transactionId)
                .orElseThrow(
                        () -> new TransactionItauCreateFailedException("Falha ao criar a transação.")
                );
    }

    @Override
    public Optional<TransactionItau> getById(final String transactionId) {
        log.info("Buscando transação por meio do id da transação {}.", transactionId);

        return this.transactionItauList.stream()
                .filter(transactionItau -> transactionId.equals(transactionItau.getTransactionId()))
                .findFirst();
    }

    @Override
    public void deleteById(String transactionId) {
        log.info("Excluindo transação por meio do id da transação {}.", transactionId);

        getById(transactionId)
                .orElseThrow(() ->
                        new TransactionItauNotFoundException("Nenhuma transação encontrada por meio do id da transação %s.".formatted(transactionId))
                );

        this.transactionItauList.removeIf(transactionItau -> transactionId.equals(transactionItau.getTransactionId()));
    }

    @Override
    public void deleteAll() {
        log.info("Excluindo todas as transações existentes.");

        if (CollectionUtils.isEmpty(this.transactionItauList)) {
            throw new TransactionItauNotFoundException("Nenhuma transação encontrada para ser deletada.");
        }

        this.transactionItauList.clear();
    }
}
