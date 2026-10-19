package br.com.daniel.java.quarkus.general.core.usecase.shorten_url;

import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;
import br.com.daniel.java.quarkus.general.core.mappers.ShortenUrlMapper;
import br.com.daniel.java.quarkus.general.core.port.ShortenUrlPort;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import br.com.daniel.java.quarkus.general.exceptions.ShortenUrlCreateFailedException;
import br.com.daniel.java.quarkus.general.exceptions.ShortenUrlNotFoundException;
import br.com.daniel.java.quarkus.general.utils.ShortenUrlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
@Slf4j
public class ShortenUrlUseCaseImpl implements ShortenUrlUseCase {

    private static final Integer SHORTEN_URL_LENGTH_MIN = 5;
    private static final Integer SHORTEN_URL_LENGTH_MAX = 11;

    @ConfigProperty(name = "shorten.url.daystoexpire.value")
    Integer daysToExpire;

    @Inject
    ShortenUrlPort shortenUrlPort;

    @Inject
    ShortenUrlMapper shortenUrlMapper;

    @Override
    @Transactional
    public ShortenUrlOutputSaved createShortenUrlV1(String originalUrl) {
        try {
            log.info("[COMMONS_UTILS][v1] Criando um novo link encurtado com o url link de origem: {}", originalUrl);

            var shortenUrl = new ShortenUrl(originalUrl, daysToExpire);
            shortenUrl.setShortenUrlLink(
                    ShortenUrlUtils.generateShortenUrlLink(SHORTEN_URL_LENGTH_MIN, SHORTEN_URL_LENGTH_MAX)
            );

            return finalizeAndSaved(originalUrl, shortenUrl);
        } catch (Exception e) {
            log.error("[COMMONS_UTILS][v1] Erro ao criar um novo link encurtado com o url link de origem: {}", originalUrl, e);
            throw new ShortenUrlCreateFailedException("Erro ao criar um novo link encurtado com o url link de origem: " + originalUrl, e);
        }
    }

    @Override
    @Transactional
    public ShortenUrlOutputSaved createShortenUrlV2(String originalUrl) {
        try {
            log.info("[COMMONS_UTILS][v2] Criando um novo link encurtado com o url link de origem: {}", originalUrl);

            var shortenUrl = new ShortenUrl(originalUrl, daysToExpire);
            shortenUrl.setShortenUrlLink(
                    ShortenUrlUtils.generateRandomKey(SHORTEN_URL_LENGTH_MIN, SHORTEN_URL_LENGTH_MAX)
            );

            return finalizeAndSaved(originalUrl, shortenUrl);
        } catch (Exception e) {
            log.error("[COMMONS_UTILS][v2] Erro ao criar um novo link encurtado com o url link de origem: {}", originalUrl, e);
            throw new ShortenUrlCreateFailedException("Erro ao criar um novo link encurtado com o url link de origem: " + originalUrl, e);
        }
    }

    @Override
    @Transactional
    public ShortenUrlOutputSaved createShortenUrlV3(String originalUrl) {
        try {
            log.info("[COMMONS_UTILS][v3] Criando um novo link encurtado com o url link de origem: {}", originalUrl);

            var shortenUrl = new ShortenUrl(originalUrl, daysToExpire);
            shortenUrl.setShortenUrlLink(
                    ShortenUrlUtils.generateRandomKeyFrom(SHORTEN_URL_LENGTH_MIN, SHORTEN_URL_LENGTH_MAX)
            );

            return finalizeAndSaved(originalUrl, shortenUrl);
        } catch (Exception e) {
            log.error("[COMMONS_UTILS][v3] Erro ao criar um novo link encurtado com o url link de origem: {}", originalUrl, e);
            throw new ShortenUrlCreateFailedException("Erro ao criar um novo link encurtado com o url link de origem: " + originalUrl, e);
        }
    }

    @Transactional
    public ShortenUrlOutputSaved finalizeAndSaved(String originalUrl, ShortenUrl shortenUrl) {
        var shortenUrlSaved = shortenUrlPort.create(shortenUrl);
        log.info("[COMMONS_UTILS] Link encurtado {} criado com sucesso por meio da link url origme: {}", shortenUrlSaved, originalUrl);
        return new ShortenUrlOutputSaved(shortenUrlSaved.getShortenUrlLink());
    }

    @Override
    public List<ShortenUrlOutput> getAllShortenUrls() {
        log.info("[COMMONS_UTILS] Buscando todos os links encurtados gerado(s)/salvo(s).");
        List<ShortenUrl> allItems = shortenUrlPort.getAll();

        if (allItems.isEmpty()) {
            log.warn("Não foram encontrado(s) links encurtado(s) gerado(s) e/ou salvo(s).");
            throw new ShortenUrlNotFoundException("Não foram encontrado(s) links encurtado(s) gerado(s) e/ou salvo(s).");
        }

        return allItems.stream()
                .map(shortenUrl -> shortenUrlMapper.toShortenUrlOutput(shortenUrl))
                .toList();
    }

    @Override
    public ShortenUrlOutput getByShortenUrlLink(final String shortenUrlLink) {
        log.info("[COMMONS_UTILS] Buscando a link da url origem completa por meio do link encurtado: {}", shortenUrlLink);

        var shortenUrlEntity = shortenUrlPort.findByShortenUrlLink(shortenUrlLink)
                .orElseThrow(
                        () -> new ShortenUrlNotFoundException("Link da url de origem não encontrada com o linkd encurtado: " + shortenUrlLink)
                );

        return shortenUrlMapper.toShortenUrlOutput(shortenUrlEntity);
    }


    //
    //        if (Objects.isNull(shortenUrlEntity)) {
    //            log.warn("[COMMONS_UTILS] ShortenUrl não encontrado com shortenUrlLink: {}", shortenUrlLink);
    //            return null;
    //        }
    //        return shortenUrlMapper.toDomain(shortenUrlEntity);
}
