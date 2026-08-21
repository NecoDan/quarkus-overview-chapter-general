package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;

import java.util.List;

public interface GamePlayerUolGetUseCase {

    List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup);
}
