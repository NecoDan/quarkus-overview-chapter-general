package br.com.daniel.java.quarkus.general.adapter.out.database.uol_challenge.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GamePlayerUolRepository implements PanacheRepository<GamePlayerUolEntity> {
}
