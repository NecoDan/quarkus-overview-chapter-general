package br.com.daniel.java.quarkus.general.adapter.out.apis;

import br.com.daniel.java.quarkus.general.adapter.out.client.HttpNativeClient;
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
public class HeroGroupUolApiAdapter implements HeroGroupUolApiPort {

    private static final String PATH = "/test-backEnd-Java/master/referencias";

    //    @Inject
    //    @RestClient
    //    HeroGroupUolClient heroGroupUolClient;

    @Inject
    HttpNativeClient httpNativeClient;

    @ConfigProperty(name = "uol.herogroup.marvel-or-dc-comics.service.url")
    String url;

    @Override
    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        try {

            final var completeUrl = URI.create(url.concat(PATH).concat("/vingadores.json"));
            return httpNativeClient.get(completeUrl.toURL().toString(), AvengersMarvelOutputDTO.class, Collections.emptyMap());
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da Marvel (Vingadores) via client externo. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da Marvel  (Vingadores) via client externo:", e);
        }
    }

    @Override
    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        try {

            final var completeUrl = URI.create(url.concat(PATH).concat("/liga_da_justica.xml"));
            return httpNativeClient.get(completeUrl.toURL().toString(), JusticeLeagueDcDTO.class, Collections.emptyMap());
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via client externo. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via client externo:", e);
        }
    }

    @Override
    public AvengersMarvelOutputDTO getMarvelSuperHeroGroupsByClientRest() {
        // return heroGroupUolClient.getMarvelSuperHeroGroups();
        return null;
    }

    @Override
    public JusticeLeagueDcDTO getDCSuperHeroGroupsByClientRest() {
        // return heroGroupUolClient.getDCSuperHeroGroups();
        return null;
    }
}
