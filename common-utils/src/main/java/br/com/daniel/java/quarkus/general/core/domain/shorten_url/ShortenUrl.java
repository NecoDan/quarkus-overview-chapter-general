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

    private static final Integer DAYS_TO_EXPIRE = 1;

    private Long id;
    private String originalUrlLink;
    private String shortenUrlLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;

    public ShortenUrl(String originalUrl) {
        this.originalUrlLink = originalUrl;
        this.createdAt = LocalDateTime.now().plusDays(DAYS_TO_EXPIRE);
        this.active = Boolean.TRUE;
    }
}
