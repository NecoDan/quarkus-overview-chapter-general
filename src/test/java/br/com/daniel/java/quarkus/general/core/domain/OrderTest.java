package br.com.daniel.java.quarkus.general.core.domain;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private OrderBtgPactual orderBtgPactual;

    @BeforeEach
    void setUp() {
        orderBtgPactual = new OrderBtgPactual();
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
            orderBtgPactual.defineDates();

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactual.getCreatedAt());
            assertNotNull(orderBtgPactual.getUpdateAt());

            // Verifica se a data gerada é próxima/posterior ao momento do teste
            assertTrue(orderBtgPactual.getCreatedAt().isAfter(beforeExecution)
                    || orderBtgPactual.getCreatedAt().isEqual(beforeExecution)
            );

            assertTrue(orderBtgPactual.getUpdateAt().isAfter(beforeExecution)
                    || orderBtgPactual.getUpdateAt().isEqual(beforeExecution)
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
            orderBtgPactual.addOrderItem(orderItem);

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactual.getItems());
            assertEquals(1, orderBtgPactual.getItems().size());
            assertTrue(orderBtgPactual.getItems().contains(orderItem));
        }

        @Test
        @DisplayName("Deve adicionar um item mantendo os itens já existentes na lista")
        void shouldAddSingleItemToExistingList() {
            // -- 01_Cenário
            orderBtgPactual.setItems(new ArrayList<>());
            var orderItemVar1 = new OrderItemBtgPactual();
            var orderItemVar2 = new OrderItemBtgPactual();

            // -- 02_Ação
            orderBtgPactual.addOrderItem(orderItemVar1);
            orderBtgPactual.addOrderItem(orderItemVar2);

            // -- 03_Verificação_Validação
            assertEquals(2, orderBtgPactual.getItems().size());
        }

        @Test
        @DisplayName("Deve inicializar a lista e adicionar uma coleção de itens quando a lista for nula")
        void shouldInitializeListAndAddCollection() {
            // -- 01_Cenário
            var orderItemList = List.of(new OrderItemBtgPactual(),
                    new OrderItemBtgPactual()
            );

            // -- 02_Ação
            orderBtgPactual.addAllOrderItem(orderItemList);

            // -- 03_Verificação_Validação
            assertNotNull(orderBtgPactual.getItems());
            assertEquals(2, orderBtgPactual.getItems().size());
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
            orderBtgPactual.setItems(new ArrayList<>());

            // -- 02_Ação
            orderBtgPactual.calculateTotalValue();

            // -- 03_Verificação_Validação
            assertEquals(BigDecimal.ZERO, orderBtgPactual.getTotalValue());
        }

        @Test
        @DisplayName("Deve somar corretamente o valor de todos os itens da lista")
        void shouldCalculateTotalValueCorrectly() {
            // Supondo que OrderItem possua construtor ou setters apropriados
            // Para estes testes, usamos objetos stub que retornam um valor em calculateItemValue()
            var orderItemVar1 = new StubOrderItem(new BigDecimal("10.50"));
            var orderItemVar2 = new StubOrderItem(new BigDecimal("20.25"));
            var item3 = new StubOrderItem(new BigDecimal("5.25"));

            orderBtgPactual.setItems(List.of(orderItemVar1, orderItemVar2, item3));

            orderBtgPactual.calculateTotalValue();

            assertEquals(new BigDecimal("36.00"), orderBtgPactual.getTotalValue());
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
            orderBtgPactual.setItems(itemsWithNull);
            orderBtgPactual.calculateTotalValue();

            // -- 03_Verificação_Validação
            assertEquals(new BigDecimal("15.00"), orderBtgPactual.getTotalValue());
        }
    }

    @Nested
    @DisplayName("Testes de validação (isOrderItemsInvalid)")
    class ValidationTests {

        @Test
        @DisplayName("Deve retornar true se a lista de itens for nula")
        void shouldReturnTrueWhenNull() {
            // -- 01_Cenário
            orderBtgPactual.setItems(null);

            // -- 02_Ação_&_03_Verificação_Validação
            assertTrue(orderBtgPactual.isOrderItemsInvalid());
        }

        @Test
        @DisplayName("Deve retornar true se a lista de itens estiver vazia")
        void shouldReturnTrueWhenEmpty() {
            // -- 01_Cenário
            orderBtgPactual.setItems(new ArrayList<>());

            // -- 02_Ação_&_03_Verificação_Validação
            assertTrue(orderBtgPactual.isOrderItemsInvalid());
        }

        @Test
        @DisplayName("Deve retornar false se a lista mantiver itens")
        void shouldReturnFalseWhenHasItems() {
            // -- 01_Cenário
            orderBtgPactual.setItems(List.of(new OrderItemBtgPactual()));

            // -- 02_Ação_&_03_Verificação_Validação
            assertFalse(orderBtgPactual.isOrderItemsInvalid());
        }
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