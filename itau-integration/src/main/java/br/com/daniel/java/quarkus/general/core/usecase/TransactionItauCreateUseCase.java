package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.usecase.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.TransactionItauOutput;

/**
 * Interface que define o contrato para a criação de transações no sistema.
 * Fornece métodos para criar uma transação e validar a data de criação.
 */
public interface TransactionItauCreateUseCase {

    /**
     * Cria uma nova transação com base nos dados fornecidos.
     *
     * @param input Objeto contendo os dados da transação a ser criada.
     * @return Um objeto {@link TransactionItauOutput} contendo os detalhes da transação criada.
     */
    TransactionItauOutput createTransaction(TransactionItauInput input);

    /**
     * Cria uma nova transação com base nos dados fornecidos.
     *
     * @param input Objeto contendo os dados da transação a ser criada.
     * @return Um objeto {@link TransactionItauOutput} contendo os detalhes da transação criada.
     */
    TransactionItauOutput createNewTransaction(TransactionItauInput input);

    /**
     * Verifica se a data de criação da transação é inválida.
     *
     * @param input Objeto contendo os dados da transação a ser validada.
     * @return {@code true} se a data de criação for inválida, caso contrário {@code false}.
     */
    boolean isCreatedAtInvalid(TransactionItauInput input);
}