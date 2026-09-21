package br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GamePlayerUolRepository implements PanacheRepository<GamePlayerUolEntity> {

    public List<GamePlayerUolEntity> findByGroupCode(final TypeHeroGroup typeHeroGroup) {
        return list("groupCodeInt=?1", typeHeroGroup.getCode());
    }
}
