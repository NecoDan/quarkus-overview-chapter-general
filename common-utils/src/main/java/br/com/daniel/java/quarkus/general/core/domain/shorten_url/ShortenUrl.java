package br.com.daniel.java.quarkus.general.core.domain.shorten_url;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrl implements Serializable {

    private Long id;
    private String originalUrlLink;
    private String shortenUrlLink;
    private LocalDateTime expiredDtAt;
    private LocalDateTime updatedExpiredDtAt;
    private Boolean expired;
    private Integer daysToExpire;

    public ShortenUrl(String originalUrl, Integer daysToExpire) {
        this.originalUrlLink = originalUrl;
        this.expiredDtAt = LocalDateTime.now().plusDays(daysToExpire);
        this.expired = Boolean.FALSE;
        this.daysToExpire = daysToExpire;
    }
}
