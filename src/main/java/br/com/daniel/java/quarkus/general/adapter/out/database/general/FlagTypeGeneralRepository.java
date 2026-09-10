package br.com.daniel.java.quarkus.general.adapter.out.database.general;

import br.com.daniel.java.quarkus.general.adapter.out.entities.general.FlagTypeGeneralEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlagTypeGeneralRepository implements PanacheRepository<FlagTypeGeneralEntity> {
}
