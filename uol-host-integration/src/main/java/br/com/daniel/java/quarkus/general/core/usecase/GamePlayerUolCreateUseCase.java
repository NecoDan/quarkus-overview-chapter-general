package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.usecase.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerOutput;

public interface GamePlayerUolCreateUseCase {

    GamePlayerOutput createPlayer(GamePlayerInput input);
}
