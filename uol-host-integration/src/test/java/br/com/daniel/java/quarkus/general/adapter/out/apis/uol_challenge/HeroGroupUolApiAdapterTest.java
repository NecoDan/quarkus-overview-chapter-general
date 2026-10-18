package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.apis.HeroGroupUolApiAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.apis.strategy.HeroGroupUolApiRestManualAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.dto.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.JusticeLeagueDcDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeroGroupUolApiAdapterTest {

    private final HeroGroupUolApiRestManualAdapter manualAdapter = mock(HeroGroupUolApiRestManualAdapter.class);
    private HeroGroupUolApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeroGroupUolApiAdapter(manualAdapter);
    }

    @Test
    void delegatesMarvelLookupToManualAdapter() {
        var expected = new AvengersMarvelOutputDTO(java.util.List.of());
        when(manualAdapter.getMarvelSuperHeroGroups()).thenReturn(expected);

        assertSame(expected, adapter.getMarvelSuperHeroGroups());
    }

    @Test
    void delegatesJusticeLeagueLookupToManualAdapter() {
        var expected = mock(JusticeLeagueDcDTO.class);
        when(manualAdapter.getDCSuperHeroGroups()).thenReturn(expected);

        assertSame(expected, adapter.getDCSuperHeroGroups());
    }
}
