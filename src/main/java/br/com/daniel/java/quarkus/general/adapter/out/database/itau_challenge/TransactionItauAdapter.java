package br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.repository.TransactionItauRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.itau_challenge.TransactionItauEntity;
import br.com.daniel.java.quarkus.general.core.domain.itau_challenge.TransactionItau;
import br.com.daniel.java.quarkus.general.core.port.itau_challenge.TransactionItauPort;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class TransactionItauAdapter implements TransactionItauPort {

    @Inject
    TransactionItauRepository transactionItauRepository;

    @Override
    public List<TransactionItau> getAllTransactions() {
        log.info("Buscando todas as transações existentes.");

        return transactionItauRepository.listAll()
                .stream()
                .map(TransactionItau::new)
                .toList();
    }

    public List<TransactionItau> getAllPageable(int pages) {
        return transactionItauRepository.findAll()
                .page(Page.ofSize(pages).index(0))
                .list()
                .stream()
                .map(TransactionItau::new)
                .toList();
    }

    public List<TransactionItau> getAllOrderByAmount() {
        return transactionItauRepository.listAll(Sort.by("")
                        .and("", Sort.Direction.Descending))
                .stream()
                .map(TransactionItau::new)
                .toList();
    }

    @Transactional
    @Override
    public TransactionItau createTransaction(TransactionItau transactionItau) {
        log.info("Salvando nova transação no banco de dados. Payload: {}", transactionItau);
        var transactionItauEntity = getTransactionItauEntity(transactionItau);

        persistirTransactionEntity(transactionItauEntity);
        return new TransactionItau(transactionItauEntity);
    }

    @Transactional
    @Override
    public void createTransactionBy(TransactionItau transactionItau) {
        log.info("Salvando nova transação na base de dados via payload: {}", transactionItau);
        persistirTransactionEntity(getTransactionItauEntity(transactionItau));
    }

    @Override
    public List<TransactionItau> getTransactionsByDateTime(OffsetDateTime dateTimeRange) {
        return transactionItauRepository.list("createdAt > ?1", dateTimeRange.toLocalDateTime())
                .stream()
                .map(TransactionItau::new)
                .toList();
    }

    @Override
    public Optional<TransactionItau> getById(Long transactionId) {
        log.info("Buscar transação por ID: {}", transactionId);
        return transactionItauRepository.findByIdOptional(transactionId)
                .map(TransactionItau::new);
    }

    @Transactional
    @Override
    public void deleteById(Long transactionId) {
        log.info("Deletar uma única transação por ID: {}", transactionId);
        transactionItauRepository.deleteById(transactionId);
    }

    @Transactional
    @Override
    public void deleteAll() {
        log.info("Deletar todas as transações existentes.");
        transactionItauRepository.deleteAll();
    }

    private TransactionItauEntity getTransactionItauEntity(TransactionItau transactionItau) {
        var transactionItauEntity = new TransactionItauEntity(transactionItau);
        transactionItauEntity.setTransactionId(UUID.randomUUID().toString());
        return transactionItauEntity;
    }

    private void persistirTransactionEntity(TransactionItauEntity transactionItauEntity) {
        transactionItauRepository.persistAndFlush(transactionItauEntity);
    }
}
