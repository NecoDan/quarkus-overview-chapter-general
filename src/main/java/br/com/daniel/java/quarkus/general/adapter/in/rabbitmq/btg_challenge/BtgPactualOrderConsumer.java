package br.com.daniel.java.quarkus.general.adapter.in.rabbitmq.btg_challenge;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BtgPactualOrderConsumer {

    @ConfigProperty(defaultValue = "mp.messaging.incoming.orders-in.queue.name")
    String queeNameConsumer;

    @Incoming("orders")
    public void consume(String message) {
        System.out.println("Mensagem recebida: " + message);
    }
}
