package br.com.daniel.java.quarkus.general.utils.logs;

import org.slf4j.MDC;

import java.util.UUID;

import static org.slf4j.MDC.put;

public final class MdcUtils {

    private static final String TRANSACTION_ID = "transactionId";

    private MdcUtils() {
        throw new IllegalStateException("This is a utility class MdcUtils and cannot be instantiated");
    }

    public static void putTransactionId(String transactionId) {
        put(TRANSACTION_ID, transactionId);
    }

    public static void putTransactionIdRandom() {
        put(TRANSACTION_ID, UUID.randomUUID().toString());
    }

    public static void clear() {
        MDC.remove(TRANSACTION_ID);
    }

}
