package br.com.daniel.java.quarkus.general.core.usecase.shorten_url;

import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Interface que define os casos de uso relacionados ao encurtamento de URLs.
 */
public interface ShortenUrlUseCase {

    /**
     * Cria uma URL encurtada utilizando a versão 1 do algoritmo.
     *
     * @param originalUrl A URL original que será encurtada.
     * @return Um objeto contendo os dados da URL encurtada.
     */
    ShortenUrlOutputSaved createShortenUrlV1(String originalUrl);

    /**
     * Cria uma URL encurtada utilizando a versão 2 do algoritmo.
     *
     * @param originalUrl A URL original que será encurtada.
     * @return Um objeto contendo os dados da URL encurtada.
     */
    ShortenUrlOutputSaved createShortenUrlV2(String originalUrl);

    /**
     * Cria uma URL encurtada utilizando a versão 3 do algoritmo.
     * Este método é transacional.
     *
     * @param originalUrl A URL original que será encurtada.
     * @return Um objeto contendo os dados da URL encurtada.
     */
    @Transactional
    ShortenUrlOutputSaved createShortenUrlV3(String originalUrl);

    /**
     * Recupera todas as URLs encurtadas.
     *
     * @return Uma lista contendo os dados de todas as URLs encurtadas.
     */
    List<ShortenUrlOutput> getAllShortenUrls();

    ShortenUrlOutput getByShortenUrlLink(String shortenUrlLink);
}