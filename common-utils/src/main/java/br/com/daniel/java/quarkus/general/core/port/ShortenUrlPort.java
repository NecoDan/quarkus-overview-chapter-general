package br.com.daniel.java.quarkus.general.core.port;

import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;

import java.util.List;
import java.util.Optional;

public interface ShortenUrlPort {
    ShortenUrl create(ShortenUrl shortenUrl);

    List<ShortenUrl> getAll();

    Optional<ShortenUrl> findById(Long id);

    Optional<ShortenUrl> findByShortenUrlLink(String shortenUrlLink);
}
