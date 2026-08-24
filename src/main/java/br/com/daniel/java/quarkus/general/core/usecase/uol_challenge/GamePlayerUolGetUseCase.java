package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge;

import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerReportOutput;

import java.util.List;

public interface GamePlayerUolGetUseCase {

    List<GamePlayerReportOutput> getAll();

    List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup);
}
