package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.uol_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.TransactionItauOutput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.GamePlayerUolUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.output.GamePlayerOutput;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    GamePlayerUolUseCase gamePlayerUolUseCase;

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
                    .entity(gamePlayerUolUseCase.createPlayer(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

}
