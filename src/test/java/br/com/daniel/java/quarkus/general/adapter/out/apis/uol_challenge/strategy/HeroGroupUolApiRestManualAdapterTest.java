package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.client.HttpNativeClient;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HeroGroupUolApiRestManualAdapterTest {

    private final HttpNativeClient client = mock(HttpNativeClient.class);
    private HeroGroupUolApiRestManualAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeroGroupUolApiRestManualAdapter();
        adapter.httpNativeClient = client;
        adapter.url = "https://example.test";
    }

    @Test
    void requestsMarvelJsonFromExpectedUrl() {
        var expected = new AvengersMarvelOutputDTO(java.util.List.of());
        when(client.get(contains("vingadores.json"), eq(AvengersMarvelOutputDTO.class), anyMap())).thenReturn(expected);

        assertSame(expected, adapter.getMarvelSuperHeroGroups());
        verify(client).get("https://example.test/test-backEnd-Java/master/referencias/vingadores.json",
                AvengersMarvelOutputDTO.class, java.util.Collections.emptyMap());
    }

    @Test
    void wrapsNativeClientFailure() {
        when(client.get(anyString(), eq(AvengersMarvelOutputDTO.class), anyMap()))
                .thenThrow(new RuntimeException("network error"));

        var error = assertThrows(HttpRestClientException.class, () -> adapter.getMarvelSuperHeroGroups());

        assertEquals("network error", error.getCause().getMessage());
    }
}
