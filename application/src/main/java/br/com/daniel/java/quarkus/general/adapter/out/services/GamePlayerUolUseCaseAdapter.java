package br.com.daniel.java.quarkus.general.adapter.out.services;

import br.com.daniel.java.quarkus.general.core.usecase.GamePlayerUolCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.GamePlayerUolGetUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerReportOutput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class GamePlayerUolUseCaseAdapter {

    @Inject
    GamePlayerUolCreateUseCase gamePlayerUolCreateUseCase;

    @Inject
    GamePlayerUolGetUseCase gamePlayerUolGetUseCase;

    public GamePlayerOutput createPlayer(GamePlayerInput input) {
        log.info("UOL_CHALLENGE - Criando um novo jogador com os dados fornecidos no corpo da requisição.");
        return gamePlayerUolCreateUseCase.createPlayer(input);
    }

    public List<GamePlayerReportOutput> getAll() {
        log.info("UOL_CHALLENGE - Obtendo todos os jogadores cadastrados.");
        return gamePlayerUolGetUseCase.getAll();
    }
}
