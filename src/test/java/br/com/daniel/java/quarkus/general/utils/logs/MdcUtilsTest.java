package br.com.daniel.java.quarkus.general.utils.logs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;

class MdcUtilsTest {

    @AfterEach
    void clearMdc() {
        MdcUtils.clear();
    }

    @Test
    void storesAndClearsTransactionId() {
        MdcUtils.putTransactionId("transaction-1");

        assertEquals("transaction-1", MDC.get("transactionId"));

        MdcUtils.clear();

        assertNull(MDC.get("transactionId"));
    }

    @Test
    void storesRandomTransactionId() {
        MdcUtils.putTransactionIdRandom();

        var value = MDC.get("transactionId");
        assertNotNull(value);
        assertDoesNotThrow(() -> java.util.UUID.fromString(value));
    }
}
