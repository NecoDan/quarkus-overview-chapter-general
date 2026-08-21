package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;


import br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.GamePlayerUolAdapter;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Singleton
@Slf4j
public class GamePlayerUolGetUseCaseImpl implements GamePlayerUolGetUseCase {

    @Inject
    GamePlayerUolAdapter gamePlayerUolAdapter;

    @Override
    public List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup) {
        return gamePlayerUolAdapter.findListExistingCodenames(typeHeroGroup);
    }
}
