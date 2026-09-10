package br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.btg_challenge.repository.OrderBtgPactualRepository;
import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.mappers.btg_challenge.OrderBtgPactualMapper;
import br.com.daniel.java.quarkus.general.core.mappers.btg_challenge.OrderBtgPactualStaticMapper;
import br.com.daniel.java.quarkus.general.core.port.btg_challenge.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.generics.PagedOutput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
@Slf4j
public class OrderBtgPactualAdapter implements OrderBtgPactualPort {

    @Inject
    OrderBtgPactualRepository repositoryOrder;

    @Inject
    OrderBtgPactualMapper orderBtgPactualMapper;

    @Inject
    MongoClient mongoClient;

    @Override
    public List<OrderBtgPactual> getAll() {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar todo(s) pedidos(s) salvos");

        return repositoryOrder.findAll()
                .stream()
                .map(entity -> orderBtgPactualMapper.toDomain(entity))
                .toList();
    }

    @Override
    public List<OrderBtgPactual> findPagedAndSorted(int pageIndex, int pageSize) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar todo(s) pedidos(s) salvo(s) paginado(s) por indicePagina: {} | tamanhoPagina: {}", pageIndex, pageSize);

        return repositoryOrder.findAll(Sort.by("createdAt").descending())
                .page(Page.of(pageIndex, pageSize))
                .list()
                .stream()
                .map(entity -> orderBtgPactualMapper.toDomain(entity))
                .toList();
    }

    @Override
    public PagedOutput<OrderBtgPactual> findPagedAndSortedBy(int pageIndex,
                                                             int pageSize,
                                                             boolean expandItems) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar todo(s) pedidos(s) salvo(s) paginado(s) " +
                "por indicePagina: {} | tamanhoPagina: {} | expandirItems: {}", pageIndex, pageSize, expandItems);

        // 1. Create query and set page state
        PanacheQuery<OrderBtgPactualEntity> query = repositoryOrder.findAll();
        return getOrderBtgPactualPagedOutput(pageIndex, pageSize, expandItems, query);
    }

    @Override
    public PagedOutput<OrderBtgPactual> getAllOrdersPageableByCustomer(UUID customerId,
                                                                       int pageIndex,
                                                                       int pageSize,
                                                                       boolean expandItems) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar todo(s) pedidos(s) salvo(s) paginado(s) " +
                "por indicePagina: {} | tamanhoPagina: {} | expandirItems: {}", pageIndex, pageSize, expandItems);

        // 1. Create query and set page state
        PanacheQuery<OrderBtgPactualEntity> query = repositoryOrder.findFromCustomerId(customerId);
        return getOrderBtgPactualPagedOutput(pageIndex, pageSize, expandItems, query);

    }

    private PagedOutput<OrderBtgPactual> getOrderBtgPactualPagedOutput(int pageIndex,
                                                                       int pageSize,
                                                                       boolean expandItems,
                                                                       PanacheQuery<OrderBtgPactualEntity> query) {
        query.page(Page.of(pageIndex, pageSize));

        var list = query.list()
                .stream()
                .map(entity -> OrderBtgPactualStaticMapper.buildOrderBtgPactual(entity, expandItems))
                .toList();

        return new PagedOutput<>(
                list,
                query.page().index,
                query.page().size,
                query.count(),
                query.pageCount(),
                query.hasNextPage(),
                query.hasPreviousPage()
        );
    }

    @Override
    public List<OrderBtgPactual> getAllOrdersBy(UUID costumerId) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar todo(s) pedidos(s) salvos");

        return repositoryOrder.findByCustomerId(costumerId)
                .stream()
                .map(entity -> orderBtgPactualMapper.toDomain(entity))
                .toList();
    }

    @Override
    public Optional<OrderBtgPactual> getOrderByIdFrom(ObjectId id) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar pedido por ID: {}", id);

        var orderBtgPactualEntity = repositoryOrder.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.id.equals(id))
                .findFirst();

        return orderBtgPactualEntity.map(entity -> orderBtgPactualMapper.toDomain(entity));
    }

    @Override
    public Optional<OrderBtgPactual> getOrderById(ObjectId id) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar pedido por ID: {}", id);

        return repositoryOrder.findByIdOptional(id)
                .map(entity -> orderBtgPactualMapper.toDomain(entity));
    }

    @Override
    public Optional<OrderBtgPactual> saveOrder(OrderBtgPactual orderBtgPactual) {
        log.info("BTG_PACTUAL_CHALLENGE - Salvando novo pedito no banco de dados. Payload: {}", orderBtgPactual);

        var orderBtgPactualEntity = saveOrderFinally(orderBtgPactual);
        return Optional.of(OrderBtgPactualStaticMapper.buildOrderBtgPactual(orderBtgPactualEntity, Boolean.FALSE));
    }

    @Override
    public Optional<OrderBtgPactual> getOrderByOrderIdExternal(UUID orderId) {
        log.info("BTG_PACTUAL_CHALLENGE - Buscar pedido por idPedidoExterno: {}", orderId);

        return repositoryOrder.findByOrderId(orderId)
                .map(entity -> orderBtgPactualMapper.toDomain(entity));
    }

    private OrderBtgPactualEntity saveOrderFinally(OrderBtgPactual orderBtgPactual) {
        AtomicReference<OrderBtgPactualEntity> orderEntity = new AtomicReference<>();

        try {
            // 1. Abre a sessão do cliente MongoDB
            try (ClientSession session = mongoClient.startSession()) {
                // 2. Executa o bloco dentro de uma transação ACID
                session.withTransaction(() -> {
                    orderEntity.set(orderBtgPactualMapper.toEntity(orderBtgPactual));
                    repositoryOrder.persistOrUpdate(orderEntity.get()); // Persiste dentro do mesmo contexto

                    session.commitTransaction();
                    return orderEntity; // Retorno do bloco com sucesso (Realiza COMMIT automático)
                });
                // Se qualquer exceção for lançada dentro do bloco withTransaction,
                // o MongoDB realiza ROLLBACK de todas as alterações automaticamente.
            }
            return orderEntity.get();
        } catch (Exception e) {
            log.error("BTG_PACTUAL_CHALLENGE - Failed create new order MongoDB: {}", e.getMessage());
            throw new OrderBtgPactualCreateFailedException(e.getMessage());
        }
    }
}
