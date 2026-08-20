package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.itau_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.TransactionItauCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.TransactionItauGetsUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.TransactionItauRemoveUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.core.usecase.output.TransactionItauOutput;
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

@Path("/itau/transactions")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class TransactionItauController {

    @Inject
    TransactionItauCreateUseCase transactionCreateUseCase;
    @Inject
    TransactionItauGetsUseCase transactionGetsUseCase;
    @Inject
    TransactionItauRemoveUseCase transactionRemoveUseCase;

    @POST
    @Path(value = "/v1")
    @Operation(
            summary = "Cria uma nova transação",
            description = "Cria uma nova transação com os dados fornecidos no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Transação criada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TransactionItauOutput.class)
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
    public Response create(@Valid TransactionItauInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de criação de transação");

            return Response.status(Response.Status.CREATED)
                    .entity(transactionCreateUseCase.createTransaction(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

    @POST
    @Path(value = "/v2")
    @Operation(
            summary = "Cria uma nova transação",
            description = "Cria uma nova transação com os dados fornecidos no corpo da requisição."
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Transação criada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TransactionItauOutput.class)
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
    public Response createTransaction(@Valid TransactionItauInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de criação de transação");

            return Response.status(Response.Status.CREATED)
                    .entity(transactionCreateUseCase.createNewTransaction(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1")
    public Response getAll() {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de busca todas as transações existentes");

            return Response.ok(transactionGetsUseCase.getAll()).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @GET
    @Path(value = "/v1/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de busca de transação por ID");

            return Response.ok(transactionGetsUseCase.getById(id)).build();
        } finally {
            MdcUtils.clear();
        }
    }

    @DELETE
    @Path(value = "/v1/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de exclusão de transação por ID");

            transactionRemoveUseCase.deleteById(id);
            return Response.noContent().build();
        } finally {
            MdcUtils.clear();
        }
    }

    @DELETE
    @Path(value = "/v1/")
    @Operation(summary = "Deletar transações", description = "Exclusão das transações.")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "204",
                    description = "Transação excluidas com sucesso"
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "Campos não atendem os requisitos da transação"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    public Response deleteAll() {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de exclusão de todas as transações existentes");

            transactionRemoveUseCase.deleteAll();
            return Response.noContent().build();
        } finally {
            MdcUtils.clear();
        }
    }
}
