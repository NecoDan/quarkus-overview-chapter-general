package br.com.daniel.java.quarkus.general.core.domain;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemBtgPactualInput;
import br.com.daniel.java.quarkus.general.util.factory.OrderBtgPactualFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderBtgPactualTest {

    private OrderBtgPactual orderBtgPactualVar1;

    private OrderBtgPactual orderBtgPactualVar2;

    @BeforeEach
    void setUp() {
        orderBtgPactualVar1 = new OrderBtgPactual();

        orderBtgPactualVar2 = new OrderBtgPactual();
        orderBtgPactualVar2.setItems(new ArrayList<>());
    }

    @Nested
    @DisplayName("Testes de datas (defineDates)")
    class DefineDatesTests {

        @Test
        @DisplayName("Deve definir createdAt e updateAt com a data/hora atual em UTC")
        void shouldSetDatesInUtc() {
            // -- 01_Cenário
            var beforeExecution = LocalDateTime.now();

            // -- 02_Ação
            orderBtgPactualVar1.defineDates();

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactualVar1.getCreatedAt());
            assertNotNull(orderBtgPactualVar1.getUpdateAt());

            // Verifica se a data gerada é próxima/posterior ao momento do teste
            assertTrue(orderBtgPactualVar1.getCreatedAt().isAfter(beforeExecution)
                    || orderBtgPactualVar1.getCreatedAt().isEqual(beforeExecution)
            );

            assertTrue(orderBtgPactualVar1.getUpdateAt().isAfter(beforeExecution)
                    || orderBtgPactualVar1.getUpdateAt().isEqual(beforeExecution)
            );
        }
    }

    @Nested
    @DisplayName("Testes de adição de itens (addOrderItem / addAllOrderItem)")
    class AddItemsTests {

        @Test
        @DisplayName("Deve inicializar a lista e adicionar um único item quando a lista for nula")
        void shouldInitializeListAndAddSingleItem() {
            // -- 01_Cenário
            var orderItem = new OrderItemBtgPactual();

            // -- 02_Ação
            orderBtgPactualVar1.addOrderItem(orderItem);

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactualVar1.getItems());
            assertEquals(1, orderBtgPactualVar1.getItems().size());
            assertTrue(orderBtgPactualVar1.getItems().contains(orderItem));
        }

        @Test
        @DisplayName("Deve adicionar um item mantendo os itens já existentes na lista")
        void shouldAddSingleItemToExistingList() {
            // -- 01_Cenário
            orderBtgPactualVar1.setItems(new ArrayList<>());
            var orderItemVar1 = new OrderItemBtgPactual();
            var orderItemVar2 = new OrderItemBtgPactual();

            // -- 02_Ação
            orderBtgPactualVar1.addOrderItem(orderItemVar1);
            orderBtgPactualVar1.addOrderItem(orderItemVar2);

            // -- 03_Verificação_Validação
            assertEquals(2, orderBtgPactualVar1.getItems().size());
        }

        @Test
        @DisplayName("Deve inicializar a lista e adicionar uma coleção de itens quando a lista for nula")
        void shouldInitializeListAndAddCollection() {
            // -- 01_Cenário
            var orderItemList = List.of(new OrderItemBtgPactual(),
                    new OrderItemBtgPactual()
            );

            // -- 02_Ação
            orderBtgPactualVar1.addAllOrderItem(orderItemList);

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactualVar1.getItems());
            assertEquals(2, orderBtgPactualVar1.getItems().size());
        }
    }

    @Nested
    @DisplayName("Testes de cálculo do valor total (calculateTotalValue)")
    class CalculateTotalValueTests {
        @Test
        @DisplayName("Deve definir valor total como ZERO se a lista de itens estiver nula")
        void shouldSetZeroWhenItemsIsNull() {
            // -- 01_Cenário
            var orderVar12 = new OrderBtgPactual();
            orderVar12.setItems(null);

            // -- 02_Ação
            orderVar12.calculateTotalValue();

            // -- 03_Verificação_Validação
            assertEquals(BigDecimal.ZERO, orderVar12.getTotalValue());
        }

        @Test
        @DisplayName("Deve definir valor total como ZERO se a lista de itens estiver vazia")
        void shouldSetZeroWhenItemsIsEmpty() {
            // -- 01_Cenário
            orderBtgPactualVar1.setItems(new ArrayList<>());

            // -- 02_Ação
            orderBtgPactualVar1.calculateTotalValue();

            // -- 03_Verificação_Validação
            assertEquals(BigDecimal.ZERO, orderBtgPactualVar1.getTotalValue());
        }

        @Test
        @DisplayName("Deve somar corretamente o valor de todos os itens da lista")
        void shouldCalculateTotalValueCorrectly() {
            // Supondo que OrderItem possua construtor ou setters apropriados
            // Para estes testes, usamos objetos stub que retornam um valor em calculateItemValue()
            var orderItemVar1 = new StubOrderItem(new BigDecimal("10.50"));
            var orderItemVar2 = new StubOrderItem(new BigDecimal("20.25"));
            var item3 = new StubOrderItem(new BigDecimal("5.25"));

            orderBtgPactualVar1.setItems(List.of(orderItemVar1, orderItemVar2, item3));

            orderBtgPactualVar1.calculateTotalValue();

            assertEquals(new BigDecimal("36.00"), orderBtgPactualVar1.getTotalValue());
        }

        @Test
        @DisplayName("Deve ignorar elementos nulos dentro da lista de itens ao calcular o total")
        void shouldIgnoreNullElementsInItemsList() {
            // -- 01_Cenário
            var orderItemVar1 = new StubOrderItem(new BigDecimal("15.00"));

            List<OrderItemBtgPactual> itemsWithNull = new ArrayList<>();
            itemsWithNull.add(orderItemVar1);
            itemsWithNull.add(null);

            // -- 02_Ação
            orderBtgPactualVar1.setItems(itemsWithNull);
            orderBtgPactualVar1.calculateTotalValue();

            // -- 03_Verificação_Validação
            assertEquals(new BigDecimal("15.00"), orderBtgPactualVar1.getTotalValue());
        }
    }

    @Nested
    @DisplayName("Testes de validação (isOrderItemsInvalid)")
    class ValidationTests {

        @Test
        @DisplayName("Deve retornar true se a lista de itens for nula")
        void shouldReturnTrueWhenNull() {
            // -- 01_Cenário
            orderBtgPactualVar1.setItems(null);

            // -- 02_Ação_&_03_Verificação_Validação
            assertTrue(orderBtgPactualVar1.isOrderItemsInvalid());
        }

        @Test
        @DisplayName("Deve retornar true se a lista de itens estiver vazia")
        void shouldReturnTrueWhenEmpty() {
            // -- 01_Cenário
            orderBtgPactualVar1.setItems(new ArrayList<>());

            // -- 02_Ação_&_03_Verificação_Validação
            assertTrue(orderBtgPactualVar1.isOrderItemsInvalid());
        }

        @Test
        @DisplayName("Deve retornar false se a lista mantiver itens")
        void shouldReturnFalseWhenHasItems() {
            // -- 01_Cenário
            orderBtgPactualVar1.setItems(List.of(new OrderItemBtgPactual()));

            // -- 02_Ação_&_03_Verificação_Validação
            assertFalse(orderBtgPactualVar1.isOrderItemsInvalid());
        }
    }

//    @Test
    @DisplayName("Deve criar novos itens quando a lista de itens for inválida")
    void testRedistributeCreateNewItems_WhenItemsInvalid_ShouldCreateItems() {
        // -- 01_Cenário
        List<OrderItemBtgPactualInput> itemsInput = List.of(
                mockOrderItemInput("Product1", 2, BigDecimal.valueOf(10)),
                mockOrderItemInput("Product2", 1, BigDecimal.valueOf(20))
        );

        // -- 02_Ação
        orderBtgPactualVar2.redistributeCreateNewItems(itemsInput);

        // -- 03_Verificação_Validação
        assertEquals(2, orderBtgPactualVar2.getItems().size());
        assertEquals(BigDecimal.valueOf(40), orderBtgPactualVar2.getTotalValue());
    }

    @Test
    @DisplayName("Deve criar novos itens e atualizar os existentes quando a lista de itens já contiver elementos")
    void testRedistributeCreateNewItems_WhenItemsExist_ShouldUpdateAndAddNewItems() {
        // -- 01_Cenário
        var orderBtgPactualVar3 = OrderBtgPactualFactory.buildMockOrderNoItems();
        orderBtgPactualVar3.setItems(new ArrayList<>());

        var existingItem = new OrderItemBtgPactual();
        existingItem.setItem(1);
        existingItem.setProduct("Product1");
        existingItem.setQuantity(1);
        existingItem.setPrice(BigDecimal.valueOf(10));
        existingItem.setCreatedAt(LocalDateTime.now());

        orderBtgPactualVar3.addOrderItem(existingItem);

        List<OrderItemBtgPactualInput> itemsInput = List.of(
                new OrderItemBtgPactualInput("Product1", 3, BigDecimal.valueOf(15)), // Update existing
                new OrderItemBtgPactualInput("Product2", 2, BigDecimal.valueOf(30)) // Add new
        );

        // -- 02_Ação
        orderBtgPactualVar3.redistributeCreateNewItems(itemsInput);

        // -- 03_Verificação_Validação
        assertEquals(2, orderBtgPactualVar3.getItems().size());
        assertEquals(BigDecimal.valueOf(105), orderBtgPactualVar3.getTotalValue());
    }

    private OrderItemBtgPactualInput mockOrderItemInput(String product,
                                                        int quantity,
                                                        BigDecimal price) {

        var orderItemBtgPactualInput = Mockito.mock(OrderItemBtgPactualInput.class);

        Mockito.when(orderItemBtgPactualInput.product())
                .thenReturn(product);

        Mockito.when(orderItemBtgPactualInput.quantity())
                .thenReturn(quantity);

        Mockito.when(orderItemBtgPactualInput.price())
                .thenReturn(price);

        return orderItemBtgPactualInput;
    }

    // Stub simples para simular o comportamento de OrderItem nos testes de cálculo
    private static class StubOrderItem extends OrderItemBtgPactual {
        private final BigDecimal fixedValue;

        public StubOrderItem(BigDecimal fixedValue) {
            this.fixedValue = fixedValue;
        }

        @Override
        public BigDecimal calculateItemValue() {
            return this.fixedValue;
        }
    }
}