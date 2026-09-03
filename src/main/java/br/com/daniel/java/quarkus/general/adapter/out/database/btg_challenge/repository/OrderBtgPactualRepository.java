package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderBtgPactualRepository implements PanacheMongoRepositoryBase<OrderBtgPactualEntity, ObjectId> {

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

    public Optional<OrderBtgPactualEntity> findByIdCustom(String id) {
        ObjectId objectId = new ObjectId(id);
        return find("_id = ?1", objectId).firstResultOptional();
    }

    public List<OrderBtgPactualEntity> findByCustomerId(UUID customerId) {
        return find("customer.customerId = ?1", customerId.toString()).list();
    }

    public Optional<OrderBtgPactualEntity> findByOrderId(UUID orderId) {
        return find("orderId = ?1", orderId.toString()).firstResultOptional();
    }

    public List<OrderBtgPactualEntity> findByTotalValueGreaterThan(BigDecimal totalValue) {
        return find("totalValue > ?1", Sort.by("createdAt"), totalValue).list();
    }

    public List<OrderBtgPactualEntity> findByTotalValueLessThan(BigDecimal totalValue) {
        return find("totalValue < ?1", Sort.by("createdAt"), totalValue).list();
    }
}
