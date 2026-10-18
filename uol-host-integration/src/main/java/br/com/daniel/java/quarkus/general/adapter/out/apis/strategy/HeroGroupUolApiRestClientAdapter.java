package br.com.daniel.java.quarkus.general.adapter.out.apis.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.apis.HeroGroupUolApiPort;
import br.com.daniel.java.quarkus.general.adapter.out.client.register.HeroGroupUolClient;
import br.com.daniel.java.quarkus.general.adapter.out.dto.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.HttpRestClientException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class HeroGroupUolApiRestClientAdapter implements HeroGroupUolApiPort {

    private final HeroGroupUolClient heroGroupUolClient;

    @Inject
    public HeroGroupUolApiRestClientAdapter(@RestClient HeroGroupUolClient heroGroupUolClient) {
        this.heroGroupUolClient = heroGroupUolClient;
    }

    @Override
    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da Marvel Comics para associar o(s) codinome(s) via RestClient [External].");

        try {
            return heroGroupUolClient.getMarvelSuperHeroGroups();
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da Marvel (Vingadores) via RestClient [External]. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da Marvel  (Vingadores) via RestClient [External]:", e);
        }
    }

    @Override
    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da DC Comics para associar o(s) codinome(s) via RestClient [External].");

        try {
            return heroGroupUolClient.getDCSuperHeroGroups();
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via RestClient [External]. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via RestClient [External]:", e);
        }
    }
}
