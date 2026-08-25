package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.client.register.HeroGroupUolClient;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HeroGroupUolApiRestClientAdapterTest {

    private final HeroGroupUolClient client = mock(HeroGroupUolClient.class);
    private HeroGroupUolApiRestClientAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeroGroupUolApiRestClientAdapter();
        adapter.heroGroupUolClient = client;
    }

    @Test
    void returnsMarvelHeroesFromRestClient() {
        var expected = mock(AvengersMarvelOutputDTO.class);
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
