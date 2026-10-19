package br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output;

import br.com.daniel.java.quarkus.general.utils.ShortenUrlUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ShortenUrlOutput(@JsonProperty("id") Long id,
                               @JsonProperty("url_link_origem") String originalUrlLink,
                               @JsonProperty("url_link_encurtado") String shortenUrlLink,
                               @JsonProperty("data_criacao") LocalDateTime expiredDtAt,
                               @JsonProperty("data_atualizacao") LocalDateTime updatedExpiredDtAt,
                               @JsonProperty("expirado") Boolean expired) {
    @JsonIgnore
    public String getRedirectUrl() {
        return ensureUrlHasHttpPrefix(this.originalUrlLink);
    }

    private String ensureUrlHasHttpPrefix(String url) {
        if (!(url.startsWith(ShortenUrlUtils.HTTP_PREFIX) || url.startsWith(ShortenUrlUtils.HTTPS_PREFIX))) {
            url = ShortenUrlUtils.HTTP_PREFIX + url;
        }
        return url;
    }
}
