package br.com.daniel.java.quarkus.general.adapter.out.services;

import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.ShortenUrlUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.input.ShortenUrlInput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Classe adaptadora responsável por intermediar as chamadas entre os casos de uso
 * relacionados ao encurtamento de URLs e os serviços externos.
 */
@ApplicationScoped
@Slf4j
public class ShortenUrlUseCaseAdapter {

    @Inject
    ShortenUrlUseCase shortenUrlUseCase;

    /**
     * Cria um novo link encurtado utilizando a versão 1 do caso de uso.
     *
     * @param shortenUrLInput Objeto contendo os dados necessários para criar o link encurtado.
     * @return Objeto contendo os dados do link encurtado salvo.
     */
    public ShortenUrlOutputSaved createV1(ShortenUrlInput shortenUrLInput) {
        log.info("[COMMONS_UTILS][v1] - Criando um novo link encurtado a partir da url de origem: {}.", shortenUrLInput);
        return shortenUrlUseCase.createShortenUrlV1(shortenUrLInput.linkUrl());
    }

    /**
     * Cria um novo link encurtado utilizando a versão 2 do caso de uso.
     *
     * @param shortenUrLInput Objeto contendo os dados necessários para criar o link encurtado.
     * @return Objeto contendo os dados do link encurtado salvo.
     */
    public ShortenUrlOutputSaved createV2(ShortenUrlInput shortenUrLInput) {
        log.info("[COMMONS_UTILS][v2] - Criando um novo link encurtado a partir da url de origem: {}.", shortenUrLInput);
        return shortenUrlUseCase.createShortenUrlV2(shortenUrLInput.linkUrl());
    }

    /**
     * Cria um novo link encurtado utilizando a versão 3 do caso de uso.
     *
     * @param shortenUrLInput Objeto contendo os dados necessários para criar o link encurtado.
     * @return Objeto contendo os dados do link encurtado salvo.
     */
    public ShortenUrlOutputSaved createV3(ShortenUrlInput shortenUrLInput) {
        log.info("[COMMONS_UTILS][v3] - Criando um novo link encurtado a partir da url de origem: {}.", shortenUrLInput);
        return shortenUrlUseCase.createShortenUrlV3(shortenUrLInput.linkUrl());
    }

    /**
     * Recupera todos os links encurtados salvos.
     *
     * @return Lista contendo os dados de todos os links encurtados salvos.
     */
    public List<ShortenUrlOutput> getAll() {
        log.info("[COMMONS_UTILS] - Buscando todos os links encurtados salvos.");
        return shortenUrlUseCase.getAllShortenUrls();
    }

    /**
     * Recupera os dados de um link encurtado com base no link encurtado fornecido.
     *
     * @param shortenUrlLink O link encurtado utilizado para buscar os dados.
     * @return Objeto contendo os dados do link encurtado correspondente.
     */
    public ShortenUrlOutput getByShortenUrlLink(final String shortenUrlLink) {
        log.info("[COMMONS_UTILS] - Buscando a link da url origem completa por meio do link encurtado: {}", shortenUrlLink);
        return shortenUrlUseCase.getByShortenUrlLink(shortenUrlLink);
    }
}