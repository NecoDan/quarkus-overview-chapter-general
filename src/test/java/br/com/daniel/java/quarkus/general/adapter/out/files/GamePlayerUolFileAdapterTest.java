package br.com.daniel.java.quarkus.general.adapter.out.files;

import br.com.daniel.java.quarkus.general.adapter.out.files.repository.GamePlayerUolFileRepository;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.util.factory.GamePlayerUolFactory;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("GamePlayerUolFileAdapterTest")
class GamePlayerUolFileAdapterTest {


    @Inject
    GamePlayerUolFileRepository repository;

    @Inject
    GamePlayerUolFileAdapter adapter;

    private final TypeHeroGroup typeHeroGroup = TypeHeroGroup.MARVEL_VINGADORES;

//    @Test
    @Order(1)
    @DisplayName("Deve retornar lista vazia quando o repositório não possuir jogadores salvos")
    void shouldReturnEmptyListWhenNoPlayersExist() {
        // -- 01_Cenário_&_02_Ação
        var resultListAll = adapter.findAll();

        // -- 03_Verificação_Validação
        assertNotNull(resultListAll);
        assertTrue(resultListAll.isEmpty());
    }

    //    @Test
    @Order(2)
    @DisplayName("Deve converter o objeto de domínio para entidade e chamar o método save do repositório")
    void shouldSaveGamePlayerSuccessfully() {
        // -- 01_Cenário
        var gamePlayerUol = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);

        // -- 02_Ação
        adapter.salvarGamePlayer(gamePlayerUol);

        // -- 03_Verificação_Validação
        final var resultListAll = adapter.findAll();
        assertNotNull(resultListAll);
        assertFalse(resultListAll.isEmpty());

        final var optionalGamePlayerUol = resultListAll.stream().findFirst();
        assertNotNull(optionalGamePlayerUol);
        final var gamePlayerUolResult = optionalGamePlayerUol.get();

        assertNotNull(gamePlayerUolResult.getId());
        assertNotEquals(0L, gamePlayerUolResult.getId());
        assertEquals(gamePlayerUol.getName(), gamePlayerUolResult.getName());
        assertEquals(gamePlayerUol.getEmail(), gamePlayerUolResult.getEmail());
        assertEquals(gamePlayerUol.getGroupCode().getCode(), gamePlayerUolResult.getGroupCode().getCode());
        assertEquals(gamePlayerUol.getCodeName(), gamePlayerUolResult.getCodeName());
    }

    //    @Test
    @Order(3)
    @DisplayName("Deve retornar lista de jogadores mapeados do domínio com sucesso")
    void shouldReturnMappedPlayerList() {
        // -- 01_Cenário
        var gamePlayerUolEntityVar1 = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);
        final var expectedNameVar1 = gamePlayerUolEntityVar1.getName();
        adapter.salvarGamePlayer(gamePlayerUolEntityVar1);

        var gamePlayerUolEntityVar2 = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);
        final var expectedNameVar2 = gamePlayerUolEntityVar2.getName();
        adapter.salvarGamePlayer(gamePlayerUolEntityVar2);

        // -- 02_Ação
        var resultListAll = adapter.findAll();

        // -- 03_Verificação_Validação
        assertNotNull(resultListAll);
        assertFalse(resultListAll.isEmpty());

        assertTrue(
                resultListAll.stream()
                        .anyMatch(entity ->
                                expectedNameVar1.equals(entity.getName()))
        );

        assertTrue(
                resultListAll.stream()
                        .anyMatch(entity ->
                                expectedNameVar2.equals(entity.getName()))
        );
    }

    //    @Test
    @Order(4)
    @DisplayName("Deve retornar lista de codinomes cadastrados quando o repositório possuir dados")
    void shouldReturnCodenamesListWhenRepositoryHasData() {
        // -- 01_Cenário
        var gamePlayerUolEntityVar10 = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);
        final var codeNameVar1 = gamePlayerUolEntityVar10.getCodeName();
        adapter.salvarGamePlayer(gamePlayerUolEntityVar10);

        var gamePlayerUolEntityVar11 = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);
        final var codeNameVar2 = gamePlayerUolEntityVar11.getCodeName();
        adapter.salvarGamePlayer(gamePlayerUolEntityVar11);

        var gamePlayerUolEntityVar12 = GamePlayerUolFactory.buildMockGamePlayerUolBy(typeHeroGroup);
        final var codeNameVar3 = gamePlayerUolEntityVar12.getCodeName();
        adapter.salvarGamePlayer(gamePlayerUolEntityVar12);

        // -- 02_Ação
        var resultListAllCodenames = adapter.findListExistingCodenames(typeHeroGroup);

        // -- 03_Verificação_Validação
        assertNotNull(resultListAllCodenames);
        assertFalse(resultListAllCodenames.isEmpty());

        assertTrue(
                resultListAllCodenames.stream()
                        .anyMatch(stringValue ->
                                stringValue.equals(codeNameVar1))
        );

        assertTrue(
                resultListAllCodenames.stream()
                        .anyMatch(stringValue ->
                                stringValue.equals(codeNameVar2))
        );

        assertTrue(
                resultListAllCodenames.stream()
                        .anyMatch(stringValue ->
                                stringValue.equals(codeNameVar3))
        );
    }

    //    @Test
    @Order(5)
    @DisplayName("Deve retornar lista vazia quando não houver codinomes para o grupo")
    void shouldReturnEmptyListWhenNoCodenamesFound() {
        // -- 01_Cenário_&_02_Ação
        var listExistingCodenames = adapter.findListExistingCodenames(TypeHeroGroup.DC_LIGA_JUSTICA);

        // -- 03_Verificação_Validação
        assertNotNull(listExistingCodenames);
        assertTrue(listExistingCodenames.isEmpty());
    }
}