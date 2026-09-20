package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy.HeroGroupUolApiFileAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy.HeroGroupUolApiRestClientAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy.HeroGroupUolApiRestManualAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.Collections;

@ApplicationScoped
@Slf4j
public class HeroGroupUolApiAdapter {

    @ConfigProperty(name = "apiquarkusgeneral.application.flag-type-service-consumer")
    Integer flagTypeServiceConsumer;

    @Inject
    HeroGroupUolApiFileAdapter heroGroupUolApiFileAdapter;

    @Inject
    HeroGroupUolApiRestManualAdapter heroGroupUolApiRestManualAdapter;

    @Inject
    HeroGroupUolApiRestClientAdapter heroGroupUolApiRestClientAdapter;

    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da Marvel Comics para associar o(s) codinome(s)");
        return heroGroupUolApiRestManualAdapter.getMarvelSuperHeroGroups();
    }

    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da DC Comics para associar o(s) codinome(s)");
        return heroGroupUolApiRestManualAdapter.getDCSuperHeroGroups();
    }
}
