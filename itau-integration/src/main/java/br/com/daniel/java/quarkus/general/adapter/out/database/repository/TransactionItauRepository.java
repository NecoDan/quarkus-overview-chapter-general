package br.com.daniel.java.quarkus.general.adapter.out.database.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.TransactionItauEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionItauRepository implements PanacheRepository<TransactionItauEntity> {
}
