package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.GamePlayerUolAdapter;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GamePlayerUolGetUseCaseImplTest {

    @Mock
    GamePlayerUolAdapter gamePlayerUolAdapter;

    @InjectMocks
    GamePlayerUolGetUseCaseImpl useCase;

    @Test
    void returnsSavedPlayersAsReports() {
        var player = GamePlayerUol.builder()
                .name("Clark")
                .email("clark@example.com")
                .rawPhoneNumber("999999999")
                .codeName("Superman")
                .groupCode(TypeHeroGroup.DC_LIGA_JUSTICA)
                .build();

        when(gamePlayerUolAdapter.findAll()).thenReturn(List.of(player));

        var reports = useCase.getAll();

        assertEquals(1, reports.size());
        assertEquals("Superman", reports.getFirst().codename());
    }

    @Test
    void throwsWhenNoSavedPlayersExist() {
        when(gamePlayerUolAdapter.findAll()).thenReturn(List.of());

        assertThrows(GamePlayerUolNotFoundException.class, () -> useCase.getAll());
    }

    @Test
    void returnsSavedCodenamesForHeroGroup() {
        when(gamePlayerUolAdapter.findListExistingCodenames(TypeHeroGroup.DC_LIGA_JUSTICA))
                .thenReturn(List.of("Batman"));

        assertEquals(List.of("Batman"),
                useCase.getListCodeNameSavedBy(TypeHeroGroup.DC_LIGA_JUSTICA));
    }
}
