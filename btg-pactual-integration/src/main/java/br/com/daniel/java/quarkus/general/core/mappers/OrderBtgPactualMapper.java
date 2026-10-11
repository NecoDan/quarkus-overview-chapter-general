package br.com.daniel.java.quarkus.general.core.mappers;

import br.com.daniel.java.quarkus.general.adapter.out.entities.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.adapter.out.entities.OrderCustomerBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderItemCreatedEventBtgPactualInput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface OrderBtgPactualMapper {

    OrderBtgPactualEntity toEntity(OrderBtgPactual domain);

    OrderCustomerBtgPactualEntity toCustomerEntity(OrderCustomerBtgPactual domainCustomer);

    OrderBtgPactual toDomain(OrderBtgPactualEntity entity);

    OrderCustomerBtgPactual toCustomerDomain(OrderCustomerBtgPactualEntity customerEntity);

    OrderBtgPactualInput toOrderBtgPactualInput(OrderCreatedEventBtgPactualInput eventInput);

    List<OrderItemBtgPactualInput> toOrderItemBtgPactualList(List<OrderItemCreatedEventBtgPactualInput> items);
}
