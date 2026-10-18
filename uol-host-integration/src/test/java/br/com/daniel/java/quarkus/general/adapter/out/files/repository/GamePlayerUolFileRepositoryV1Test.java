package br.com.daniel.java.quarkus.general.adapter.out.files.repository;

import br.com.daniel.java.quarkus.general.util.factory.GamePlayerUolFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamePlayerUolFileRepositoryV1Test {

    private GamePlayerUolFileRepository repository;

    @BeforeEach
    void setUp() {
        repository = new GamePlayerUolFileRepository();
    }

    @Test
    void shouldSaveEntitySuccessfully() {
        // -- 01_Cenário
        final var entity = GamePlayerUolFactory.buildMockGamePlayerUolEntity();

        // -- 02_Ação
        repository.save(entity);

        // -- 03_Verificação_Validação
        assertNotNull(entity.getId());
        assertNotEquals(0L, entity.getId());

        final var resultListAll = repository.findAll();
        assertFalse(resultListAll.isEmpty());
        assertNotNull(resultListAll);

        final var entityOptional = repository.findById(entity.getId());
        assertNotNull(entityOptional);
        assertTrue(entityOptional.isPresent());

        final var gamePlayerUolEntityResult = entityOptional.get();
        assertEquals(entity.getName(), gamePlayerUolEntityResult.getName());
        assertEquals(entity.getEmail(), gamePlayerUolEntityResult.getEmail());
        assertEquals(entity.getGroupCodeInt(), gamePlayerUolEntityResult.getGroupCodeInt());
        assertEquals(entity.getCodeName(), gamePlayerUolEntityResult.getCodeName());
    }

    @Test
    void shouldFindAllSavedEntities() {
        // -- 01_Cenário
        final var firstEntityVar1 = GamePlayerUolFactory.buildMockGamePlayerUolEntity();
        final var expectedNameVar1 = firstEntityVar1.getName();
        repository.save(firstEntityVar1);

        final var secondEntityVar2 = GamePlayerUolFactory.buildMockGamePlayerUolEntity();
        final var expectedNameVar2 = firstEntityVar1.getName();
        repository.save(secondEntityVar2);

        // -- 02_Ação
        final var resultListAll = repository.findAll();

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

    @Test
    void shouldGenerateDifferentIdsForDifferentEntities() {
        // -- 01_Cenário
        final var firstEntityVar3 = GamePlayerUolFactory.buildMockGamePlayerUolEntity();
        final var secondEntityVar4 = GamePlayerUolFactory.buildMockGamePlayerUolEntity();

        // -- 02_Ação
        repository.save(firstEntityVar3);
        repository.save(secondEntityVar4);

        // -- 03_Verificação_Validação
        assertNotNull(firstEntityVar3.getId());
        assertNotNull(secondEntityVar4.getId());
        assertNotEquals(firstEntityVar3.getId(), secondEntityVar4.getId());
    }

    @Test
    void shouldPersistEntityDataCorrectly() {
        // -- 01_Cenário
        final var entityVar5 = GamePlayerUolFactory.buildMockGamePlayerUolEntity();

        // -- 02_Ação
        repository.save(entityVar5);
        final var resultListAll = repository.findAll();

        // -- 03_Verificação_Validação
        assertEquals(1, resultListAll.size());

        final var entityOptional = resultListAll.stream().findFirst();
        assertNotNull(entityOptional);
        assertTrue(entityOptional.isPresent());

        final var savedEntity = entityOptional.get();
        assertNotNull(savedEntity);

        assertEquals(entityVar5.getId(), savedEntity.getId());
        assertEquals(entityVar5.getName(), savedEntity.getName());
        assertEquals(entityVar5.getEmail(), savedEntity.getEmail());
        assertEquals(entityVar5.getCodeName(), savedEntity.getCodeName());
        assertEquals(
                entityVar5.getGroupCodeInt(),
                savedEntity.getGroupCodeInt()
        );
        assertEquals(
                entityVar5.getEncryptedPhoneNumber(),
                savedEntity.getEncryptedPhoneNumber()
        );
    }

    @Test
    void shouldReturnListWhenFindAllIsCalled() {
        // Act
        final var result = repository.findAll();

        // Assert
        assertNotNull(result);
    }
}