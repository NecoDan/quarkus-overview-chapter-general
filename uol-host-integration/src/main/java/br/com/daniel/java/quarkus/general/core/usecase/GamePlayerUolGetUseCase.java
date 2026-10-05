package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.usecase.output.GamePlayerReportOutput;

import java.util.List;

public interface GamePlayerUolGetUseCase {

    List<GamePlayerReportOutput> getAll();

    List<String> getListCodeNameSavedBy(TypeHeroGroup typeHeroGroup);
}
