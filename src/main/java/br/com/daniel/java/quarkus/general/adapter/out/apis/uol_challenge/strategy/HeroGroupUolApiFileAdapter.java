package br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.strategy;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.HeroGroupUolApiPort;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;
import br.com.daniel.java.quarkus.general.utils.FileUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@ApplicationScoped
@Slf4j
public class HeroGroupUolApiFileAdapter implements HeroGroupUolApiPort {

    private static final String PATH_JSON_HERO_GROUP_VINGADORES_MARVEL = "/dados-grupo-herois/personagens_marvel.json";
    private static final String PATH_JSON_HERO_GROUP_JUSTICE_LEAGUE_DCCOMICS = "/dados-grupo-herois/personagens_dc.xml";

    @Override
    public AvengersMarvelOutputDTO getMarvelSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da Marvel Comics para associar o(s) codinome(s) via arquivo.");

        try {
            final var fileContent = FileUtils.loadConfigFile(PATH_JSON_HERO_GROUP_VINGADORES_MARVEL);
            return FileUtils.toOjectFromFileContentJSON(fileContent, AvengersMarvelOutputDTO.class);
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da Marvel (Vingadores) via arquivo nos" +
                    " resources. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da Marvel (Vingadores) " +
                    "via arquivo nos resources:", e);
        }
    }

    @Override
    public JusticeLeagueDcDTO getDCSuperHeroGroups() {
        log.info("UOL_CHALLENGE - Obter lista de herois da DC Comics para associar o(s) codinome(s) via arquivo.");

        try {
            final var fileContent = FileUtils.loadConfigFile(PATH_JSON_HERO_GROUP_JUSTICE_LEAGUE_DCCOMICS);
            return FileUtils.toOjectFromFileContentXml(fileContent, JusticeLeagueDcDTO.class);
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) via arquivo nos " +
                    "resources. Erro: {}", e.getMessage());
            throw new HttpRestClientException("Erro ao tentar recuperar os grupos de super-heróis da DC (Liga da Justiça) " +
                    "via client externo:", e);
        }
    }
}
