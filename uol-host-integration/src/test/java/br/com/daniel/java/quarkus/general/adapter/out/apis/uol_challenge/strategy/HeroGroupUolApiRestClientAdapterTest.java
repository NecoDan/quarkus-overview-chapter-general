package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.apis.strategy.HeroGroupUolApiRestClientAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.client.register.HeroGroupUolClient;
import br.com.daniel.java.quarkus.general.adapter.out.dto.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.exceptions.HttpRestClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeroGroupUolApiRestClientAdapterTest {

    private final HeroGroupUolClient client = mock(HeroGroupUolClient.class);
    private HeroGroupUolApiRestClientAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeroGroupUolApiRestClientAdapter();
    }

    @Test
    void returnsMarvelHeroesFromRestClient() {
        var expected = new AvengersMarvelOutputDTO(java.util.List.of());
        when(client.getMarvelSuperHeroGroups()).thenReturn(expected);

        assertSame(expected, adapter.getMarvelSuperHeroGroups());
    }

    @Test
    void wrapsRestClientFailure() {
        when(client.getDCSuperHeroGroups()).thenThrow(new RuntimeException("offline"));

        var error = assertThrows(HttpRestClientException.class, () -> adapter.getDCSuperHeroGroups());

        assertEquals("offline", error.getCause().getMessage());
    }
}
