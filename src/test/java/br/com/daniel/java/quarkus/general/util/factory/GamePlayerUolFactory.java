package br.com.daniel.java.quarkus.general.util.factory;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class GamePlayerUolFactory {

    private GamePlayerUolFactory() {
        throw new IllegalStateException("Utility class GamePlayerUolEntityFactory");
    }

    private static final Faker FAKER_INSTANCE = Faker.instance();

    public static GamePlayerUolEntity buildMockGamePlayerUolEntity() {
        final var typeHeroGroup = TypeHeroGroup.randomTypeHeroGroup();

        return GamePlayerUolEntity.builder()
                .name(FAKER_INSTANCE.name().fullName())
                .email(FAKER_INSTANCE.internet().emailAddress())
                .rawPhoneNumber(FAKER_INSTANCE.phoneNumber().phoneNumber())
                .groupCode(typeHeroGroup)
                .codeName(getBuildRandomMockCodeName(typeHeroGroup))
                .indicadorAtivo(Boolean.TRUE)
                .createdAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .updateAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .build();
    }

    public static GamePlayerUol buildMockGamePlayerUol() {
        final var typeHeroGroup = TypeHeroGroup.randomTypeHeroGroup();

        return GamePlayerUol.builder()
                .name(FAKER_INSTANCE.name().fullName())
                .email(FAKER_INSTANCE.internet().emailAddress())
                .rawPhoneNumber(FAKER_INSTANCE.phoneNumber().phoneNumber())
                .groupCode(typeHeroGroup)
                .codeName(getBuildRandomMockCodeName(typeHeroGroup))
                .indicadorAtivo(Boolean.TRUE)
                .createdAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .updateAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .build();
    }

    public static GamePlayerUol buildMockGamePlayerUolBy(TypeHeroGroup typeHeroGroup) {
        return GamePlayerUol.builder()
                .name(FAKER_INSTANCE.name().fullName())
                .email(FAKER_INSTANCE.internet().emailAddress())
                .rawPhoneNumber(FAKER_INSTANCE.phoneNumber().phoneNumber())
                .groupCode(typeHeroGroup)
                .codeName(getBuildRandomMockCodeName(typeHeroGroup))
                .indicadorAtivo(Boolean.TRUE)
                .createdAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .updateAt(LocalDateTime.now()
                        .atOffset(java.time.ZoneOffset.UTC)
                        .toLocalDateTime()
                )
                .build();
    }

    private static String getBuildRandomMockCodeName(TypeHeroGroup typeHeroGroup) {
        final var listCodeNames = typeHeroGroup.isAvengers()
                ? getAllListCodeNameAvengers()
                : getAllListCodeNameDCComics();

        return listCodeNames.stream()
                .skip(ThreadLocalRandom.current().nextInt(listCodeNames.size()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Erro ao criar mock codinome"));
    }

    private static List<String> getAllListCodeNameDCComics() {
        return List.of(
                "Superman",
                "Batman",
                "Mulher-Maravilha",
                "Flash",
                "Aquaman",
                "Lanterna Verde",
                "Caçador de Marte",
                "Ciborgue",
                "Shazam",
                "Arqueiro Verde",
                "Canário Negro",
                "Robin",
                "Batgirl",
                "Asa Noturna",
                "Coringa",
                "Lex Luthor",
                "Mulher-Gato",
                "Arlequina",
                "Sinestro",
                "Adão Negro"
        );
    }

    private static List<String> getAllListCodeNameAvengers() {
        return List.of(
                "Homem de Ferro",
                "Capitão América",
                "Thor",
                "Hulk",
                "Viúva Negra",
                "Gavião Arqueiro",
                "Homem-Aranha",
                "Pantera Negra",
                "Doutor Estranho",
                "Wolverine",
                "Jean Grey",
                "Ciclope",
                "Tempestade",
                "Magneto",
                "Professor X",
                "Deadpool",
                "Capitã Marvel",
                "Feiticeira Escarlate",
                "Visão",
                "Homem-Formiga");
    }
}
