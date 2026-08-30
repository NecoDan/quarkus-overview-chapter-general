package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository.OrderBtgPactualRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class OrderBtgPactualAdapter implements OrderBtgPactualPort {

    @Inject
    OrderBtgPactualRepository repositoryOrder;

    @Inject
    MongoClient mongoClient;

    @Override
    public Optional<OrderBtgPactual> getOrderById(UUID id) {
        log.info("Buscar pedido por ID: {}", id);
        return repositoryOrder.findByIdOptional(id.toString())
                .map(OrderBtgPactual::new);
    }

    @Override
    public Optional<OrderBtgPactual> saveOrder(OrderBtgPactual orderBtgPactual) {
        log.info("Salvando novo pedito no banco de dados. Payload: {}", orderBtgPactual);

        try {
            // 1. Abre a sessão do cliente MongoDB
            try (ClientSession session = mongoClient.startSession()) {
                // 2. Executa o bloco dentro de uma transação ACID
                session.withTransaction(() -> {
                            var orderEntity = new OrderBtgPactualEntity(orderBtgPactual);

                            repositoryOrder.persist(orderEntity); // Persiste dentro do mesmo contexto
                            return Optional.of(new OrderBtgPactual(orderEntity)); // Retorno do bloco com sucesso (Realiza COMMIT automático)
                        }
                );
                // Se qualquer exceção for lançada dentro do bloco withTransaction,
                // o MongoDB realiza ROLLBACK de todas as alterações automaticamente.
            }
        } catch (Exception e) {
            log.error("Failed create new order MongoDB: {}", e.getMessage());
            throw new OrderBtgPactualCreateFailedException(e.getMessage());
        }

        return Optional.empty();
    }
}
