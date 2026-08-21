package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.port.GamePlayerUolPort;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.exceptions.EntityCreateFailedException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class GamePlayerUolUseCaseImpl implements GamePlayerUolUseCase {

    @Inject
    GamePlayerUolPort gamePlayerUolPort;

    @Override
    public GamePlayerOutput createPlayer(GamePlayerInput input) {
        log.info("Inicializando processamento pra criação novo jogador. Payload: {}", input);

        try {
            var gamePlayerUol = new GamePlayerUol(input);
            gamePlayerUolPort.salvarGamePlayer(gamePlayerUol);

            return GamePlayerOutput.from(gamePlayerUol.getCodeName(), gamePlayerUol.getGroupCode().getNome());
        } catch (Exception e) {
            log.error("Falha ao criar um novo jogador. Payload: {}. Erro: {}", input, e.getMessage());
            throw new EntityCreateFailedException("Falha ao criar um novo jogador. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), e
            );
        }
    }
}
