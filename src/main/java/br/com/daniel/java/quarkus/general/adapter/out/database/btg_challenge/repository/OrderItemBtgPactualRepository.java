//package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository;
//
//import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderItemBtgPactualEntity;
//import io.quarkus.mongodb.panache.PanacheMongoRepository;
//import jakarta.enterprise.context.ApplicationScoped;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@ApplicationScoped
//public class OrderItemBtgPactualRepository implements PanacheMongoRepository<OrderItemBtgPactualEntity> {
//
//    public Optional<OrderItemBtgPactualEntity> findByIdCustomize(UUID orderItemId) {s
//        return find("where orderItemId = ?1", orderItemId).firstResultOptional();
//    }
//
//    public Optional<OrderItemBtgPactualEntity> findByProductName(String productName) {
//        return find("lower(nome)", productName.toLowerCase()).firstResultOptional();
//    }
//
//    // Atualização customizada via consulta MongoDB
//    public void desactiveOrderItem(UUID orderItemId) {
//        update("ativo = false where orderItemId = ?1", orderItemId);
//    }
//}
