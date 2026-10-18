package br.com.daniel.java.quarkus.general.adapter.out.apis;

import br.com.daniel.java.quarkus.general.adapter.out.dto.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.adapter.out.apis.strategy.HeroGroupUolApiRestManualAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class HeroGroupUolApiAdapter {

    private final HeroGroupUolApiRestManualAdapter heroGroupUolApiRestManualAdapter;

    @Inject
    public HeroGroupUolApiAdapter(HeroGroupUolApiRestManualAdapter heroGroupUolApiRestManualAdapter) {
        this.heroGroupUolApiRestManualAdapter = heroGroupUolApiRestManualAdapter;
    }

    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da Marvel Comics para associar o(s) codinome(s)");
        return heroGroupUolApiRestManualAdapter.getMarvelSuperHeroGroups();
    }

    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da DC Comics para associar o(s) codinome(s)");
        return heroGroupUolApiRestManualAdapter.getDCSuperHeroGroups();
    }
}
