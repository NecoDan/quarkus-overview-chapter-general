package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;


import br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.GamePlayerUolAdapter;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.port.GamePlayerUolFilePort;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerReportOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolNotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Singleton
@Slf4j
public class GamePlayerUolGetUseCaseImpl implements GamePlayerUolGetUseCase {

    @Inject
    GamePlayerUolAdapter gamePlayerUolAdapter;

    @Inject
    GamePlayerUolFilePort gamePlayerUolFilePort;

    @Override
    public List<GamePlayerReportOutput> getAll() {
        log.info("UOL_CHALLENGE - Inicializando a busca de todo(s) os jogador(es) registrado(s).");

        final var allList = gamePlayerUolAdapter.findAll();
        if (CollectionUtils.isEmpty(allList)) {
            log.warn("Não foram encontrado(s) jogadores cadastrados até o momento.");
            throw new GamePlayerUolNotFoundException("Não foram encontrado(s) jogadores cadastrados até o momento.");
        }

        return allList.stream()
                .map(GamePlayerReportOutput::from)
                .toList();
    }

    @Override
    public List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup) {
        log.info("UOL_CHALLENGE - Inicializando a busca dos codinomes salvo(s)/utilizado(s)");
        return gamePlayerUolAdapter.findListExistingCodenames(typeHeroGroup);
    }
}
