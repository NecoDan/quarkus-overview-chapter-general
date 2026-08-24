package br.com.daniel.java.quarkus.general.core.domain;

import lombok.Getter;

import java.util.Random;
import java.util.stream.Stream;

@Getter
public enum TypeHeroGroup {

    MARVEL_VINGADORES(1, "Vingadores"),
    DC_LIGA_JUSTICA(2, "Liga da Justiça");

    private final int code;
    private final String description;
    private static final Random RANDOM = new Random();

    TypeHeroGroup(int codigo, String description) {
        this.code = codigo;
        this.description = description;
    }

    public static TypeHeroGroup of(int code) {
        return getStreamValues()
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código inválido e/ou inexistente para o Tipo Grupo Herois."));
    }

    public static TypeHeroGroup randomTypeHeroGroup() {
        return getStreamValues()
                .skip((int) (Math.random() * getStreamValues().count()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível gerar um Tipo Grupo Herois aleatório."));
    }

    public boolean isAvengers() {
        return this == MARVEL_VINGADORES;
    }

    public boolean isJusticeLeague() {
        return this == DC_LIGA_JUSTICA;
    }

    private static Stream<TypeHeroGroup> getStreamValues() {
        return Stream.of(TypeHeroGroup.values());
    }

}
