package br.com.daniel.java.quarkus.general.adapter.out.apis;

import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import br.com.daniel.java.quarkus.general.exceptions.api.HttpRestClientException;

public interface HeroGroupUolApiPort {

    /**
     * Busca os grupos de super-heróis da Marvel através do cliente externo.
     *
     * @return O DTO contendo a lista de grupos de heróis da Marvel.
     * @throws HttpRestClientException Se ocorrer um erro durante a comunicação com o serviço externo.
     */
    AvengersMarvelOutputDTO getMarvelSuperHeroGroups();

    /**
     * Busca os grupos de super-heróis da DC (Liga da Justiça) através do cliente externo.
     *
     * @return O DTO contendo a lista de grupos de super-heróis da DC.
     * @throws HttpRestClientException Caso ocorra uma falha na comunicação com o serviço externo.
     */
    JusticeLeagueDcDTO getDCSuperHeroGroups();

    /**
     * Busca os grupos de super-heróis da Marvel através do cliente externo.
     *
     * @return O DTO contendo a lista de grupos de heróis da Marvel.
     * @throws HttpRestClientException Se ocorrer um erro durante a comunicação com o serviço externo.
     */
    AvengersMarvelOutputDTO getMarvelSuperHeroGroupsByClientRest();

    /**
     * Busca os grupos de super-heróis da DC (Liga da Justiça) através do cliente externo.
     *
     * @return O DTO contendo a lista de grupos de super-heróis da DC.
     * @throws HttpRestClientException Caso ocorra uma falha na comunicação com o serviço externo.
     */
    JusticeLeagueDcDTO getDCSuperHeroGroupsByClientRest();
}
