package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.uol_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.GamePlayerUolCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.GamePlayerUolGetUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerReportOutput;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/uol/gameplayers")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class GamePlayerUolController {

    @Inject
    GamePlayerUolCreateUseCase gamePlayerUolCreateUseCase;

    @Inject
    GamePlayerUolGetUseCase gamePlayerUolGetUseCase;

    @POST
    @Path(value = "/v1")
    @Operation(
            summary = "Cria um novo jogador",
            description = "Cria um novo jogador com os dados fornecidos no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Jogador criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = GamePlayerOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "Campos não atendem os requisitos da transação"
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
    public Response create(@Valid GamePlayerInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("UOL_CHALLENGE - Inicializando rota de criação de um novo jogador com o codinome");

            return Response.status(Response.Status.CREATED)
                    .entity(gamePlayerUolCreateUseCase.createPlayer(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1")
    @Operation(
            summary = "Busca todos os jogadores",
            description = "Retorna todos os jogadores salvos."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Lista com jogadores retornadas com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = GamePlayerReportOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Jogadores não cadastrados, lista vazia"
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
            log.info("UOL_CHALLENGE - Inicializando a busca de todos os jogadores cadastrados");

            return Response.status(Response.Status.OK)
                    .entity(gamePlayerUolGetUseCase.getAll())
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }
}
