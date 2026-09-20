package br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.itau_challenge.TransactionItauEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionItauRepository implements PanacheRepository<TransactionItauEntity> {
}
