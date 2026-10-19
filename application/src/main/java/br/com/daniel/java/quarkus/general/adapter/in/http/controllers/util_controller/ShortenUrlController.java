package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.util_controller;

import br.com.daniel.java.quarkus.general.adapter.out.services.ShortenUrlUseCaseAdapter;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.input.ShortenUrlInput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutput;
import br.com.daniel.java.quarkus.general.core.usecase.shorten_url.output.ShortenUrlOutputSaved;
import br.com.daniel.java.quarkus.general.utils.ShortenUrlUtils;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.net.URI;

@Path("/commons/shorten_url")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class ShortenUrlController {

    @Context
    UriInfo uriInfo;

    @Inject
    ShortenUrlUseCaseAdapter shortenUrlUseCaseAdapter;

    @GET
    @Path(value = "/v1")
    @Operation(
            summary = "Busca todos os links encurtados",
            description = "Retorna todos os links encurtados salvos."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Lista com todos os links encurtados retornadas com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ShortenUrlOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Links encurtados não cadastrados, lista vazia"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    public Response getAll() {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("COMMONS_UTILS - Inicializando a busca de todos os links encurtados cadastrados");

            return Response.status(Response.Status.OK)
                    .entity(shortenUrlUseCaseAdapter.getAll())
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/{shortURL}")
    public Response getByShortenUrl(@PathParam("shortURL") @NotNull(message = "link encurtado é obrigatório") String shortURL) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("COMMONS_UTILS - Obtendo link original por meio do link encurtado: {}", shortURL);

            var shortenUrlOutput = shortenUrlUseCaseAdapter.getByShortenUrlLink(shortURL);
            var uri = URI.create(shortenUrlOutput.getRedirectUrl());

            return Response.seeOther(uri).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @POST
    @Path(value = "/v1")
    @Operation(
            summary = "[v1] Cria um link encurtado",
            description = "[v1] Cria um novo link encurtado por meio da url de origem fornecida no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "[v1] Link encurtado gerado/criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ShortenUrlInput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "[v1] Campos não atendem os requisitos para a criação do link encurtado"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "[v1] Requisição inválida"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "[v1] Erro interno do servidor"
            )
    })
    public Response createV1(@Valid ShortenUrlInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("[COMMONS_UTILS][v1] - Inicializando rota de criação de um novo link encurtado com a url de origem: {}", input.linkUrl());
            return buildSavedResponse(shortenUrlUseCaseAdapter.createV1(input));
        } finally {
            log.info("[COMMONS_UTILS][v1] - Finalizado a criação do encurta link");
            MdcUtils.clear();
        }
    }

    @POST
    @Path(value = "/v2")
    @Operation(
            summary = "[v2] Cria um link encurtado",
            description = "[v2] Cria um novo link encurtado por meio da url de origem fornecida no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "[v2] Link encurtado gerado/criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ShortenUrlInput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "[v2] Campos não atendem os requisitos para a criação do link encurtado"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "[v2] Requisição inválida"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "[v2] Erro interno do servidor"
            )
    })
    public Response createV2(@Valid ShortenUrlInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("[COMMONS_UTILS][v2] - Inicializando rota de criação de um novo link encurtado com a url de origem: {}", input.linkUrl());

            return buildSavedResponse(shortenUrlUseCaseAdapter.createV2(input));
        } finally {
            log.info("[COMMONS_UTILS][v2] - Finalizado a criação do encurta link");
            MdcUtils.clear();
        }
    }

    @POST
    @Path(value = "/v3")
    @Operation(
            summary = "[v3] Cria um link encurtado",
            description = "[v3] Cria um novo link encurtado por meio da url de origem fornecida no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "[v3] Link encurtado gerado/criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ShortenUrlInput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "[v3] Campos não atendem os requisitos para a criação do link encurtado"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "[v3] Requisição inválida"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "[v3] Erro interno do servidor"
            )
    })
    public Response createV3(@Valid ShortenUrlInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("[COMMONS_UTILS][v3] - Inicializando rota de criação de um novo link encurtado com a url de origem: {}", input.linkUrl());

            return buildSavedResponse(shortenUrlUseCaseAdapter.createV2(input));
        } finally {
            log.info("[COMMONS_UTILS][v3] - Finalizado a criação do encurta link");
            MdcUtils.clear();
        }
    }

    private Response buildSavedResponse(ShortenUrlOutputSaved shortenUrlOutput) {
        return Response.status(Response.Status.CREATED)
                .entity(buildUriResponse(shortenUrlOutput))
                .build();
    }

    private URI buildUriResponse(ShortenUrlOutputSaved shortenUrlOutput) {
        return uriInfo.getAbsolutePathBuilder()
                .path(shortenUrlOutput.shortenUrl())
                .build();
    }


}
