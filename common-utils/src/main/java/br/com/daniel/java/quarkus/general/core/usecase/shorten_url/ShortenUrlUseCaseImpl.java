package br.com.daniel.java.quarkus.general.core.usecase.shorten_url;

import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;
import br.com.daniel.java.quarkus.general.core.mappers.ShortenUrlMapper;
import br.com.daniel.java.quarkus.general.core.port.ShortenUrlPort;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import br.com.daniel.java.quarkus.general.exceptions.ShortenUrlCreateFailedException;
import br.com.daniel.java.quarkus.general.utils.RandomUtils;
import br.com.daniel.java.quarkus.general.utils.ShortenUrlUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Slf4j
public class ShortenUrlUseCaseImpl implements ShortenUrlUseCase {

    private static final Integer SHORTEN_URL_LENGTH_MIN = 5;
    private static final Integer SHORTEN_URL_LENGTH_MAX = 11;

    @Inject
    ShortenUrlPort shortenUrlPort;

    @Inject
    ShortenUrlMapper shortenUrlMapper;

    @Override
    @Transactional
    public ShortenUrlOutputSaved createShortenUrlV1(String originalUrl) {
        try {
            log.info("[COMMONS_UTILS][v1] Criando ShortenUrl com originalUrl: {}", originalUrl);

            var shortenUrl = new ShortenUrl(originalUrl);
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
            log.info("[COMMONS_UTILS][v2] Criando ShortenUrl com originalUrl: {}", originalUrl);

            var shortenUrl = new ShortenUrl(originalUrl);
            var idSaved = Objects.isNull(shortenUrl.getId()) ? RandomUtils.gerarValorRandomicoAte(1000) : shortenUrl.getId();

            shortenUrl.setShortenUrlLink(ShortenUrlUtils.toBase62(String.valueOf(idSaved)));
            return finalizeAndSaved(originalUrl, shortenUrl);
        } catch (Exception e) {
            log.error("[COMMONS_UTILS][v2] Erro ao criar um novo link encurtado com o url link de origem: {}", originalUrl, e);
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
        log.info("[COMMONS_UTILS] Buscando todos os links encurtados salvos.");

        return shortenUrlPort.getAll().stream()
                .map(shortenUrl -> shortenUrlMapper.toShortenUrlOutput(shortenUrl))
                .toList();
    }

    //
    //        if (Objects.isNull(shortenUrlEntity)) {
    //            log.warn("[COMMONS_UTILS] ShortenUrl não encontrado com shortenUrlLink: {}", shortenUrlLink);
    //            return null;
    //        }
    //        return shortenUrlMapper.toDomain(shortenUrlEntity);
}
