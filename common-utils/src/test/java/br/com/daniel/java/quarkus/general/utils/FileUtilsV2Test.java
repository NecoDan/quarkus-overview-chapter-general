package br.com.daniel.java.quarkus.general.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class FileUtilsV2Test {

    @Test
    @DisplayName("Deve retornar a lista de nomes de arquivos presentes no diretório")
    void shouldReturnListOfFileNamesSuccessfully(@TempDir Path tempDir) throws IOException {
        // -- 01_Cenário
        // Cria arquivos temporários reais no diretório
        Files.createFile(tempDir.resolve("vingadores.csv"));
        Files.createFile(tempDir.resolve("liga_da_justica.csv"));

        // -- 02_Ação
        List<String> result = FileUtils.getFileNameList(tempDir.toString());

        // -- 03_Verificação_Validação
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("vingadores.csv"));
        Assertions.assertTrue(result.contains("liga_da_justica.csv"));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o diretório estiver vazio")
    void shouldReturnEmptyListWhenDirectoryIsEmpty(@TempDir Path tempDir) {
        // -- 02_Ação
        List<String> result = FileUtils.getFileNameList(tempDir.toString());

        // -- 03_Verificação_Validação
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o caminho do diretório não existir")
    void shouldThrowIllegalStateExceptionWhenDirectoryDoesNotExist() {
        // -- 01_Cenário
        String invalidPath = "/caminho/invalido/que/nao/existe";

        // -- 02_Ação & Assert
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> FileUtils.getFileNameList(invalidPath)
        );

        Assertions.assertTrue(exception.getMessage().contains("Error reading directory"));
    }

    @Nested
    @DisplayName("getDefaultResourcesFolderPath")
    class GetDefaultResourcesFolderPathTests {

        //        @Test
        @DisplayName("Deve retornar o Path do diretório de recursos com sucesso em ambiente normal")
        void getDefaultResourcesFolderPath_ShouldReturnPathSuccessfully() {
            // Act
            Path result = FileUtils.getDefaultResourcesFolderPath();

            // Assert
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.toString().contains("target")
                    || result.toString().contains("build")
                    || result.toString().contains("classes")
            );
        }

        @Test
        @DisplayName("Deve lançar IllegalStateException quando resourceUrl for nulo")
        void getDefaultResourcesFolderPath_ShouldThrowExceptionWhenResourceUrlIsNull() {
            // Arrange
            ClassLoader mockClassLoader = Mockito.mock(ClassLoader.class);
            Mockito.when(mockClassLoader.getResource(StringUtils.EMPTY)).thenReturn(null);

            try (MockedStatic<FileUtils> mockUtils = Mockito.mockStatic(FileUtils.class, Mockito.CALLS_REAL_METHODS)) {
                mockUtils.when(FileUtils::getClassLoader)
                        .thenReturn(mockClassLoader);

                // Act & Assert
                IllegalStateException exception = Assertions.assertThrows(
                        IllegalStateException.class,
                        FileUtils::getDefaultResourcesFolderPath
                );

                Assertions.assertTrue(exception.getMessage().contains("Resources folder not found"));
            }
        }

        //        @Test
        @DisplayName("Deve capturar e relançar IllegalStateException quando Paths.get lançar exceção")
        void getDefaultResourcesFolderPath_ShouldCatchAndReThrowExceptionWhenPathsGetFails() throws Exception {
            // Arrange
            URL mockUrl = new URL("file:/caminho/invalido");
            ClassLoader mockClassLoader = Mockito.mock(ClassLoader.class);
            Mockito.when(mockClassLoader.getResource(StringUtils.EMPTY)).thenReturn(mockUrl);

            try (MockedStatic<FunctionalUtils> mockUtils = Mockito.mockStatic(FunctionalUtils.class, Mockito.CALLS_REAL_METHODS);
                 MockedStatic<Paths> mockPaths = Mockito.mockStatic(Paths.class)) {

                mockUtils.when(FileUtils::getClassLoader).thenReturn(mockClassLoader);
                mockPaths.when(() -> Paths.get(ArgumentMatchers.anyString())).thenThrow(new IllegalArgumentException("Path inválido"));

                // Act & Assert
                IllegalStateException exception = Assertions.assertThrows(
                        IllegalStateException.class,
                        FileUtils::getDefaultResourcesFolderPath
                );

                Assertions.assertTrue(exception.getMessage().contains("Resources folder not found: Path inválido"));
            }
        }
    }

    @Nested
    @DisplayName("getFileNameList - Testes Unitários Simulado (Mocking Files.list)")
    class MockedFilesTests {

        @Test
        @DisplayName("Deve capturar IOException de Files.list e lançar IllegalStateException")
        void shouldThrowIllegalStateExceptionWhenFilesListFails() {
            // -- 01_Cenário
            try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.list(ArgumentMatchers.any(Path.class)))
                        .thenThrow(new IOException("Acesso negado ou erro de I/O"));

                // -- 02_Ação & Assert
                IllegalStateException exception = Assertions.assertThrows(
                        IllegalStateException.class,
                        () -> FileUtils.getFileNameList("qualquer_caminho")
                );

                Assertions.assertEquals("Error reading directory: Acesso negado ou erro de I/O", exception.getMessage());
            }
        }

        @Test
        @DisplayName("Deve processar a Stream de Paths corretamente via Mock")
        void shouldProcessPathStreamCorrectly() {
            // -- 01_Cenário
            Path path1 = Paths.get("/tmp/heroes/codinomes1.json");
            Path path2 = Paths.get("/tmp/heroes/codinomes2.json");

            try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.list(ArgumentMatchers.any(Path.class)))
                        .thenReturn(Stream.of(path1, path2));

                // -- 02_Ação
                List<String> result = FileUtils.getFileNameList("/tmp/heroes");

                // -- 03_Verificação_Validação
                Assertions.assertNotNull(result);
                Assertions.assertEquals(2, result.size());
                Assertions.assertEquals(List.of("codinomes1.json", "codinomes2.json"), result);
            }
        }
    }
}
