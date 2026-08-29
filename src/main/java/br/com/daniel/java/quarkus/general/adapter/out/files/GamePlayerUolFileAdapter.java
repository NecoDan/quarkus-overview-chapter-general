package br.com.daniel.java.quarkus.general.adapter.out.files;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.adapter.out.files.repository.GamePlayerUolFileRepository;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.core.port.uol_challenge.GamePlayerUolFilePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class GamePlayerUolFileAdapter implements GamePlayerUolFilePort {

    @Inject
    GamePlayerUolFileRepository repository;

    @Override
    public List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup) {
        log.info("UOL_CHALLENGE - Recuperando a lista de codinomes salvo(s)/utilizado(s) via arquivo.");
        return repository.findListExistingCodenames(typeHeroGroup);
    }

    @Override
    public List<GamePlayerUol> findAll() {
        log.info("UOL_CHALLENGE - Recuperando a lista de todos os jogadores salvo(s) via arquivo.");

        return repository.findAll()
                .stream()
                .map(GamePlayerUol::new)
                .toList();
    }

    @Override
    public Optional<GamePlayerUol> findById(Long id) {
        log.info("UOL_CHALLENGE - Recuperar o jogador por ID: {}, via arquivo.", id);
        return repository.findById(id).map(entity -> new GamePlayerUol());
    }

    @Override
    public void salvarGamePlayer(GamePlayerUol gamePlayerUol) {
        log.info("UOL_CHALLENGE - Salvando novo jogador via arquivo. Dados: {}", gamePlayerUol);
        repository.save(new GamePlayerUolEntity(gamePlayerUol));
    }
}
