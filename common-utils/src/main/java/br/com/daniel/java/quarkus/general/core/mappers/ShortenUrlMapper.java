package br.com.daniel.java.quarkus.general.core.mappers;

import br.com.daniel.java.quarkus.general.adapter.out.entities.shorten_url.ShortenUrlEntity;
import br.com.daniel.java.quarkus.general.core.domain.shorten_url.ShortenUrl;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface ShortenUrlMapper {
    ShortenUrlEntity toEntity(ShortenUrl domain);

    ShortenUrl toDomain(ShortenUrlEntity entity);

    ShortenUrlOutput toShortenUrlOutput(ShortenUrl domain);
}
