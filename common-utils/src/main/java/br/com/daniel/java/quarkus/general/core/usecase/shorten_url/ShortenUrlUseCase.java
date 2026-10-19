package br.com.daniel.java.quarkus.general.core.usecase.shorten_url;

import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ShortenUrlUseCase {
    ShortenUrlOutputSaved createShortenUrlV1(String originalUrl);

    ShortenUrlOutputSaved createShortenUrlV2(String originalUrl);

    @Transactional
    ShortenUrlOutputSaved createShortenUrlV3(String originalUrl);

    List<ShortenUrlOutput> getAllShortenUrls();
}
