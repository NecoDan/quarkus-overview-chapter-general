package br.com.daniel.java.quarkus.general.core.usecase.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderTotalAmountValueBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderTotalQuantityValuesBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.generics.PagedOutput;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public interface OrderBtgPactualGetsUseCase {
    OrderBtgPactualOutput getById(ObjectId id);

    PagedOutput<OrderBtgPactualOutput> getAllPageable(int pageIndex, int pageSize, boolean expandItems);

    List<OrderBtgPactualOutput> getAllOrdersBy(UUID customerId);

    OrderTotalAmountValueBtgPactualOutput getTotalAmountBy(ObjectId id);

    OrderTotalQuantityValuesBtgPactualOutput getTotalQuantityOrdersBy(UUID customerId);

}
