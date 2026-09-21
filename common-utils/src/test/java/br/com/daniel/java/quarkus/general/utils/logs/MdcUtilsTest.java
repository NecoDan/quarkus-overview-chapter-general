package br.com.daniel.java.quarkus.general.utils.logs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class MdcUtilsTest {

    @AfterEach
    void clearMdc() {
        MdcUtils.clear();
    }

    @Test
    void storesAndClearsTransactionId() {
        MdcUtils.putTransactionId("transaction-1");

        Assertions.assertEquals("transaction-1", MDC.get("transactionId"));

        MdcUtils.clear();

        Assertions.assertNull(MDC.get("transactionId"));
    }

    @Test
    void storesRandomTransactionId() {
        MdcUtils.putTransactionIdRandom();

        var value = MDC.get("transactionId");
        Assertions.assertNotNull(value);
        Assertions.assertDoesNotThrow(() -> java.util.UUID.fromString(value));
    }
}
