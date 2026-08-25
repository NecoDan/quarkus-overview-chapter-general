package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.itau_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.StatiticsTransactionItauUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.StatisticsItauOutput;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class StatiticsItauControllerTest {

    private final StatiticsTransactionItauUseCase useCase = mock(StatiticsTransactionItauUseCase.class);
    private StatiticsItauController controller;

    @BeforeEach
    void setUp() {
        controller = new StatiticsItauController();
        controller.statiticsTransactionItauUseCase = useCase;
    }

    @Test
    void returnsStatisticsForRequestedInterval() {
        var statistics = new StatisticsItauOutput(2L, 30.0, 15.0, 10.0, 20.0);
        when(useCase.calculateStatistics(60)).thenReturn(statistics);

        var response = controller.getStatisticsSummary(60);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertSame(statistics, response.getEntity());
        verify(useCase).calculateStatistics(60);
    }
}
