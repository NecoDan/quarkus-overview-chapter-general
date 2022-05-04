package br.com.daniel.java.quarkus.general.adapter.http.spring.controller;

import br.com.daniel.java.quarkus.general.adapter.datastore.entitys.Fruta;
import br.com.daniel.java.quarkus.general.util.RandomicoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/frutas")
@Slf4j
public class FrutasResource {

    @Path("/hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {

        log.info("{}", "Inicio chamada mensagem hello...");
        final var message = "Estamos no ar com quarkus-overview-chapter...";

        log.info("{}", message);
        return message;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruta> list() {

        log.info("{}", "Obter todas as listas de frutas...");
        final List<Fruta> frutas = Fruta.listAll();

        log.info("{}", frutas);
        return frutas;
    }

    @POST
    @Transactional
    public void addFruta() {

        log.info("{}", "Criar novo elemento do tipo fruta...");
        final var fruta = buildFruta("Maça", RandomicoUtil.gerarValorRandomicoDoubleLimite(100));

        log.info("{}", fruta);
        fruta.persist();
    }

    private Fruta buildFruta(String nome, double qtde) {
        Fruta fruta = new Fruta();
        fruta.nome = nome;
        fruta.qtde = qtde;
        return fruta;
    }
}
