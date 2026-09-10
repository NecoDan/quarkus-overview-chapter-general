package br.com.daniel.java.quarkus.general.adapter.out.files.repository;

import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
import br.com.daniel.java.quarkus.general.exceptions.api.GamePlayerUolNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class GamePlayerUolFileRepositoryV2Test {
    @Spy private GamePlayerUolFileRepository repository;

    @Test
    @DisplayName("Deve lançar GamePlayerUolNotFoundException quando ocorrer um erro ao ler o CSV no findById")
    void shouldThrowExceptionWhenErrorOccursInFindById() {
        // -- 01_Cenário
        Long id = 1L;

        doThrow(new RuntimeException("Erro de leitura do arquivo"))
                .when(repository).readCsvAndGetAllElementsEntity();

        // -- 02_Ação_&_03_Verificação_Validação
        var gamePlayerUolNotFoundException = assertThrows(GamePlayerUolNotFoundException.class,
                () -> repository.findById(id)
        );

        assertTrue(gamePlayerUolNotFoundException.getMessage()
                .contains("Falha ao carregar/obter dados via arquivo csv")
        );
    }

    @Test
    @DisplayName("Deve lançar GamePlayerUolNotFoundException quando ocorrer erro no findListExistingCodenames")
    void shouldThrowExceptionWhenErrorOccursInFindCodenames() {
        // -- 01_Cenário
        var typeHeroGroup = TypeHeroGroup.MARVEL_VINGADORES;

        doThrow(new RuntimeException("Arquivo não encontrado"))
                .when(repository).readCsvAndGetAllElementsEntity();

        // -- 02_Ação_&_03_Verificação_Validação
        assertThrows(GamePlayerUolNotFoundException.class,
                () -> repository.findListExistingCodenames(typeHeroGroup)
        );
    }
}