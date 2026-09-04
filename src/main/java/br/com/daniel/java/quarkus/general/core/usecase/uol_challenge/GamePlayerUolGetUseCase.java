package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerReportOutput;

import java.util.List;

public interface GamePlayerUolGetUseCase {

    List<GamePlayerReportOutput> getAll();

    List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup);
}
