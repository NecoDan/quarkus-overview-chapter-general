package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualGetsUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderCreatedBtgPactualOutput;
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

import java.util.UUID;

@Path("/btgpactual/orders")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class OrderBtgPactualController {

    @Inject
    OrderBtgPactualCreateUseCase orderBtgPactualCreateUseCase;

    @Inject
    OrderBtgPactualGetsUseCase orderBtgPactualGetsUseCase;

    @POST
    @Path(value = "/v1")
    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um novo pedido com os dados fornecidos no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Pedido criada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = OrderCreatedBtgPactualOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "Campos não atendem os requisitos pra criaçaõ do Pedido"
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
    public Response create(@Valid OrderBtgPactualInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando rota de criação do Pedido");

            return Response.status(Response.Status.CREATED)
                    .entity(orderBtgPactualCreateUseCase.createOrder(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de busca de pedido por ID: {}", id);

            return Response.ok(orderBtgPactualGetsUseCase.getById(id)).build();
        } finally {
            MdcUtils.clear();
        }
    }
}
