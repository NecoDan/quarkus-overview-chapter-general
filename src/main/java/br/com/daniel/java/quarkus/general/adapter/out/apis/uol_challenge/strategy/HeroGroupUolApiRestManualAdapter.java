package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.HeroGroupUolApiPort;
import br.com.daniel.java.quarkus.general.adapter.out.client.HttpNativeClient;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import br.com.daniel.java.quarkus.general.utils.FileUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.Collections;

@ApplicationScoped
@Slf4j
public class HeroGroupUolApiRestManualAdapter implements HeroGroupUolApiPort {

    private static final URI PATH = URI.create("/test-backEnd-Java/master/referencias");

    @Inject
    HttpNativeClient httpNativeClient;

    @ConfigProperty(name = "uol.herogroup.marvel-or-dc-comics.service.url")
    String url;

    @Override
    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da Marvel Comics para associar o(s) codinome(s) via Manual_HttpNativeClient [External].");

        try {
            final var completeUrl = URI.create(url + PATH + "/vingadores.json").toString();
            return httpNativeClient.get(completeUrl, AvengersMarvelOutputDTO.class, Collections.emptyMap());
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da Marvel (Vingadores) via" +
                    " Manual_HttpNativeClient [External]. Erro: {}", e.getMessage());

            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da Marvel " +
                    "(Vingadores) via Manual_HttpNativeClient [External]:", e);
        }
    }

    @Override
    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da DC Comics para associar o(s) codinome(s) via Manual_HttpNativeClient [External].");

        try {
            final var completeUrl = URI.create(url + PATH + "/liga_da_justica.xml").toString();
            final var contentFile = httpNativeClient.get(completeUrl, String.class, Collections.emptyMap());

            return FileUtils.toOjectFromFileContentXml(contentFile, JusticeLeagueDcDTO.class);
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via " +
                    "Manual_HttpNativeClient [External]. Erro: {}", e.getMessage());

            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) " +
                    "via Manual_HttpNativeClient [External]:", e);
        }
    }
}
