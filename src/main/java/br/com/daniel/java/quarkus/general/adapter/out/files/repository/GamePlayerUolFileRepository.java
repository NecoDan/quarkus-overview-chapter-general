package br.com.daniel.java.quarkus.general.adapter.out.files.repository;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.exceptions.EntityCreateFailedException;
import br.com.daniel.java.quarkus.general.exceptions.InfraConfigFailedException;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolNotFoundException;
import br.com.daniel.java.quarkus.general.utils.FileUtils;
import br.com.daniel.java.quarkus.general.utils.FunctionalUtils;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Repositório responsável pela persistência de jogadores do desafio UOL
 * utilizando um arquivo CSV como mecanismo de armazenamento.
 *
 * <p>A classe realiza as operações de criação e consulta dos registros,
 * mantendo os dados em um arquivo temporário criado durante a inicialização
 * da aplicação.</p>
 *
 * <p>Antes da persistência, o identificador do jogador é gerado e os dados
 * derivados da entidade, como o número de telefone criptografado e o código
 * do grupo, são definidos.</p>
 *
 * <p>Os dados são serializados para o formato CSV e, durante a leitura,
 * são convertidos novamente para {@link GamePlayerUolEntity}.</p>
 *
 * <p>Em caso de falha na configuração do arquivo, criação do registro ou
 * interpretação dos dados armazenados, exceções específicas da aplicação
 * são lançadas.</p>
 *
 * @author Daniel Santos
 * @since 1.0
 */
@Singleton
@Slf4j
public class GamePlayerUolFileRepository {

    private static final String PATH_NAME_FILE_DB = "data-db-file";
    private static final String FILE_NAME = "file_tb_uol_gameplayers";
    private static final Path fileSavedData;

    static {
        try {
            fileSavedData = Files.createTempFile(Files.createTempDirectory(PATH_NAME_FILE_DB), FILE_NAME, ".csv");

            System.out.println("Diretorio/arquivo pra salvar os dados criado com sucesso: " + fileSavedData.toAbsolutePath());
            log.info("Diretorio/arquivo pra salvar os dados criado com sucesso: {}", fileSavedData.toAbsolutePath());
        } catch (IOException e) {
            final var errorMessage = "Falha ao criar diretorio/arquivo pra salvar os dados: " + e.getMessage();
            System.err.println(errorMessage);

            log.error(errorMessage);
            throw new InfraConfigFailedException(errorMessage);
        }
    }

    /**
     * Salva um novo jogador no arquivo CSV.
     *
     * <p>Durante o processo de persistência, o método gera um identificador
     * para a entidade, define o telefone criptografado e calcula o código
     * do grupo antes de armazenar os dados.</p>
     *
     * @param entity entidade do jogador que será persistida
     * @throws EntityCreateFailedException quando ocorre uma falha durante
     *                                     a criação ou persistência do registro
     */
    public void save(GamePlayerUolEntity entity) {
        log.info("UOL_CHALLENGE - Salvar entity via arquivo. Dados: {}", entity);

        try {
            entity.setId(RandomUtils.secureStrong().randomLong());
            entity.defineEncryptedPhoneNumber();
            entity.defineGroupCodeInt();

            saveFinally(entity);
            log.info("Arquivo salvo com sucesso: {}", fileSavedData.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Falha ao criar arquivo e salvar os dados: " + e.getMessage());
            log.error("Falha ao criar arquivo e salvar os dados: " + e.getMessage());
            throw new EntityCreateFailedException("Falha ao criar arquivo e salvar os dados: " + e.getMessage());
        }
    }

    /**
     * Recupera todos os jogadores armazenados no arquivo CSV.
     *
     * <p>Caso o arquivo ainda não possua registros ou não possa ser
     * disponibilizado para leitura, uma lista vazia é retornada.</p>
     *
     * @return lista contendo todos os jogadores persistidos
     * @throws ParseEntityFailedException quando ocorre uma falha ao
     *                                    interpretar os dados do arquivo CSV
     */
    public List<GamePlayerUolEntity> findAll() {
        try {
            log.info("UOL_CHALLENGE - Recuperar todos os registros salvo(s) via arquivo.");
            return readCsvAndGetAllElementsEntity();
        } catch (Exception e) {
            System.err.println("Erro ao carregar/obter dados via arquivo csv: " + e.getMessage());
            log.error("Erro ao carregar/obter dados via arquivo csv: " + e.getMessage());
            throw new GamePlayerUolNotFoundException("Erro ao carregar/obter dados via arquivo csv: " + e.getMessage());
        }
    }

    /**
     * Recupera um registro do jogador pelo seu identificador único a partir do arquivo CSV.
     * <p>
     * O método lê os dados do arquivo CSV e realiza uma busca pelo ID fornecido.
     * </p>
     *
     * @param id Identificador único do jogador a ser buscado.
     * @return Um {@link Optional} contendo a entidade {@link GamePlayerUolEntity} caso seja encontrada,
     * ou um {@link Optional#empty()} caso o ID não corresponda a nenhum registro.
     * @throws GamePlayerUolNotFoundException Se ocorrer qualquer falha na leitura ou no processamento
     *                                        dos dados do arquivo CSV.
     */
    public Optional<GamePlayerUolEntity> findById(Long id) {
        log.info("UOL_CHALLENGE - Recuperar registro salvo(s) via arquivo by ID: {}", id);

        try {
            return readCsvAndGetAllElementsEntity()
                    .stream()
                    .filter(entity -> Objects.equals(entity.getId(), id))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Falha ao carregar/obter dados via arquivo csv: " + e.getMessage());
            log.error("Falha ao carregar/obter dados via arquivo csv: " + e.getMessage());
            throw new GamePlayerUolNotFoundException("Falha ao carregar/obter dados via arquivo csv: " + e.getMessage());
        }
    }

    /**
     * Recupera a lista de codinomes já cadastrados para um determinado grupo de heróis a partir do arquivo CSV.
     * <p>
     * O método lê o arquivo CSV, filtra os registros correspondentes ao código do grupo informado
     * e extrai apenas os codinomes associados.
     * </p>
     *
     * @param typeHeroGroup Grupo de heróis (ex: Vingadores, Liga da Justiça) utilizado como filtro na busca.
     * @return Uma {@link List} de {@link String} contendo todos os codinomes existentes para o grupo informado.
     * @throws GamePlayerUolNotFoundException Se ocorrer qualquer falha na leitura ou no processamento
     *                                        dos dados do arquivo CSV.
     */
    public List<String> findListExistingCodenames(TypeHeroGroup typeHeroGroup) {
        log.info("UOL_CHALLENGE - Recuperando a lista de codinomes salva(s) via arquivo.");

        try {
            return readCsvAndGetAllElementsEntity()
                    .stream()
                    .filter(entity -> Objects.equals(entity.getGroupCodeInt(), typeHeroGroup.getCode()))
                    .map(GamePlayerUolEntity::getCodeName)
                    .toList();
        } catch (Exception e) {
            System.err.println("Erro encontrado ao carregar/obter dados via arquivo csv: " + e.getMessage());
            log.error("Erro encontrado ao carregar/obter dados via arquivo csv: " + e.getMessage());
            throw new GamePlayerUolNotFoundException("Erro encontrado ao carregar/obter dados via arquivo csv: " + e.getMessage());
        }
    }

    public List<GamePlayerUolEntity> readCsvAndGetAllElementsEntity() {
        List<GamePlayerUolEntity> entities = new ArrayList<>();

        try (var reader = Files.newBufferedReader(fileSavedData);
             var parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {
            for (CSVRecord csvRecord : parser) {
                entities.add(getBuildEntityValueFrom(csvRecord));
            }

            return entities;
        } catch (IOException e) {
            System.err.println("Falha ao carregar e obter os dados do arquivo csv: " + e.getMessage());
            log.error("Falha ao carregar e obter os dados do arquivo csv: " + e.getMessage());
            throw new ParseEntityFailedException("Falha ao carregar e obter os dados do arquivo csv: " + e.getMessage());
        }
    }

    private GamePlayerUolEntity getBuildEntityValueFrom(CSVRecord csvRecord) {
        return GamePlayerUolEntity.builder()
                .id(Long.parseLong(csvRecord.get("id")))
                .name(csvRecord.get("nome"))
                .email(csvRecord.get("email"))
                .encryptedPhoneNumber(csvRecord.get("telefone"))
                .codeName(csvRecord.get("codinome"))
                .groupCodeInt(Integer.parseInt(csvRecord.get("codigo_grupo")))
                .indicadorAtivo(Boolean.parseBoolean(csvRecord.get("ativo")))
                .createdAt(FunctionalUtils.onlyLocalDateTimeBy(csvRecord.get("dt_criacao"), FunctionalUtils.BR_DATETIME_FORMAT))
                .updateAt(FunctionalUtils.onlyLocalDateTimeBy(csvRecord.get("dt_atualizacao"), FunctionalUtils.BR_DATETIME_FORMAT))
                .build();
    }

    private void saveFinally(GamePlayerUolEntity entity) throws IOException {

        var allEntityList = findAll();
        final var fileNameCsv = fileSavedData.toAbsolutePath().toString();

        if (isFileExisteAndListEntityEmpty(allEntityList)) {
            FileUtils.generateCsvFile(fileNameCsv, buildDataForCsv(List.of(entity)));
            return;
        }

        allEntityList.addLast(entity);
        FileUtils.generateCsvFile(fileNameCsv, buildDataForCsv(allEntityList));
    }

    private boolean isFileExisteAndListEntityEmpty(List<GamePlayerUolEntity> allEntityList) {
        return Files.exists(GamePlayerUolFileRepository.fileSavedData) && CollectionUtils.isEmpty(allEntityList);
    }

    private List<String[]> buildDataForCsv(List<GamePlayerUolEntity> entityList) {
        List<String[]> data = new ArrayList<>();
        data.add(getColumns());

        entityList.forEach(
                entityLine -> data.add(
                        new String[]{
                                String.valueOf(entityLine.getId()),
                                entityLine.getName(),
                                entityLine.getEmail(),
                                entityLine.getEncryptedPhoneNumber(),
                                entityLine.getCodeName(),
                                Objects.isNull(entityLine.getGroupCodeInt()) ? StringUtils.EMPTY : String.valueOf(entityLine.getGroupCodeInt()),
                                Boolean.toString(entityLine.isIndicadorAtivo()),
                                Objects.isNull(entityLine.getCreatedAt()) ? StringUtils.EMPTY : FunctionalUtils.formatCreationDate(entityLine.getCreatedAt()),
                                Objects.isNull(entityLine.getUpdateAt()) ? StringUtils.EMPTY : FunctionalUtils.formatCreationDate(entityLine.getUpdateAt())}
                )
        );

        return data;
    }

    private String[] getColumns() {
        return new String[]{"id", "nome", "email", "telefone", "codinome", "codigo_grupo", "ativo", "dt_criacao", "dt_atualizacao"};
    }
}
