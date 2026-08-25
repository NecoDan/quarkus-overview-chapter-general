package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy.HeroGroupUolApiRestManualAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class HeroGroupUolApiAdapterTest {

    private final HeroGroupUolApiRestManualAdapter manualAdapter = mock(HeroGroupUolApiRestManualAdapter.class);
    private HeroGroupUolApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new HeroGroupUolApiAdapter();
        adapter.heroGroupUolApiRestManualAdapter = manualAdapter;
    }

    @Test
    void delegatesMarvelLookupToManualAdapter() {
        var expected = mock(AvengersMarvelOutputDTO.class);
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
