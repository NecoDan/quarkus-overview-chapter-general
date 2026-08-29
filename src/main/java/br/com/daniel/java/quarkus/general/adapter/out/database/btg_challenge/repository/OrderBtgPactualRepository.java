package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderBtgPactualRepository implements PanacheMongoRepositoryBase<OrderBtgPactualEntity, String> {

    @Override
    public void persist(OrderBtgPactualEntity entity) {
        if (entity.getOrderId() == null) {
            entity.setOrderId(UUID.randomUUID().toString());
        }

        PanacheMongoRepositoryBase.super.persist(entity);
    }

    @Override
    public void persist(Iterable<OrderBtgPactualEntity> entities) {
        for (OrderBtgPactualEntity entity : entities) {
            if (entity.getOrderId() == null) {
                entity.setOrderId(UUID.randomUUID().toString());
            }
        }
        PanacheMongoRepositoryBase.super.persist(entities);
    }

    public Optional<OrderBtgPactualEntity> findByIdCustomize(UUID orderId) {
        return find("where orderId = ?1", orderId).firstResultOptional();
    }

    public List<OrderBtgPactualEntity> findByTotalValueGreaterThan(BigDecimal totalValue) {
        return find("totalValue > ?1", Sort.by("data_criacao"), totalValue).list();
    }

    public List<OrderBtgPactualEntity> findByTotalValueLessThan(BigDecimal totalValue) {
        return find("totalValue < ?1", Sort.by("data_criacao"), totalValue).list();
    }
}
