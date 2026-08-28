package br.com.daniel.java.quarkus.general.utils;

import com.github.javafaker.Faker;
import com.google.gson.reflect.TypeToken;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.FileSystemException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilsV1Test {

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar instanciar a classe")
    void falhaTentarCriarInstanciaViaConstrutorPrivado() throws Throwable {
        // -- 01_Cenario
        final var expectedMessage = "This is a utility class FileUtils and cannot be instantiated";

        Constructor<FileUtils> constructor = FileUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // -- 02_Ação
        final var invocationTargetException = assertThrows(
                InvocationTargetException.class, constructor::newInstance
        );

        // -- 03_Verificação_Validação
        assertNotNull(invocationTargetException);
        assertEquals(InvocationTargetException.class, invocationTargetException.getClass());

        final var unsupportedOperationException = invocationTargetException.getTargetException();
        assertInstanceOf(IllegalStateException.class, unsupportedOperationException);
        assertTrue(isNotEmpty(unsupportedOperationException.getMessage())
                && StringUtils.containsAny(unsupportedOperationException.getMessage(),
                expectedMessage)
        );
    }

    @Test
    @DisplayName("Deve retornar o ClassLoader do contexto da thread atual com sucesso")
    void shouldReturnCurrentThreadContextClassLoader() {
        // -- 01_Cenário
        var classLoader = FileUtils.getClassLoader();

        // -- 02_Ação_&_03_Verificação_Validação
        assertNotNull(classLoader, "O ClassLoader retornado não deve ser nulo");
        assertEquals(
                Thread.currentThread().getContextClassLoader(),
                classLoader,
                "O ClassLoader retornado deve ser igual ao da thread atual"
        );
    }

    @Test
    @DisplayName("Deve retornar um InputStream válido quando o arquivo de recurso existir no classpath")
    void shouldReturnInputStreamWhenResourceExists() {
        // -- 01_Cenário
        final var existingResource = "personagens_marvel.json";

        // -- 02_Ação
        final var inputStream = FileUtils.getResourceAsStream(existingResource);

        // -- 03_Verificação_Validação
        assertNotNull(inputStream, "O InputStream retornado não deve ser nulo para um recurso existente");
    }

    @Test
    @DisplayName("Deve retornar null quando o arquivo de recurso não for encontrado no classpath")
    void shouldReturnNullWhenResourceDoesNotExist() {
        // -- 01_Cenário
        final var nonExistentResource = "recurso-inexistente-xyz.txt";

        // -- 02_Ação
        final var inputStream = FileUtils.getResourceAsStream(nonExistentResource);

        // -- 03_Verificação_Validação
        assertNull(inputStream, "O InputStream retornado deve ser nulo quando o recurso não for encontrado");
    }

    @Test
    @DisplayName("Deve converter uma String XML válida para o objeto correspondente")
    void shouldConvertValidXmlStringToObject() {
        // -- 01_Cenário
        var xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><config><name>MeuSistema</name></config>";

        // -- 02_Ação
        final var xmlConfigModelResult = FileUtils.toObjectFromFileContentXmlBy(xmlContent, XmlConfigModel.class);

        // -- 03_Verificação_Validação
        assertNotNull(xmlConfigModelResult, "O objeto retornado não deve ser nulo");
        assertEquals("MeuSistema", xmlConfigModelResult.getName(), "O valor mapeado do XML está incorreto");
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o XML for inválido ou malformado")
    void shouldThrowExceptionWhenXmlIsInvalid() {
        // -- 01_Cenário
        final var invalidXmlContent = "<config><name>Incompleto</config>";

        // 02_Ação - 03_Verificação_Validação
        var illegalStateException = assertThrows(
                IllegalStateException.class,
                () -> FileUtils.toObjectFromFileContentXmlBy(invalidXmlContent, XmlConfigModel.class),
                "Deveria lançar IllegalStateException para XML inválido"
        );

        assertNotNull(illegalStateException.getMessage(), "A mensagem de erro não deve ser nula");
        assertTrue(illegalStateException.getMessage().contains("Failed to create and/or convert object"),
                "A mensagem de erro deve conter o prefixo esperado");
        assertNotNull(illegalStateException.getCause(), "A exceção original deve ser preservada como causa (cause)");
    }

    @Test
    @DisplayName("Deve carregar o conteúdo do arquivo de configuração com sucesso quando o arquivo existir")
    void shouldLoadConfigFileSuccessfully() {
        // -- 01_Cenário
        final var existingFileName = "personagens_marvel.json";

        try {
            // 02_Ação
            final var content = FileUtils.loadConfigFile(existingFileName);

            // 03_Verificação_Validação
            assertNotNull(content, "O conteúdo carregado não deve ser nulo");
            assertFalse(content.isBlank(), "O conteúdo do arquivo não deve estar vazio");
        } catch (FileSystemException e) {
            fail("Não deveria lançar FileSystemException para um arquivo existente: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve lançar FileSystemException quando o arquivo de configuração não for encontrado")
    void shouldThrowFileSystemExceptionWhenFileNotFound() {
        // -- 01_Cenário
        final var nonExistentFileName = "arquivo-que-nao-existe-12345.json";

        // 02_Ação - 03_Verificação_Validação
        var fileSystemException = assertThrows(
                FileSystemException.class,
                () -> FileUtils.loadConfigFile(nonExistentFileName),
                "Deveria lançar FileSystemException quando o arquivo não for encontrado"
        );

        assertNotNull(fileSystemException.getMessage(), "A mensagem de exceção não deve ser nula");
        assertTrue(fileSystemException.getMessage().contains("Arquivo não encontrado: " + nonExistentFileName),
                "A mensagem deve informar que o arquivo não foi encontrado");
    }

    @Test
    @DisplayName("Deve converter uma lista de mapas em uma String JSON válida")
    void shouldConvertListOfMapsToJsonString() {
        // -- 01_Cenário
        Faker instance = Faker.instance();

        var nameVar1 = instance.name().fullName();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", nameVar1);
        item1.put("active", true);

        var nameVar2 = instance.name().fullName();
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("name", nameVar2);
        item2.put("active", false);
        List<Map<String, Object>> items = List.of(item1, item2);

        // -- 02_Ação
        final var jsonResult = FileUtils.toStringJsonFromGSONBy(items);

        // -- 03_Verificação_Validação
        assertNotNull(jsonResult, "O JSON retornado não deve ser nulo");
        assertFalse(jsonResult.isBlank(), "O JSON retornado não deve estar em branco");
        assertTrue(FileUtils.isValidJson(jsonResult));

        // Validação básica da estrutura JSON gerada
        // Desserializa o JSON de volta para uma estrutura manipulável
        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> itemsValidate = FileUtils.GSON.fromJson(jsonResult, listType);

        // Assert
        assertNotNull(itemsValidate, "A lista não deve ser nula");
        assertEquals(2, itemsValidate.size(), "A lista deve conter 2 elementos");

        // Validando o primeiro item
        assertEquals(1.0, itemsValidate.get(0).get("id"), "O ID do primeiro item deve ser 1"); // Números no Gson viram Double/Number por padrão nos Maps
        assertEquals(nameVar1, itemsValidate.get(0).get("name"), "O nome do primeiro item deve ser 'Item A'");
        assertEquals(true, itemsValidate.get(0).get("active"), "O status ativo do primeiro item deve ser true");

        // Validando o segundo item
        assertEquals(2.0, itemsValidate.get(1).get("id"), "O ID do segundo item deve ser 2");
        assertEquals(nameVar2, itemsValidate.get(1).get("name"), "O nome do segundo item deve ser 'Item B'");
        assertEquals(false, itemsValidate.get(1).get("active"), "O status ativo do segundo item deve ser false");
    }

    @Test
    @DisplayName("Deve retornar uma string de array vazio quando a lista estiver vazia")
    void shouldReturnEmptyJsonArrayWhenListIsEmpty() {
        // -- 01_Cenário
        List<Map<String, Object>> emptyList = Collections.emptyList();

        // -- 02_Ação
        final var jsonResult = FileUtils.toStringJsonFromGSONBy(emptyList);

        // -- 03_Verificação_Validação
        assertEquals("[]", jsonResult, "O resultado para uma lista vazia deve ser um array JSON vazio");
    }

    @Test
    @DisplayName("Deve serializar corretamente quando a lista for nula")
    void shouldHandleNullListGracefully() {
        // -- 01_Cenário
        final var jsonResult = FileUtils.toStringJsonFromGSONBy(null);

        // -- 03_Verificação_Validação
        assertEquals("null", jsonResult, "O Gson serializa referências nulas para a string 'null'");
    }

    @Test
    @DisplayName("Deve retornar true para uma string JSON de objeto válida")
    void shouldReturnTrueForValidJsonObject() {
        // Arrange
        String validJson = "{\"id\": 1, \"name\": \"Test\", \"active\": true}";

        // Act & Assert
        assertTrue(FileUtils.isValidJson(validJson), "A string deveria ser um JSON válido");
    }

    @Test
    @DisplayName("Deve retornar true para uma string JSON de array válida")
    void shouldReturnTrueForValidJsonArray() {
        // Arrange
        String validJsonArray = "[{\"id\": 1}, {\"id\": 2}]";

        // Act & Assert
        assertTrue(FileUtils.isValidJson(validJsonArray), "A string deveria ser um array JSON válido");
    }

    @ParameterizedTest
    @DisplayName("Deve retornar false para strings nulas, vazias ou em branco")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    void shouldReturnFalseForNullOrBlankStrings(String blankOrNullJson) {
        // Act & Assert
        assertFalse(FileUtils.isValidJson(blankOrNullJson), "Strings nulas ou em branco devem retornar false");
    }

    @Setter
    @XmlRootElement(name = "config")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class XmlConfigModel {

        private String name;

        @XmlElement
        public String getName() {
            return name;
        }
    }
}