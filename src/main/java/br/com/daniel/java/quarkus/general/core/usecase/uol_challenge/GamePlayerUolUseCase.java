package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;

public interface GamePlayerUolUseCase {

    GamePlayerOutput createPlayer(GamePlayerInput input);
}
