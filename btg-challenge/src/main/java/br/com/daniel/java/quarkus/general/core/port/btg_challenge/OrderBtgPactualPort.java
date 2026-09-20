package br.com.daniel.java.quarkus.general.core.port.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.usecase.generics.PagedOutput;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderBtgPactualPort {

    Optional<OrderBtgPactual> getOrderByIdFrom(ObjectId id);

    Optional<OrderBtgPactual> getOrderById(ObjectId id);

    Optional<OrderBtgPactual> saveOrder(OrderBtgPactual orderBtgPactual);

    Optional<OrderBtgPactual> getOrderByOrderIdExternal(UUID orderId);

    List<OrderBtgPactual> getAll();

    PagedOutput<OrderBtgPactual> getAllOrdersPageableByCustomer(UUID customerId,
                                                                int pageIndex,
                                                                int pageSize,
                                                                boolean expandItems);

    List<OrderBtgPactual> getAllOrdersBy(UUID customerId);

    List<OrderBtgPactual> findPagedAndSorted(int pageIndex, int pageSize);

    PagedOutput<OrderBtgPactual> findPagedAndSortedBy(int pageIndex, int pageSize, boolean expandItems);
}
