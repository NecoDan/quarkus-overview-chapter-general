package br.com.daniel.java.quarkus.general.utils.logs;

import org.slf4j.MDC;

import java.util.UUID;

import static org.slf4j.MDC.put;

/**
 * Classe utilitária para gerenciar valores do MDC (Mapped Diagnostic Context), especificamente
 * para lidar com IDs de transação. Esta classe fornece métodos para adicionar, gerar e limpar
 * IDs de transação no contexto do MDC.
 */
public final class MdcUtils {

    // Chave usada para armazenar o ID de transação no contexto do MDC
    private static final String TRANSACTION_ID = "transactionId";

    /**
     * Construtor privado para evitar a instanciação desta classe utilitária.
     * Lança uma IllegalStateException se a instanciação for tentada.
     */
    private MdcUtils() {
        throw new IllegalStateException("Esta é uma classe utilitária MdcUtils e não pode ser instanciada");
    }

    /**
     * Adiciona um ID de transação fornecido ao contexto do MDC.
     *
     * @param transactionId o ID de transação a ser adicionado ao contexto do MDC
     */
    public static void putTransactionId(String transactionId) {
        put(TRANSACTION_ID, transactionId);
    }

    /**
     * Gera um UUID aleatório como ID de transação e o adiciona ao contexto do MDC.
     */
    public static void putTransactionIdRandom() {
        put(TRANSACTION_ID, UUID.randomUUID().toString());
    }

    /**
     * Remove o ID de transação do contexto do MDC.
     */
    public static void clear() {
        MDC.remove(TRANSACTION_ID);
    }
}