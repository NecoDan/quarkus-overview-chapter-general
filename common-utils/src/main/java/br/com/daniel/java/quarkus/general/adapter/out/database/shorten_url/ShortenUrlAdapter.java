package br.com.daniel.java.quarkus.general.adapter.out.database.shorten_url;

import br.com.daniel.java.quarkus.general.adapter.out.entities.shorten_url.ShortenUrlEntity;
import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;
import br.com.daniel.java.quarkus.general.core.mappers.ShortenUrlMapper;
import br.com.daniel.java.quarkus.general.core.port.ShortenUrlPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class ShortenUrlAdapter implements ShortenUrlPort {

    @Inject
    ShortenUrlMapper shortenUrlMapper;

    @Override
    @Transactional
    public ShortenUrl create(ShortenUrl shortenUrl) {
        log.info("[COMMONS_UTILS] Criando link encurtado com os seguintes dados: {}", shortenUrl);
        var shortenUrlEntity = shortenUrlMapper.toEntity(shortenUrl);

        shortenUrlEntity.persistAndFlush();
        return shortenUrlMapper.toDomain(shortenUrlEntity);
    }

    @Override
    public List<ShortenUrl> getAll() {
        log.info("[COMMONS_UTILS] Buscando todos os links encurtados salvo(s).");
        var shortenUrlEntities = ShortenUrlEntity.listAll();

        return shortenUrlEntities.stream()
                .map(entity -> shortenUrlMapper.toDomain((ShortenUrlEntity) entity))
                .toList();
    }

    @Override
    public Optional<ShortenUrl> findById(final Long id) {
        log.info("[COMMONS_UTILS] Buscando ShortenUrl com id: {}", id);
        var shortenUrlEntity = ShortenUrlEntity.findById(id);

        return Optional.ofNullable(shortenUrlEntity)
                .map(entity -> shortenUrlMapper.toDomain((ShortenUrlEntity) entity));
    }

    @Override
    public Optional<ShortenUrl> findByShortenUrlLink(final String shortenUrlLink) {
        log.info("[COMMONS_UTILS] Buscando ShortenUrl com shortenUrlLink: {}", shortenUrlLink);
        var shortenUrlEntity = ShortenUrlEntity.find("shortenUrlLink", shortenUrlLink).firstResult();

        return Optional.ofNullable(shortenUrlEntity)
                .map(entity -> shortenUrlMapper.toDomain((ShortenUrlEntity) entity));
    }
}
