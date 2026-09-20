package br.com.daniel.java.quarkus.general.core.domain;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderItemBtgPactual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemBtgPactualTest {

    @ParameterizedTest(name = "Preço = {0}, Quantidade = {1} -> Total Esperado = {2}")
    @CsvSource({
            "10.50, 2, 21.00",
            "0.00, 5, 0.00",
            "15.99, 0, 0.00",
            "100.00, 1, 100.00"
    })
    @DisplayName("Deve calcular o valor total do item corretamente para valores válidos")
    void shouldCalculateItemValueCorrectly(BigDecimal price, Integer quantity, BigDecimal expectedResult) {
        var orderItem = new OrderItemBtgPactual();
        orderItem.setPrice(price);
        orderItem.setQuantity(quantity);

        var result = orderItem.calculateItemValue();

        assertThat(result).isEqualByComparingTo(expectedResult);
    }

    @Test
    @DisplayName("Deve retornar BigDecimal.ZERO quando preço e quantidade forem nulos")
    void shouldReturnZeroWhenBothPriceAndQuantityAreNull() {
        var orderItem = new OrderItemBtgPactual();
        orderItem.setPrice(null);
        orderItem.setQuantity(null);

        var result = orderItem.calculateItemValue();

        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Deve calcular o valor total do item com sucesso quando preço e quantidade forem válidos")
    void shouldCalculateItemValueSuccessfullyWhenParamsAreValid() {
        var orderItem = new OrderItemBtgPactual();
        orderItem.setPrice(new BigDecimal("10.50"));
        orderItem.setQuantity(3);

        var result = orderItem.calculateItemValue();

        assertThat(result)
                .isNotNull()
                .isEqualByComparingTo(new BigDecimal("31.50"));
    }

    @ParameterizedTest(name = "Deve retornar ZERO quando preço é {0} e quantidade é {1}")
    @MethodSource("provideNullOrInvalidParams")
    @DisplayName("Deve retornar BigDecimal.ZERO quando preço ou quantidade forem nulos")
    void shouldReturnZeroWhenPriceOrQuantityIsNull(BigDecimal price, Integer quantity) {
        var orderItem = new OrderItemBtgPactual();
        orderItem.setPrice(price);
        orderItem.setQuantity(quantity);

        var result = orderItem.calculateItemValue();

        assertThat(result)
                .isNotNull()
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    private static Stream<Arguments> provideNullOrInvalidParams() {
        return Stream.of(
                Arguments.of(null, 5),                       // Preço nulo
                Arguments.of(new BigDecimal("10.00"), null),  // Quantidade nula
                Arguments.of(null, null)                      // Ambos nulos
        );
    }

    @Test
    @DisplayName("Deve retornar ZERO quando a quantidade for 0")
    void shouldReturnZeroWhenQuantityIsZero() {
        var orderItem = new OrderItemBtgPactual();
        orderItem.setPrice(new BigDecimal("25.00"));
        orderItem.setQuantity(0);

        var result = orderItem.calculateItemValue();

        assertThat(result)
                .isNotNull()
                .isEqualByComparingTo(BigDecimal.ZERO);
    }
}