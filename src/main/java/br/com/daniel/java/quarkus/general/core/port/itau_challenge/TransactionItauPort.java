package br.com.daniel.java.quarkus.general.core.port.itau_challenge;

import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface que define o contrato para operações em memória relacionadas a transações do Itaú.
 */
public interface TransactionItauPort {

    /**
     * Retorna todas as transações armazenadas em memória.
     *
     * @return Lista de todas as transações em memória.
     */
    List<TransactionItau> getAllTransactions();

    @Transactional
    void createTransactionBy(TransactionItau transactionItau);

    /**
     * Retorna as transações armazenadas em memória que correspondem ao intervalo de data e hora fornecido.
     *
     * @param dateTimeRange Intervalo de data e hora para filtrar as transações.
     * @return Lista de transações que correspondem ao intervalo fornecido.
     */
    List<TransactionItau> getTransactionsByDateTime(OffsetDateTime dateTimeRange);

    /**
     * Cria e armazena uma nova transação em memória.
     *
     * @param transactionItau Objeto da transação a ser criada.
     * @return A transação criada.
     */
    TransactionItau createTransaction(TransactionItau transactionItau);

    /**
     * Busca uma transação em memória pelo seu ID.
     *
     * @param transactionId ID da transação a ser buscada.
     * @return Um Optional contendo a transação, caso encontrada.
     */
    Optional<TransactionItau> getById(Long transactionId);

    /**
     * Remove uma transação em memória pelo seu ID.
     *
     * @param transactionId ID da transação a ser removida.
     */
    void deleteById(final Long transactionId);

    /**
     * Remove todas as transações armazenadas em memória.
     */
    void deleteAll();
}