package br.com.daniel.java.quarkus.general.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RabbitMqConfig {

    @ConfigProperty(defaultValue = "mp.messaging.incoming.orders-in.queue.name") String queeNameConsumer;

    @Produces
    @ApplicationScoped
    public void orderCreateQueue() {
        System.out.println("Queue name for consumer: " + queeNameConsumer);
    }
}
