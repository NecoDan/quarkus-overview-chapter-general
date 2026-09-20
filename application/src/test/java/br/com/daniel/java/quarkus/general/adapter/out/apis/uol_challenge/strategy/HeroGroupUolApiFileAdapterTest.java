package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HeroGroupUolApiFileAdapterTest {

    private final HeroGroupUolApiFileAdapter adapter = new HeroGroupUolApiFileAdapter();

    //TODO verficar esses teste num segundo momento

    //@Test
    void loadsMarvelHeroesFromBundledJson() {
        var result = adapter.getMarvelSuperHeroGroups();

        assertNotNull(result);
        assertFalse(result.vingadores().isEmpty());
    }

    //TODO verficar esses teste num segundo momento

    //@Test
    void loadsJusticeLeagueHeroesFromBundledXml() {
        var result = adapter.getDCSuperHeroGroups();

        assertNotNull(result);
        assertFalse(result.getCodinomes().isEmpty());
    }
}
