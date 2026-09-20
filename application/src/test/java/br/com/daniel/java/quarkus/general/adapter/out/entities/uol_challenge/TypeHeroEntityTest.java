package br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - TypeHeroEntity")
class TypeHeroEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Validações da Anotação @IntervaloValido")
    class ValidacaoIntervaloDatasTest {

        @Test
        @DisplayName("Deve passar na validação quando a data final for após a data inicial")
        void devePassarQuandoDataFinalForAposDataInicial() {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fim = inicio.plusDays(5);

            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .description("Heroi Valido")
                    .startDateExpiration(inicio)
                    .endDateExpiration(fim)
                    .build();

            Set<ConstraintViolation<TypeHeroEntity>> violations = validator.validate(entity);

            assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
        }

        @Test
        @DisplayName("Deve falhar na validação quando a data final for anterior à data inicial")
        void deveFalharQuandoDataFinalForAnteriorADataInicial() {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fim = inicio.minusDays(1);

            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .description("Wolverine")
                    .startDateExpiration(inicio)
                    .endDateExpiration(fim)
                    .build();

            Set<ConstraintViolation<TypeHeroEntity>> violations = validator.validate(entity);

            assertFalse(violations.isEmpty(), "Deve conter violação de validação");
            assertEquals(1, violations.size());

            ConstraintViolation<TypeHeroEntity> violation = violations.iterator().next();
            assertEquals("A validade final deve ser após a validade inicial", violation.getMessage());
        }

        @Test
        @DisplayName("Deve falhar na validação quando as datas forem exatamente iguais")
        void deveFalharQuandoDatasForemIguais() {
            LocalDateTime agora = LocalDateTime.now();

            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .description("Homem de Ferro")
                    .startDateExpiration(agora)
                    .endDateExpiration(agora)
                    .build();

            Set<ConstraintViolation<TypeHeroEntity>> violations = validator.validate(entity);

            assertFalse(violations.isEmpty(), "Deve falhar se o intervalo não for estritamente posterior");
        }

        @Test
        @DisplayName("Deve passar na validação quando as datas forem nulas (tratamento de null pointer no validator)")
        void devePassarQuandoDatasForemNulas() {
            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .description("Batman")
                    .build();

            Set<ConstraintViolation<TypeHeroEntity>> violations = validator.validate(entity);

            assertTrue(violations.isEmpty(), "Validação de intervalo deve ignorar campos nulos caso não haja @NotNull");
        }
    }

    @Nested
    @DisplayName("Testes de Construtores, Lombok e Getters/Setters")
    class LombokETodosAtributosTest {

        @Test
        @DisplayName("Deve instanciar corretamente via Builder e getters")
        void deveInstanciarViaBuilder() {
            LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 10, 0);
            LocalDateTime fim = LocalDateTime.of(2026, 12, 31, 23, 59);

            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .id(1L)
                    .description("Guerreiro")
                    .startDateExpiration(inicio)
                    .endDateExpiration(fim)
                    .build();

            assertAll("Verificando valores do objeto",
                    () -> assertEquals(1L, entity.getId()),
                    () -> assertEquals("Guerreiro", entity.getDescription()),
                    () -> assertEquals(inicio, entity.getStartDateExpiration()),
                    () -> assertEquals(fim, entity.getEndDateExpiration())
            );
        }

        @Test
        @DisplayName("Deve instanciar via Construtor Completo e Sem Argumentos")
        void deveInstanciarViaConstrutores() {
            TypeHeroEntity emptyEntity = new TypeHeroEntity();
            assertNotNull(emptyEntity);

            LocalDateTime agora = LocalDateTime.now();
            TypeHeroEntity fullEntity = new TypeHeroEntity(2L, "Vingadores", agora, agora.plusDays(1));

            assertEquals(2L, fullEntity.getId());
            assertEquals("Vingadores", fullEntity.getDescription());
        }

        @Test
        @DisplayName("Deve validar o comportamento de Equals e HashCode")
        void deveTestarEqualsEHashCode() {
            LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 0, 0);
            LocalDateTime fim = LocalDateTime.of(2026, 1, 2, 0, 0);

            TypeHeroEntity entity1 = new TypeHeroEntity(1L, "Vingadores", inicio, fim);
            TypeHeroEntity entity2 = new TypeHeroEntity(1L, "Vingadores", inicio, fim);
            TypeHeroEntity entity3 = new TypeHeroEntity(2L, "TheBoys", inicio, fim);

            assertEquals(entity1, entity2);
            assertEquals(entity1.hashCode(), entity2.hashCode());
            assertNotEquals(entity1, entity3);
        }

        @Test
        @DisplayName("Deve validar a saída do método toString")
        void deveTestarToString() {
            TypeHeroEntity entity = TypeHeroEntity.builder()
                    .id(10L)
                    .description("Vingadores")
                    .build();

            String toStringResult = entity.toString();

            assertTrue(toStringResult.contains("id=10"));
            assertTrue(toStringResult.contains("description=Vingadores"));
        }
    }

}