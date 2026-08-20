package br.com.daniel.java.quarkus.general.core.usecase;

/**
 * Interface que define os casos de uso relacionados à remoção de transações do Itaú.
 * Fornece métodos para excluir uma transação específica ou todas as transações.
 */
public interface TransactionItauRemoveUseCase {

    /**
     * Exclui uma transação específica com base no ID fornecido.
     *
     * @param transactionId O ID da transação a ser excluída.
     */
    void deleteById(String transactionId);

    /**
     * Exclui todas as transações disponíveis.
     */
    void deleteAll();
}