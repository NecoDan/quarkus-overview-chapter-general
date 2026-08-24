package br.com.daniel.java.quarkus.general.adapter.out.database.general;

import br.com.daniel.java.quarkus.general.adapter.out.entities.general.FlagTypeGeneralEntity;
import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@ApplicationScoped
public class FlagTypeGeneralRepository implements PanacheRepository<FlagTypeGeneralEntity> {
}
