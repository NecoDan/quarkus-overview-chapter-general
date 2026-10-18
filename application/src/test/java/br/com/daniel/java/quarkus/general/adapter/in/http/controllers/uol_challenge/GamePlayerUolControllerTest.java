package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.services.GamePlayerUolUseCaseAdapter;
import br.com.daniel.java.quarkus.general.core.usecase.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerReportOutput;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class GamePlayerUolControllerTest {

    private final GamePlayerUolUseCaseAdapter gamePlayerUolUseCaseAdapter = mock(GamePlayerUolUseCaseAdapter.class);

    private GamePlayerUolController controller;

    @BeforeEach
    void setUp() {
        controller = new GamePlayerUolController();
        controller.gamePlayerUolUseCaseAdapter = gamePlayerUolUseCaseAdapter;
    }

    @Test
    void createsPlayerWithCreatedStatus() {
        var input = new GamePlayerInput("Clark", "clark@example.com", "999999999", 2);
        var output = GamePlayerOutput.from("Superman", "Liga da Justiça");
        when(gamePlayerUolUseCaseAdapter.createPlayer(input)).thenReturn(output);

        var response = controller.create(input);

        Assertions.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertSame(output, response.getEntity());
        verify(gamePlayerUolUseCaseAdapter).createPlayer(input);
    }

    @Test
    void returnsAllPlayersWithOkStatus() {
        var players = List.of(new GamePlayerReportOutput(
                "Clark", "clark@example.com", "999999999", "Superman", "Liga da Justiça"));
        when(gamePlayerUolUseCaseAdapter.getAll()).thenReturn(players);

        var response = controller.getAll();

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertSame(players, response.getEntity());
        verify(gamePlayerUolUseCaseAdapter).getAll();
    }
}
