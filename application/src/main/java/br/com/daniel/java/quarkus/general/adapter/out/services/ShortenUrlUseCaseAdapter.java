package br.com.daniel.java.quarkus.general.adapter.out.services;

import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.ShortenUrlUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.input.ShortenUrlInput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class ShortenUrlUseCaseAdapter {

    @Inject ShortenUrlUseCase shortenUrlUseCase;

    public ShortenUrlOutputSaved createV1(ShortenUrlInput shortenUrLInput) {
        log.info("[COMMONS_UTILS][v1] - Criando um novo link encurtado com os dados fornecidos no corpo da requisição.");
        return shortenUrlUseCase.createShortenUrlV1(shortenUrLInput.linkUrl());
    }

    public ShortenUrlOutputSaved createV2(ShortenUrlInput shortenUrLInput) {
        log.info("[COMMONS_UTILS][v2] - Criando um novo link encurtado com os dados fornecidos no corpo da requisição.");
        return shortenUrlUseCase.createShortenUrlV3(shortenUrLInput.linkUrl());
    }

    public List<ShortenUrlOutput> getAll() {
        log.info("[COMMONS_UTILS] - Buscando todos os links encurtados salvos.");
        return shortenUrlUseCase.getAllShortenUrls();
    }
}
