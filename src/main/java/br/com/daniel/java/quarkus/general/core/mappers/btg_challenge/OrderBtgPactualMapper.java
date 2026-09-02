package br.com.daniel.java.quarkus.general.core.mappers.btg_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderBtgPactualEntity;
import br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge.OrderCustomerBtgPactualEntity;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderCustomerBtgPactual;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderCreatedEventBtgPactualInput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface OrderBtgPactualMapper {

    OrderBtgPactualEntity toEntity(OrderBtgPactual domain);

    OrderCustomerBtgPactualEntity toCustomerEntity(OrderCustomerBtgPactual domainCustomer);

    OrderBtgPactual toDomain(OrderBtgPactualEntity entity);

    OrderCustomerBtgPactual toCustomerDomain(OrderCustomerBtgPactualEntity customerEntity);

    OrderBtgPactualInput toOrderBtgPactualInput(OrderCreatedEventBtgPactualInput eventInput);
}
