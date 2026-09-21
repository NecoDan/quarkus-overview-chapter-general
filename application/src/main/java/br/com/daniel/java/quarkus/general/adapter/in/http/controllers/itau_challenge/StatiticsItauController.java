package br.com.daniel.java.quarkus.general.adapter.in.http.controllers.itau_challenge;


import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.StatiticsTransactionItauUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.StatisticsItauOutput;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/itau/statistics")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class StatiticsItauController {

    @Inject
    StatiticsTransactionItauUseCase statiticsTransactionItauUseCase;

    @GET
    @Path("/v1/")
    @APIResponse(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = StatisticsItauOutput.class)
            ), description = "faz a busca único lançamento faturamento por id"
    )
    public Response getStatisticsSummary(@QueryParam("intervaloBusca") Integer intervaloBusca) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de busca de estatísticas de transações");

            return Response
                    .ok(statiticsTransactionItauUseCase.calculateStatistics(intervaloBusca))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }
}
