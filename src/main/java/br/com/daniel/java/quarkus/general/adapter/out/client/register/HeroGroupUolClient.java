package br.com.daniel.java.quarkus.general.adapter.out.client.register;

import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.AvengersMarvelOutputDTO;
import br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge.JusticeLeagueDcDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/test-backEnd-Java/master/referencias")
@RegisterRestClient(configKey = "uol.herogroup.marvel-or-dc-comics.service.url")
@ApplicationScoped
public interface HeroGroupUolClient {

    @GET
    @Path("/vingadores.json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Default
    AvengersMarvelOutputDTO getMarvelSuperHeroGroups();

    @GET
    @Path("/liga_da_justica.xml")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @Default
    JusticeLeagueDcDTO getDCSuperHeroGroups();
}
