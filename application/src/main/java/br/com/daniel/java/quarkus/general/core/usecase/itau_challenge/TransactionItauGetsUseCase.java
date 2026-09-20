package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;


import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.TransactionItauOutput;

import java.util.List;

/**
 * Interface que define os casos de uso relacionados a transações do Itaú.
 * Fornece métodos para obter todas as transações, buscar por ID e filtrar por intervalo de tempo.
 */
public interface TransactionItauGetsUseCase {

    /**
     * Obtém todas as transações disponíveis.
     *
     * @return Uma lista de objetos {@link TransactionItauOutput} representando todas as transações.
     */
    List<TransactionItauOutput> getAll();

    /**
     * Obtém uma transação específica com base no ID fornecido.
     *
     * @param transactionId O ID da transação a ser buscada.
     * @return Um objeto {@link TransactionItauOutput} representando a transação correspondente ao ID.
     */
    TransactionItauOutput getById(String transactionId);

    /**
     * Obtém todas as transações dentro de um intervalo de tempo especificado.
     *
     * @param secondsRange O intervalo de tempo em segundos para filtrar as transações.
     * @return Uma lista de objetos {@link TransactionItauOutput} representando as transações no intervalo especificado.
     */
    List<TransactionItauOutput> getAllTransactionsByRange(Integer secondsRange);
}