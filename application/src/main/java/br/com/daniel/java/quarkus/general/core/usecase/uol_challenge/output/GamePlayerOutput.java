package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record GamePlayerOutput(@JsonProperty("respostaSucesso") String respostaSucesso
) {

    public static GamePlayerOutput from(String codeName,
                                        String groupCode) {
        return new GamePlayerOutput("Jogador %s do grupo %s, cadastrado com sucesso."
                .formatted(codeName, groupCode)
        );
    }
}
