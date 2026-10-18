package br.com.daniel.java.quarkus.general.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.opencsv.CSVWriter;
import jakarta.xml.bind.JAXBContext;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Classe utilitária para manipulação de arquivos.
 * <p>
 * <p>
 * Esta classe fornece métodos para geração de arquivos CSV, conversão de conteúdo
 * para `MultipartFile` e outras operações relacionadas a arquivos.
 *
 * <p>Esta classe não pode ser instanciada.</p>
 */
public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("This is a utility class FileUtils and cannot be instantiated");
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    /**
     * Carrega o conteúdo de um arquivo de configuração a partir do caminho dos recursos (classpath)
     * e o converte para uma String utilizando o charset UTF-8.
     *
     * @param fileName o nome ou caminho relativo do arquivo de configuração nos recursos
     * @return o conteúdo do arquivo em formato de String
     * @throws FileSystemException se o arquivo não for encontrado ou se ocorrer qualquer erro
     *                             durante a leitura do fluxo de dados (InputStream)
     */
    public static String loadConfigFile(final String fileName) throws FileSystemException {
        try (InputStream inputStream = getResourceAsStream(fileName)) {

            if (Objects.isNull(inputStream)) {
                System.out.printf("Arquivo não encontrado: " + fileName);
                throw new FileSystemException("Arquivo não encontrado: " + fileName);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.printf("Falha ao ler o arquivo de recursos: %s".formatted(e.getMessage()));
            throw new FileSystemException("Falha ao ler o arquivo de recursos: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Verifica se o conteúdo informado possui um formato JSON válido.
     *
     * <p>O método considera inválido um conteúdo {@code null} ou em branco.
     * Para os demais casos, realiza o parsing do conteúdo utilizando o
     * {@code GSON}. Caso ocorra um erro de sintaxe durante o parsing,
     * o conteúdo é considerado inválido.</p>
     *
     * @param jsonStr conteúdo em formato {@code String} que será validado
     * @return {@code true} se o conteúdo for um JSON válido;
     * {@code false} caso seja nulo, vazio, em branco ou apresente
     * erro de sintaxe
     */
    public static boolean isValidJson(String jsonStr) {
        if (jsonStr == null || jsonStr.isBlank()) {
            System.err.println("Is content file json invalid");
            return false;
        }

        try {
            final var obj = GSON.fromJson(jsonStr, Object.class);
            System.out.println(obj);
            return true;
        } catch (JsonSyntaxException e) {
            System.err.printf("Is content file json invalid e/or failed: %s.%n", e.getMessage());
            return false;
        }
    }

    /**
     * Converte uma lista de mapas simplificados para uma string JSON usando GSON.
     *
     * @param simplifiedItems A lista de mapas contendo os itens a serem convertidos.
     * @return Uma string JSON representando os itens fornecidos.
     */
    public static String toStringJsonFromGSONBy(List<Map<String, Object>> simplifiedItems) {
        return GSON.toJson(simplifiedItems);
    }

    /**
     * Converte uma classe para uma string JSON.
     *
     * @param clazz A classe a ser convertida.
     * @return Uma string JSON representando a classe fornecida.
     */
    public static String toStringJson(Class<?> clazz) {
        try {
            return MAPPER.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            final var errorMessage = "Failed create e/or convert to JSON string object from value: %s".formatted(e.getMessage());
            System.out.printf(errorMessage);
            throw new IllegalStateException(errorMessage, e);
        }
    }

    /**
     * Converte um objeto para uma string JSON.
     *
     * @param object O objeto a ser convertido.
     * @return Uma string JSON representando o objeto fornecido.
     */
    public static String toStringJsonFrom(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            final var errorMessage = "Failed create e/or convert to JSON string object by value: %s".formatted(e.getMessage());
            System.out.printf(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Converte o conteúdo JSON de um arquivo para uma instância do tipo informado.
     *
     * <p>O conteúdo recebido é convertido utilizando o {@code ObjectMapper}
     * configurado em {@code MAPPER}. O tipo de destino é construído a partir
     * da classe informada no parâmetro {@code clazz}.</p>
     *
     * <p>Caso ocorra qualquer erro durante a conversão, uma
     * {@link IllegalStateException} é lançada contendo a mensagem do erro
     * original.</p>
     *
     * @param contentFile conteúdo JSON que será convertido para o objeto de destino
     * @param clazz       classe do objeto de destino
     * @param <T>         tipo do objeto que será retornado
     * @return uma instância de {@code T} criada a partir do conteúdo JSON informado
     * @throws IllegalStateException caso ocorra um erro durante a conversão
     */
    public static <T> T toOjectFromFileContentJSON(String contentFile,
                                                   Class<T> clazz) {
        try {
            final JavaType javaType = MAPPER.getTypeFactory()
                    .constructType(clazz);

            return MAPPER.convertValue(contentFile, javaType);
        } catch (Exception e) {
            System.out.printf("Failed create e/or convert to object from JSON content value: %s", e.getMessage());
            throw new IllegalStateException(String.format("Failed create e/or convert to object from JSON content value: %s", e.getMessage()));
        }
    }

    /**
     * Converte o conteúdo XML de um arquivo para uma instância do tipo informado.
     *
     * <p>A conversão é realizada utilizando o {@code XML_MAPPER}, que é
     * responsável por desserializar o conteúdo XML e criar uma instância
     * da classe de destino especificada.</p>
     *
     * <p>Caso ocorra qualquer erro durante a criação do tipo ou durante a
     * desserialização do conteúdo XML, uma {@link IllegalStateException}
     * será lançada contendo a mensagem do erro ocorrido.</p>
     *
     * @param contentFile conteúdo XML que será convertido para o objeto de destino
     * @param clazz       classe do objeto de destino
     * @param <T>         tipo do objeto que será retornado
     * @return uma instância de {@code T} criada a partir do conteúdo XML informado
     * @throws IllegalStateException caso ocorra um erro durante a conversão
     *                               ou desserialização do conteúdo XML
     */
    public static <T> T toOjectFromFileContentXml(String contentFile,
                                                  Class<T> clazz) {
        try {
            final JavaType javaType = XML_MAPPER.getTypeFactory()
                    .constructType(clazz);

            return XML_MAPPER.readValue(contentFile, javaType);
        } catch (Exception e) {
            System.out.printf("Failed create e/or convert to object from XML content value: %s", e.getMessage());
            throw new IllegalStateException(String.format("Failed create e/or convert to object object from XML content value: %s", e.getMessage()));
        }
    }

    /**
     * Converte o conteúdo de uma String em formato XML para um objeto do tipo especificado.
     *
     * @param <T>         o tipo do objeto de destino
     * @param contentFile a String contendo o XML a ser convertido
     * @param clazz       a classe do objeto que será gerado a partir do XML
     * @return a instância do objeto populada com os dados do XML
     * @throws IllegalStateException se ocorrer falha ao instanciar o contexto JAXB ou realizar o unmarshal
     */
    public static <T> T toObjectFromFileContentXmlBy(String contentFile,
                                                     Class<T> clazz) {
        try {
            var jaxbContext = JAXBContext.newInstance(clazz);
            var unmarshaller = jaxbContext.createUnmarshaller();

            try (var stringReader = new StringReader(contentFile)) {
                return clazz.cast(unmarshaller.unmarshal(stringReader));
            }
        } catch (Exception e) {
            String errorMessage = String.format("Failed to create and/or convert object from XML content value: %s", e.getMessage());
            System.out.printf(errorMessage);
            throw new IllegalStateException(errorMessage, e);
        }
    }

    /**
     * Gera um arquivo CSV no caminho especificado a partir de uma lista de dados.
     *
     * @param filePath O caminho onde o arquivo CSV será gerado.
     * @param data     A lista de arrays de strings que representam as linhas e colunas do arquivo CSV.
     * @throws FileSystemException Se ocorrer um erro ao gerar o arquivo CSV.
     */
    public static void generateCsvFile(String filePath,
                                       List<String[]> data) throws FileSystemException {
        try (var csvWriter = new CSVWriter(new FileWriter(filePath))) {
            csvWriter.writeAll(data);
        } catch (IOException e) {
            System.out.printf("Error while generating CSV file: " + e.getMessage());
            throw new FileSystemException("Error while generating CSV file: " + e.getMessage());
        }
    }

    /**
     * Gera um arquivo CSV em formato de array de bytes a partir de uma lista de dados.
     *
     * @param data A lista de arrays de strings que representam as linhas e colunas do arquivo CSV.
     * @return Um array de bytes contendo o conteúdo do arquivo CSV gerado.
     * @throws FileSystemException Se ocorrer um erro ao gerar o conteúdo do arquivo CSV.
     */
    public static byte[] generateCsvFile(List<String[]> data) throws FileSystemException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             var csvWriter = new CSVWriter(
                     new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)
             )
        ) {
            csvWriter.writeAll(data);
            csvWriter.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.out.printf("Error while generating CSV content: " + e.getMessage());
            throw new FileSystemException("Error while generating CSV content: " + e.getMessage());
        }
    }

    /**
     * Obtém um fluxo de entrada (InputStream) para ler um recurso a partir do carregador de classes (ClassLoader)
     * associado à thread atual.
     *
     * @param fileName o nome ou caminho do recurso a ser buscado
     * @return um {@link InputStream} para o recurso, ou {@code null} se o recurso não for encontrado
     */
    public static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);
    }

    /**
     * Recupera o carregador de classes (ClassLoader) atualmente associado ao contexto da thread em execução.
     *
     * @return o {@link ClassLoader} do contexto da thread atual
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread()
                .getContextClassLoader();
    }

    /**
     * Obtém o caminho da pasta de recursos padrão do projeto.
     * <p>
     * Este método tenta localizar a pasta de recursos padrão utilizando o classloader
     * do contexto atual. Caso a pasta não seja encontrada, uma exceção será lançada.
     *
     * @return O caminho da pasta de recursos padrão como um objeto `Path`.
     * @throws IllegalStateException Se a pasta de recursos não for encontrada.
     */
    public static Path getDefaultResourcesFolderPath() {
        try {
            var resourceUrl = getClassLoader().getResource(StringUtils.EMPTY);

            if (resourceUrl != null) {
                Path resourcePath = Paths.get(resourceUrl.getPath());
                System.out.println("Default resources folder: " + resourcePath.toAbsolutePath());
                return resourcePath;
            }

            System.err.println("Resources folder not found.");
            throw new IllegalStateException("Failed/error Resources folder not found.");
        } catch (Exception e) {
            System.err.println("Resources folder not found.");
            throw new IllegalStateException("Resources folder not found: " + e.getMessage());
        }
    }

    /**
     * Obtém uma lista com os nomes dos arquivos em um diretório especificado.
     *
     * @param pathName O caminho do diretório onde os nomes dos arquivos serão listados.
     * @return Uma lista de strings contendo os nomes dos arquivos no diretório.
     */
    public static List<String> getFileNameList(String pathName) {
        var fileNameList = new ArrayList<String>();
        var directoryPath = Paths.get(pathName);

        try (Stream<Path> paths = Files.list(directoryPath)) {
            paths.forEach(path -> fileNameList.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
            throw new IllegalStateException("Error reading directory: " + e.getMessage());
        }

        return fileNameList;
    }

    /**
     * Cria uma pasta no caminho especificado, caso ela não exista.
     *
     * @param destinationFolderSaveFile O caminho do diretório onde a nova pasta será criada.
     * @param newFolderName             O nome da nova pasta a ser criada.
     * @return O caminho absoluto da pasta criada ou existente.
     */
    public static Path createFolderIfNotExists(String destinationFolderSaveFile,
                                               String newFolderName) {

        var folderPath = Paths.get(destinationFolderSaveFile + newFolderName);
        var absolutePath = folderPath.toAbsolutePath();

        if (!Files.exists(absolutePath)) {
            System.out.println("Folder does not exist: " + absolutePath);
            if (folderPath.toFile().mkdir() && Files.exists(absolutePath) && Files.isDirectory(absolutePath)) {
                System.out.println("Folder found at: " + absolutePath);
            }
        }

        return absolutePath;
    }

    /**
     * Exclui uma pasta ou arquivo no caminho especificado.
     *
     * @param pathFileName O caminho completo do arquivo ou pasta a ser excluído.
     *                     Deve ser um caminho válido e acessível no sistema de arquivos.
     */
    public static void deleteFolder(String pathFileName) {
        try {
            var filePath = Paths.get(pathFileName);
            Files.delete(filePath);
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }
}
