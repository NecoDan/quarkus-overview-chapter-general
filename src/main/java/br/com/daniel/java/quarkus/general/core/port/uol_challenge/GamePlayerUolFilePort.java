package br.com.daniel.java.quarkus.general.core.port.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;

import java.util.List;
import java.util.Optional;

public interface GamePlayerUolFilePort {

    /**
     * Recupera a lista de codinomes que já estão salvos ou em uso para um determinado grupo de heróis.
     *
     * @param typeHeroGroup Grupo de heróis (ex: Vingadores, Liga da Justiça) utilizado como filtro na busca.
     * @return Uma {@link List} de {@link String} contendo os codinomes associados ao grupo informado.
     */
    List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup);

    /**
     * Recupera todos os jogadores salvos no repositório de arquivos.
     * <p>
     * Transforma as entidades persistidas em objetos do domínio {@link GamePlayerUol}.
     * </p>
     *
     * @return Uma {@link List} contendo todos os objetos {@link GamePlayerUol} cadastrados.
     */
    List<GamePlayerUol> findAll();

    Optional<GamePlayerUol> findById(Long id);

    /**
     * Salva um novo jogador no arquivo.
     * <p>
     * Converte o objeto de domínio {@link GamePlayerUol} para a entidade {@link GamePlayerUolEntity}
     * antes de realizar a persistência no repositório.
     * </p>
     *
     * @param gamePlayerUol Objeto do domínio contendo os dados do jogador a ser salvo.
     */
    void salvarGamePlayer(GamePlayerUol gamePlayerUol);
}
