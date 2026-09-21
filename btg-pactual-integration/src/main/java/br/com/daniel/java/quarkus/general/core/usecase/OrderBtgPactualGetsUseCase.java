package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.usecase.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalAmountValueBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.output.OrderTotalQuantityValuesBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.domain.PagedOutput;
import org.bson.types.ObjectId;

import java.util.UUID;

public interface OrderBtgPactualGetsUseCase {
    OrderBtgPactualOutput getById(ObjectId id);

    PagedOutput<OrderBtgPactualOutput> getAllPageable(int pageIndex, int pageSize, boolean expandItems);

    PagedOutput<OrderBtgPactualOutput> getAllOrdersPageableByCustomer(UUID customerId, int pageIndex, int pageSize, boolean expandItems);

    OrderTotalAmountValueBtgPactualOutput getTotalAmountBy(ObjectId id);

    OrderTotalQuantityValuesBtgPactualOutput getTotalQuantityOrdersBy(UUID customerId);

}
