package br.com.daniel.java.quarkus.general.core.port;

import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define o contrato para operações relacionadas a URLs encurtadas.
 */
public interface ShortenUrlPort {

    /**
     * Cria uma nova URL encurtada.
     *
     * @param shortenUrl Objeto contendo os dados da URL encurtada a ser criada.
     * @return A URL encurtada criada.
     */
    ShortenUrl create(ShortenUrl shortenUrl);

    /**
     * Retorna uma lista com todas as URLs encurtadas.
     *
     * @return Lista de URLs encurtadas.
     */
    List<ShortenUrl> getAll();

    /**
     * Busca uma URL encurtada pelo seu identificador único.
     *
     * @param id Identificador único da URL encurtada.
     * @return Um Optional contendo a URL encurtada, caso encontrada.
     */
    Optional<ShortenUrl> findById(Long id);

    /**
     * Busca uma URL encurtada pelo link encurtado.
     *
     * @param shortenUrlLink Link encurtado da URL.
     * @return Um Optional contendo a URL encurtada, caso encontrada.
     */
    Optional<ShortenUrl> findByShortenUrlLink(String shortenUrlLink);
}