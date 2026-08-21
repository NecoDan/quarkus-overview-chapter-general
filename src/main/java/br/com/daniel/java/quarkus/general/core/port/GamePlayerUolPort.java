package br.com.daniel.java.quarkus.general.core.port;

import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import jakarta.transaction.Transactional;

import java.util.List;

public interface GamePlayerUolPort {

    List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup);

    @Transactional
    void salvarGamePlayer(GamePlayerUol gamePlayerUol);
}
