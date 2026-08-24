package br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record GamePlayerReportOutput(@JsonProperty("nome") String name,
                                     @JsonProperty("email") String mail,
                                     @JsonProperty("telefone") String fone,
                                     @JsonProperty("codinome") String codename,
                                     @JsonProperty("arquivoReferencia") String groupHeroDescription
) {
    public static GamePlayerReportOutput from(GamePlayerUol gamePlayerUol) {
        return new GamePlayerReportOutput(gamePlayerUol.getName(),
                gamePlayerUol.getEmail(),
                gamePlayerUol.getRawPhoneNumber(),
                gamePlayerUol.getCodeName(),
                gamePlayerUol.getGroupCode().getDescription()
        );
    }
}
