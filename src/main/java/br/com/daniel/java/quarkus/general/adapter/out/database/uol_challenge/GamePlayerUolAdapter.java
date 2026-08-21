package br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.repository.GamePlayerUolRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.port.GamePlayerUolPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Slf4j
public class GamePlayerUolAdapter implements GamePlayerUolPort {

    @Inject
    GamePlayerUolRepository repository;

    @Override
    public List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup) {
        log.info("Recuperando a lista de codinomes salvo(s)/utilizado(s) ");

        return repository.findByGroupCode(typeHeroGroup)
                .stream()
                .filter(Objects::nonNull)
                .map(GamePlayerUolEntity::getCodeName)
                .toList();
    }

    @Transactional
    @Override
    public void salvarGamePlayer(GamePlayerUol gamePlayerUol) {
        log.info("Salvando novo jogador na base de dados. Dados: {}", gamePlayerUol);
        repository.persistAndFlush(new GamePlayerUolEntity(gamePlayerUol));
    }
}
