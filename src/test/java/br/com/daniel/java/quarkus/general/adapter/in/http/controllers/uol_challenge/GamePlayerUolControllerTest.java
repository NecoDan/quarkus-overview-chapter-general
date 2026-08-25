package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.uol_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.GamePlayerUolCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.GamePlayerUolGetUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerReportOutput;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class GamePlayerUolControllerTest {

    private final GamePlayerUolCreateUseCase createUseCase = mock(GamePlayerUolCreateUseCase.class);
    private final GamePlayerUolGetUseCase getUseCase = mock(GamePlayerUolGetUseCase.class);
    private GamePlayerUolController controller;

    @BeforeEach
    void setUp() {
        controller = new GamePlayerUolController();
        controller.gamePlayerUolCreateUseCase = createUseCase;
        controller.gamePlayerUolGetUseCase = getUseCase;
    }

    @Test
    void createsPlayerWithCreatedStatus() {
        var input = new GamePlayerInput("Clark", "clark@example.com", "999999999", 2);
        var output = new GamePlayerOutput("Superman", "Liga da Justiça");
        when(createUseCase.createPlayer(input)).thenReturn(output);

        var response = controller.create(input);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertSame(output, response.getEntity());
        verify(createUseCase).createPlayer(input);
    }

    @Test
    void returnsAllPlayersWithOkStatus() {
        var players = List.of(new GamePlayerReportOutput(
                "Clark", "clark@example.com", "999999999", "Superman", "Liga da Justiça"));
        when(getUseCase.getAll()).thenReturn(players);

        var response = controller.getAll();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertSame(players, response.getEntity());
        verify(getUseCase).getAll();
    }
}
