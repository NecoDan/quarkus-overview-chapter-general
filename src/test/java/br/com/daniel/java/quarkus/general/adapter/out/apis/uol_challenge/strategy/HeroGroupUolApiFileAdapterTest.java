package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroGroupUolApiFileAdapterTest {

    private final HeroGroupUolApiFileAdapter adapter = new HeroGroupUolApiFileAdapter();

    @Test
    void loadsMarvelHeroesFromBundledJson() {
        var result = adapter.getMarvelSuperHeroGroups();

        assertNotNull(result);
        assertFalse(result.vingadores().isEmpty());
    }

    @Test
    void loadsJusticeLeagueHeroesFromBundledXml() {
        var result = adapter.getDCSuperHeroGroups();

        assertNotNull(result);
        assertFalse(result.getCodinomes().isEmpty());
    }
}
