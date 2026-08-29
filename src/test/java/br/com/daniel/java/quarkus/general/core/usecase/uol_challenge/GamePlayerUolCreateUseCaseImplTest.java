package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.HeroGroupUolApiAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.port.uol_challenge.GamePlayerUolFilePort;
import br.com.daniel.java.quarkus.general.core.port.uol_challenge.GamePlayerUolPort;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolCreateFailedException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamePlayerUolCreateUseCaseImplTest {

    @Mock
    GamePlayerUolPort gamePlayerUolPort;

    @Mock
    GamePlayerUolFilePort gamePlayerUolFilePort;

    @Mock
    HeroGroupUolApiAdapter heroGroupUolApiAdapter;

    @InjectMocks
    GamePlayerUolCreateUseCaseImpl useCase;

    @Test
    void assignsAnAvailableJusticeLeagueCodenameAndSavesPlayer() {
        when(heroGroupUolApiAdapter.getDCSuperHeroGroups())
                .thenReturn(new JusticeLeagueDcDTO(List.of("Batman", "Superman")));

        when(gamePlayerUolPort.findListExistingCodenames(TypeHeroGroup.DC_LIGA_JUSTICA))
                .thenReturn(List.of("Batman"));

        var output = useCase.createPlayer(
                new GamePlayerInput("Clark", "clark@example.com", "999999999", 2)
        );

        assertTrue(StringUtils.containsAny("Superman", output.respostaSucesso()));
        assertTrue(StringUtils.containsAny("Liga da Justiça", output.respostaSucesso()));

        var playerCaptor = ArgumentCaptor.forClass(GamePlayerUol.class);
        verify(gamePlayerUolPort).salvarGamePlayer(playerCaptor.capture());
        assertEquals("Superman", playerCaptor.getValue().getCodeName());
    }

    @Test
    void rejectsJusticeLeaguePlayerWhenEveryCodenameIsTaken() {
        var input = new GamePlayerInput("Bruce",
                "bruce@example.com",
                "999999999",
                2
        );

        when(heroGroupUolApiAdapter.getDCSuperHeroGroups())
                .thenReturn(new JusticeLeagueDcDTO(List.of("Batman")));

        when(gamePlayerUolPort.findListExistingCodenames(TypeHeroGroup.DC_LIGA_JUSTICA))
                .thenReturn(List.of("Batman"));

        final var exception = assertThrows(GamePlayerUolCreateFailedException.class,
                () -> useCase.createPlayer(input)
        );

        assertNotNull(exception);
        assertInstanceOf(GamePlayerUolCreateFailedException.class, exception);

        verify(gamePlayerUolPort, never()).salvarGamePlayer(any());
    }
}
