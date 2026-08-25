package br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.repository.GamePlayerUolRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GamePlayerUolAdapterTest {

    private final GamePlayerUolRepository repository = mock(GamePlayerUolRepository.class);
    private GamePlayerUolAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GamePlayerUolAdapter();
        adapter.repository = repository;
    }

    @Test
    void returnsOnlyNonNullSavedCodenamesForGroup() {
        var first = GamePlayerUolEntity.builder().codeName("Batman").build();
        var second = GamePlayerUolEntity.builder().codeName("Superman").build();
        when(repository.findByGroupCode(TypeHeroGroup.DC_LIGA_JUSTICA))
                .thenReturn(List.of(first, null, second));

        var result = adapter.findListExistingCodenames(TypeHeroGroup.DC_LIGA_JUSTICA);

        assertEquals(List.of("Batman", "Superman"), result);
    }

    @Test
    void persistsMappedGamePlayer() {
        var player = GamePlayerUol.builder()
                .name("Clark")
                .email("clark@example.com")
                .rawPhoneNumber("999999999")
                .groupCode(TypeHeroGroup.DC_LIGA_JUSTICA)
                .build();

        adapter.salvarGamePlayer(player);

        verify(repository).persistAndFlush(any(GamePlayerUolEntity.class));
    }
}
