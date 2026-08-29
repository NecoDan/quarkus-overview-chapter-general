package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.apis.uol_challenge.HeroGroupUolApiAdapter;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.HeroMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.port.uol_challenge.GamePlayerUolFilePort;
import br.com.daniel.java.quarkus.general.core.port.uol_challenge.GamePlayerUolPort;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolCreateFailedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
@Slf4j
public class GamePlayerUolCreateUseCaseImpl implements GamePlayerUolCreateUseCase {

    @Inject
    GamePlayerUolPort gamePlayerUolPort;

    @Inject
    GamePlayerUolFilePort gamePlayerUolFilePort;

    @Inject
    HeroGroupUolApiAdapter heroGroupUolApiAdapter;

    @Override
    public GamePlayerOutput createPlayer(GamePlayerInput input) {
        try {
            log.info("UOL_CHALLENGE - Inicializando processamento pra criação novo jogador. Payload: {}", input);
            var gamePlayerUol = new GamePlayerUol(input);
            loadValidateHeroGroupAndSetCodename(gamePlayerUol);

            validateNewPlayerCreation(input, gamePlayerUol);
            gamePlayerUolPort.salvarGamePlayer(gamePlayerUol);
            gamePlayerUolFilePort.salvarGamePlayer(gamePlayerUol);

            return GamePlayerOutput.from(
                    gamePlayerUol.getCodeName(),
                    gamePlayerUol.getGroupCode().getDescription()
            );
        } catch (Exception e) {
            log.error("UOL_CHALLENGE - Falha ao criar um novo jogador. Payload: {}. Erro: {}", input, e.getMessage());
            throw new GamePlayerUolCreateFailedException("Falha ao criar um novo jogador. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), e
            );
        }
    }

    private void validateNewPlayerCreation(GamePlayerInput input,
                                           GamePlayerUol gamePlayerUol) {

        if (gamePlayerUol.isJusticeLeague() && gamePlayerUol.isCodenameInValid()) {
            log.error("UOL_CHALLENGE - Falha ao criar um novo jogador. Payload: {}. Erro: Codinome não disponiveis para o grupo de herois: {}",
                    input, gamePlayerUol.getGroupCode());
            throw new GamePlayerUolCreateFailedException("Falha ao criar um novo jogador. Erro: " +
                    "Codinome não disponiveis para o grupo de herois: %s".formatted(gamePlayerUol.getGroupCode()));
        }
    }

    private void loadValidateHeroGroupAndSetCodename(GamePlayerUol gamePlayerUol) {
        if (gamePlayerUol.isAvengers()) {
            gamePlayerUol.setCodeName(getRandomCodenameValidFrom(
                            getCodenameListAvengers()
                    )
            );
        } else {
            gamePlayerUol.setCodeName(
                    extractCodenameThroughJusticeLeagueGroupList(gamePlayerUol)
            );
        }
    }

    private String extractCodenameThroughJusticeLeagueGroupList(GamePlayerUol gamePlayerUol) {
        final var existingCodenameList = gamePlayerUolPort.findListExistingCodenames(gamePlayerUol.getGroupCode());

        final var codenameListJusticeLeague = getCodenameListJusticeLeague();

        if (CollectionUtils.isEmpty(existingCodenameList)) {
            return getRandomCodenameValidFrom(codenameListJusticeLeague);
        }

        var existingSet = new HashSet<>(existingCodenameList);
        for (var codenameAvailable : codenameListJusticeLeague) {
            if (!existingSet.contains(codenameAvailable)) {
                return codenameAvailable;
            }
        }

        return StringUtils.EMPTY;
    }

    private static String getRandomCodenameValidFrom(List<String> codenameList) {
        return codenameList.stream()
                .skip(RandomUtils.secureStrong().randomLong(0, codenameList.size()))
                .findFirst()
                .orElse(StringUtils.EMPTY);
    }

    private List<String> getCodenameListJusticeLeague() {
        return heroGroupUolApiAdapter.getDCSuperHeroGroups()
                .getCodinomes();
    }

    private List<String> getCodenameListAvengers() {
        return heroGroupUolApiAdapter.getMarvelSuperHeroGroups()
                .vingadores()
                .stream()
                .map(HeroMarvelOutputDTO::codinome)
                .toList();
    }
}
