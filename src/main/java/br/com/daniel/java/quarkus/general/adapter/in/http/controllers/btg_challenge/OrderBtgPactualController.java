package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualGetsUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderCreatedBtgPactualOutput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.output.OrderTotalQuantityValuesBtgPactualOutput;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
    @Path(value = "/v1")
    public Response getAll(@QueryParam("page") @DefaultValue("0") int page,
                           @QueryParam("size") @DefaultValue("10") int size,
                           @QueryParam("expand_items") @DefaultValue("false") boolean expandItems) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando rota de busca de todo(s) pedido(s)");

            return Response.ok(orderBtgPactualGetsUseCase.getAllPageable(page, size, expandItems)).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/{id}")
    public Response getByOrderId(@PathParam("id") ObjectId id) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando rota de busca de pedido por ID: {}", id);

            return Response.ok(orderBtgPactualGetsUseCase.getById(id)).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/{id}/totalAmount")
    public Response getTotalAmountByOrderId(@PathParam("id") ObjectId id) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando rota de busca do valor total do pedido por ID: {}", id);

            return Response.ok(orderBtgPactualGetsUseCase.getTotalAmountBy(id)).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/consumers/listAll")
    @Operation(
            summary = "Busca todos os pedidos por cliente",
            description = "Retorna todos os pedido(s) salvo(s) e sumarizados por cliente."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Lista com pedido(s) sumarizados retornados com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = OrderTotalQuantityValuesBtgPactualOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Pedido(s) não cadastrados, lista vazia"
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
    public Response getAllOrderByCustomerId(@QueryParam("customerId") UUID customerId,
                                            @QueryParam("page") @DefaultValue("0") int page,
                                            @QueryParam("size") @DefaultValue("10") int size,
                                            @QueryParam("expand_items") @DefaultValue("false") boolean expandItems) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando a busca ");

            return Response.ok(orderBtgPactualGetsUseCase.getAllOrdersPageableByCustomer(
                            customerId, page, size, expandItems
                    )).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/consumers/summarize")
    @Operation(
            summary = "Sumarizar os dados de pedidos por cliente",
            description = "Retorna um resumo sumarizado sos pedido(s) salvo(s) pelo cliente."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Lista com pedido(s) sumarizados retornados com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = OrderTotalQuantityValuesBtgPactualOutput.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Pedido(s) não cadastrados, lista vazia"
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
    public Response getSummariseOrdersByCustomerId(@QueryParam("customerId") UUID customerId) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - Inicializando a busca ");

            return Response.ok(orderBtgPactualGetsUseCase.getTotalQuantityOrdersBy(customerId)).build();
        } finally {
            MdcUtils.clear();
        }
    }
}
