package br.com.daniel.java.quarkus.general.core.port;

import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import jakarta.transaction.Transactional;

public interface GamePlayerUolPort {
    @Transactional
    void salvarGamePlayer(GamePlayerUol gamePlayerUol);
}
