package br.com.daniel.java.quarkus.general.utils;

import io.smallrye.config.SmallRyeConfig;
import org.assertj.core.api.AssertionsForClassTypes;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class FunctionalUtilsTest {

    @Test
    @DisplayName("Deve converter UUID para ObjectId de 24 caracteres hexadecimais com sucesso")
    void shouldConvertUuidToObjectIdSuccessfully() {
        // -- 01_Cenário
        var uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // A string sem hífens é "123e4567e89b12d3a456426614174000"
        // Os primeiros 24 caracteres são "123e4567e89b12d3a4564266"
        var expectedHex = "123e4567e89b12d3a4564266";

        // -- 02_Ação
        var objectId = FunctionalUtils.uuidToObjectIdMongoDb(uuid);

        // -- 03_Verificação_Validação
        AssertionsForClassTypes.assertThat(objectId).isNotNull();
        AssertionsForClassTypes.assertThat(objectId.toHexString()).isEqualTo(expectedHex);
    }

    @Test
    @DisplayName("Deve demonstrar perda de dados (colisão) quando dois UUIDs diferem apenas nos últimos 8 caracteres")
    void shouldProduceSameObjectIdForUuidsDifferingOnlyInTail() {
        // -- 01_Cenário
        var uuid1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var uuid2 = UUID.fromString("123e4567-e89b-12d3-a456-4266ffffffff");

        // -- 02_Ação
        var objectId1 = FunctionalUtils.uuidToObjectIdMongoDb(uuid1);
        var objectId2 = FunctionalUtils.uuidToObjectIdMongoDb(uuid2);

        // -- 03_Verificação_Validação
        AssertionsForClassTypes.assertThat(objectId1).isEqualTo(objectId2);
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando o UUID for nulo")
    void shouldThrowNullPointerExceptionWhenUuidIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            FunctionalUtils.uuidToObjectIdMongoDb(null);
        });
    }

    @Test
    void formatsCreationDatesAndHandlesNull() {
        var date = LocalDateTime.of(2026, 8, 25, 14, 30, 45);

        Assertions.assertEquals("25/08/2026 14:30:45", FunctionalUtils.formatCreationDate(date));
        Assertions.assertEquals("25/08/2026 14:30:45", FunctionalUtils.formatCreationDateBy(date));
        Assertions.assertEquals("", FunctionalUtils.formatCreationDate(null));
    }

    @Test
    void formatsNumbersWithTwoDecimalPlaces() {
        Assertions.assertEquals("10.24", FunctionalUtils.formatDecimalNumber(new BigDecimal("10.235")));
        Assertions.assertEquals("10.50", FunctionalUtils.formatDecimalNumberBy(10.5));
    }

    @Test
    void rejectsNullNumericValue() {
        var error = Assertions.assertThrows(IllegalArgumentException.class,
                () -> FunctionalUtils.formatDecimalNumber(null));

        Assertions.assertEquals("Valor numerico encontra-se inválido e/ou inexsitente.", error.getMessage());
    }

    @Test
    void formatsCpfWithOrWithoutPunctuation() {
        Assertions.assertEquals("123.456.789-01", FunctionalUtils.formatCpf("12345678901"));
        Assertions.assertEquals("123.456.789-01", FunctionalUtils.formatCpf("123.456.789-01"));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar instanciar a classe utilitária via Reflection")
    void constructor_ShouldThrowExceptionWhenInstantiated() throws NoSuchMethodException {
        Constructor<FunctionalUtils> constructor = FunctionalUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Assertions.assertInstanceOf(IllegalStateException.class, exception.getCause());
        Assertions.assertEquals("This is a utility class FunctionalUtils and cannot be instantiated", exception.getCause().getMessage());
    }

    @Nested
    @DisplayName("getActiveProfiles")
    class GetActiveProfilesTests {

        @Test
        @DisplayName("Deve retornar a lista de perfis ativos do SmallRyeConfig com sucesso")
        void getActiveProfiles_ShouldReturnProfiles() {
            try (MockedStatic<ConfigProvider> mockedConfigProvider = Mockito.mockStatic(ConfigProvider.class)) {
                org.eclipse.microprofile.config.Config mockConfig = Mockito.mock(org.eclipse.microprofile.config.Config.class);
                SmallRyeConfig mockSmallRyeConfig = Mockito.mock(SmallRyeConfig.class);
                List<String> expectedProfiles = List.of("dev", "test");

                mockedConfigProvider.when(ConfigProvider::getConfig).thenReturn(mockConfig);
                Mockito.when(mockConfig.unwrap(SmallRyeConfig.class)).thenReturn(mockSmallRyeConfig);
                Mockito.when(mockSmallRyeConfig.getProfiles()).thenReturn(expectedProfiles);

                List<String> activeProfiles = FunctionalUtils.getActiveProfiles();

                Assertions.assertNotNull(activeProfiles);
                Assertions.assertEquals(2, activeProfiles.size());
                Assertions.assertEquals(expectedProfiles, activeProfiles);
            }
        }
    }

    @Nested
    @DisplayName("formatCreationDate / formatCreationDateBy")
    class FormatCreationDateTests {

        @Test
        @DisplayName("Deve formatar LocalDateTime no padrão brasileiro (dd/MM/yyyy HH:mm:ss)")
        void formatCreationDate_ShouldFormatCorrectly() {
            LocalDateTime dateTime = LocalDateTime.of(2026, 8, 27, 14, 30, 45);

            String result = FunctionalUtils.formatCreationDate(dateTime);

            Assertions.assertEquals("27/08/2026 14:30:45", result);
        }

        @Test
        @DisplayName("Deve retornar String vazia quando o LocalDateTime for nulo")
        void formatCreationDate_ShouldReturnEmptyStringWhenNull() {
            String result = FunctionalUtils.formatCreationDate(null);

            Assertions.assertEquals("", result);
        }
    }

    @Nested
    @DisplayName("onlyLocalDateTimeDefaultEnglish / onlyLocalDateTimeBy")
    class OnlyLocalDateTimeTests {

        @Test
        @DisplayName("Deve converter String no formato inglês padrão para LocalDateTime")
        void onlyLocalDateTimeDefaultEnglish_ShouldParseDate() {
            String dateStr = "2026-08-27 15:45:00";

            LocalDateTime result = FunctionalUtils.onlyLocalDateTimeDefaultEnglish(dateStr);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(LocalDateTime.of(2026, 8, 27, 15, 45, 0), result);
        }

        @Test
        @DisplayName("Deve truncar e converter String com mais de 19 caracteres")
        void onlyLocalDateTimeDefaultEnglish_ShouldTruncateAndParseDate() {
            String dateStr = "2026-08-27 15:45:00.1234567";

            LocalDateTime result = FunctionalUtils.onlyLocalDateTimeDefaultEnglish(dateStr);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(LocalDateTime.of(2026, 8, 27, 15, 45, 0), result);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Deve lançar IllegalStateException quando a data enviada for inválida ou nula")
        void onlyLocalDateTimeDefaultEnglish_ShouldThrowExceptionWhenInvalid(String input) {
            Assertions.assertThrows(
                    IllegalStateException.class,
                    () -> FunctionalUtils.onlyLocalDateTimeDefaultEnglish(input)
            );
        }

        @Test
        @DisplayName("Deve converter String para LocalDateTime informando formato customizado")
        void onlyLocalDateTimeBy_ShouldParseCustomFormat() {
            String dateStr = "27-08-2026 16:20";
            String pattern = "dd-MM-yyyy HH:mm";

            LocalDateTime result = FunctionalUtils.onlyLocalDateTimeBy(dateStr, pattern);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(LocalDateTime.of(2026, 8, 27, 16, 20), result);
        }

        @Test
        @DisplayName("Deve lançar IllegalStateException no onlyLocalDateTimeBy quando formato ou valor forem inválidos")
        void onlyLocalDateTimeBy_ShouldThrowExceptionWhenArgumentsInvalid() {
            Assertions.assertThrows(
                    IllegalStateException.class,
                    () -> FunctionalUtils.onlyLocalDateTimeBy(null, "yyyy-MM-dd")
            );

            Assertions.assertThrows(
                    IllegalStateException.class,
                    () -> FunctionalUtils.onlyLocalDateTimeBy("2026-08-27", "")
            );
        }
    }

    @Nested
    @DisplayName("Operações numéricas (onlyLongNumbers / onlyNumbers / onlyIntNumbers)")
    class NumberOperationsTests {

        @Test
        @DisplayName("Deve extrair apenas números e converter para long")
        void onlyLongNumbers_ShouldReturnLongValue() {
            long result = FunctionalUtils.onlyLongNumbers("ABC-12345-XYZ");
            Assertions.assertEquals(12345L, result);
        }

        @Test
        @DisplayName("Deve retornar 0L ao passar String nula ou sem números para long")
        void onlyLongNumbers_ShouldReturnZeroWhenInvalid() {
            Assertions.assertEquals(0L, FunctionalUtils.onlyLongNumbers(null));
            Assertions.assertEquals(0L, FunctionalUtils.onlyLongNumbers("   "));
        }

        @Test
        @DisplayName("Deve remover todos os não-dígitos da String")
        void onlyNumbers_ShouldRemoveNonDigits() {
            String result = FunctionalUtils.onlyNumbers("123.456.789-00");
            Assertions.assertEquals("12345678900", result);
        }

        @Test
        @DisplayName("Deve retornar String vazia no onlyNumbers quando entrada for nula")
        void onlyNumbers_ShouldReturnEmptyWhenNull() {
            Assertions.assertEquals("", FunctionalUtils.onlyNumbers(null));
        }

        @Test
        @DisplayName("Deve converter String numérica simples para inteiro via onlyIntNumbers e onlyIntegerNumbers")
        void onlyIntegerNumbers_ShouldReturnInteger() {
            Assertions.assertEquals(987, FunctionalUtils.onlyIntNumbers("987"));
            Assertions.assertEquals(987, FunctionalUtils.onlyIntegerNumbers("987"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Deve retornar 0 quando a String para conversão de inteiro for inválida/nula")
        void onlyIntegerNumbers_ShouldReturnZeroWhenInvalid(String input) {
            Assertions.assertEquals(0, FunctionalUtils.onlyIntNumbers(input));
            Assertions.assertEquals(0, FunctionalUtils.onlyIntegerNumbers(input));
        }
    }

    @Nested
    @DisplayName("isStringValida")
    class IsStringValidaTests {

        @Test
        @DisplayName("Deve retornar true para String preenchida e válida")
        void isStringValida_ShouldReturnTrue() {
            Assertions.assertTrue(FunctionalUtils.isStringValida("Texto Valido"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Deve retornar false para Strings nulas, vazias ou em branco")
        void isStringValida_ShouldReturnFalseForInvalidStrings(String input) {
            Assertions.assertFalse(FunctionalUtils.isStringValida(input));
        }
    }

    @Nested
    @DisplayName("formatDecimalNumber / formatDecimalNumberBy")
    class DecimalFormattingTests {

        @Test
        @DisplayName("Deve formatar BigDecimal com duas casas decimais e separador ponto")
        void formatDecimalNumber_ShouldFormatBigDecimal() {
            BigDecimal value = new BigDecimal("1234.5678");

            String result = FunctionalUtils.formatDecimalNumber(value);

            Assertions.assertEquals("1234.57", result); // Arredondamento HALF_UP
        }

        @Test
        @DisplayName("Deve formatar Double com duas casas decimais e separador ponto")
        void formatDecimalNumberBy_ShouldFormatDouble() {
            Double value = 15.5;

            String result = FunctionalUtils.formatDecimalNumberBy(value);

            Assertions.assertEquals("15.50", result);
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando o número for nulo")
        void formatDecimalNumber_ShouldThrowExceptionWhenNull() {
            BigDecimal nullValue = null;

            IllegalArgumentException exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> FunctionalUtils.formatDecimalNumber(nullValue)
            );

            Assertions.assertEquals("Valor numerico encontra-se inválido e/ou inexsitente.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("formatCpf")
    class FormatCpfTests {

        @Test
        @DisplayName("Deve formatar CPF numérico ou com máscara para o padrão 000.000.000-00")
        void formatCpf_ShouldFormatCorrectly() {
            String rawCpf = "12345678901";
            String maskedCpf = "123.456.789-01";

            Assertions.assertEquals("123.456.789-01", FunctionalUtils.formatCpf(rawCpf));
            Assertions.assertEquals("123.456.789-01", FunctionalUtils.formatCpf(maskedCpf));
        }
    }
}
