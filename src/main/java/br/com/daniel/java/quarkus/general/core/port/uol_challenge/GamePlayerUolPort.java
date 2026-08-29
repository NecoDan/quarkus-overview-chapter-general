package br.com.daniel.java.quarkus.general.core.port.uol_challenge;

import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Port de persistência e consulta para os jogadores do desafio UOL.
 * <p>
 * Esta interface é responsável por intermediar a comunicação entre as regras de negócio
 * e a camada de dados (repositório), implementando as operações definidas na porta
 * {@link GamePlayerUolPort}.
 * </p>
 */
public interface GamePlayerUolPort {

    /**
     * Recupera a lista de codinomes que já foram salvos ou utilizados para um determinado grupo de heróis.
     *
     * @param typeHeroGroup O tipo do grupo de heróis (ex: Vingadores, Liga da Justiça) para filtragem.
     * @return Uma lista de {@link String} contendo os codinomes existentes e associados ao grupo informado.
     */
    List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup);

    /**
     * Busca todos os jogadores cadastrados na base de dados.
     *
     * @return Uma lista de objetos de domínio {@link GamePlayerUol} contendo todos os jogadores encontrados.
     */
    List<GamePlayerUol> findAll();

    /**
     * Persiste um novo jogador na base de dados e executa o *flush* imediatamente.
     * <p>
     * Este método é executado dentro de um contexto transacional.
     * </p>
     *
     * @param gamePlayerUol O objeto de domínio {@link GamePlayerUol} contendo os dados do jogador a ser salvo.
     */
    @Transactional
    void salvarGamePlayer(GamePlayerUol gamePlayerUol);
}
