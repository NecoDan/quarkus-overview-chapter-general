package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.domain.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.mappers.OrderBtgPactualMapper;
import br.com.daniel.java.quarkus.general.core.mappers.OrderBtgPactualMapperImpl;
import br.com.daniel.java.quarkus.general.core.port.OrderBtgPactualPort;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.exceptions.api.OrderBtgPactualCreateFailedException;
import br.com.daniel.java.quarkus.general.util.factory.OrderBtgPactualFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderBtgPactualCreateUseCaseImplTest {

    @Mock
    private OrderBtgPactualPort orderBtgPactualPort;

    @Mock
    private OrderBtgPactualMapper orderBtgPactualMapper;

    @InjectMocks
    private OrderBtgPactualCreateUseCaseImpl useCase;

    private OrderBtgPactualMapperImpl orderBtgPactualMapperImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderBtgPactualMapperImpl = new OrderBtgPactualMapperImpl();
    }

    @Test
    void createOrderWithValidInputReturnsOutput() {
        // -- 01_Cenário
        var input = OrderBtgPactualFactory.buildMockOrderInputRecord(Boolean.TRUE);
        var orderBtgPactualSaved = OrderBtgPactualFactory.buildMockOrderWithItems();
        var savedOrderId = orderBtgPactualSaved.getOrderId();

        when(orderBtgPactualPort.saveOrder(any()))
                .thenReturn(Optional.of(orderBtgPactualSaved));

        // -- 02_Ação
        var result = useCase.createOrder(input);

        // -- 03_Verificação_Validação
        assertNotNull(result);
        assertTrue(StringUtils.containsAny(result.respostaSucesso(), savedOrderId));
        verify(orderBtgPactualPort).saveOrder(any(OrderBtgPactual.class));
    }

    @Test
    void createOrderWithNullInputThrowsIllegalArgumentException() {
        // -- 01_Cenário_&_02_Ação
        var illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> useCase.createOrder(null)
        );

        // -- 03_Verificação_Validação
        assertEquals("payload do pedido não pode ser nulo", illegalArgumentException.getMessage());
    }

    //    @Test
    void createOrderFromEventWithExistingOrderLogsWarning() {
        // -- 01_Cenário
        var input = OrderBtgPactualFactory.buildMockEventOrderInputRecord(Boolean.TRUE);
        var orderBtgPactualExisting = OrderBtgPactualFactory.buildMockOrderWithItems();
        var itemsOrderBtgPactualExisting = this.orderBtgPactualMapperImpl.toOrderItemBtgPactualList(input.items());

        when(orderBtgPactualPort.getOrderByOrderIdExternal(any(UUID.class)))
                .thenReturn(Optional.of(orderBtgPactualExisting));

        when(orderBtgPactualMapper.toOrderItemBtgPactualList(any()))
                .thenReturn(itemsOrderBtgPactualExisting);

        when(orderBtgPactualPort.saveOrder(any()))
                .thenReturn(Optional.of(orderBtgPactualExisting));

        // -- 02_Ação
        useCase.createOrderFrom(input);

        // -- 03_Verificação_Validação
        verify(orderBtgPactualPort)
                .getOrderByOrderIdExternal(any(UUID.class));

        verify(orderBtgPactualPort)
                .saveOrder(orderBtgPactualExisting);
    }

    @Test
    void createOrderFromEventWithNullInputThrowsIllegalArgumentException() {
        // -- 01_Cenário_&_02_Ação
        var illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> useCase.createOrderFrom(null)
        );

        // -- 03_Verificação_Validação
        assertEquals("payload do pedido não pode ser nulo", illegalArgumentException.getMessage());
    }

    //    @Test
    void createOrderFromEventWithNewOrderSavesOrder() {
        var orderCreatedEventBtgPactualInput = mock(OrderCreatedEventBtgPactualInput.class);
        var orderBtgPactualInput = mock(OrderBtgPactualInput.class);
        var newOrder = mock(OrderBtgPactual.class);

        when(orderBtgPactualPort.getOrderByOrderIdExternal(any(UUID.class)))
                .thenReturn(Optional.empty());

        when(orderBtgPactualMapper.toOrderBtgPactualInput(orderCreatedEventBtgPactualInput))
                .thenReturn(orderBtgPactualInput);

        useCase.createOrderFrom(orderCreatedEventBtgPactualInput);

        verify(orderBtgPactualPort)
                .saveOrder(any(OrderBtgPactual.class));
    }

    @Test
    void createOrderThrowsOrderBtgPactualCreateFailedExceptionOnUnexpectedError() {
        var orderBtgPactualInput = mock(OrderBtgPactualInput.class);

        when(orderBtgPactualPort.saveOrder(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        var orderBtgPactualCreateFailedException = assertThrows(OrderBtgPactualCreateFailedException.class,
                () -> useCase.createOrder(orderBtgPactualInput));

        assertTrue(orderBtgPactualCreateFailedException.getMessage().contains("Erro ao criar um novo Pedido"));
    }
}