package br.com.daniel.java.quarkus.general.adapter.in.rabbitmq.btg_challenge;

import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Slf4j
public class BtgPactualOrderConsumer {

    @ConfigProperty(defaultValue = "mp.messaging.incoming.orders-in.queue.name")
    String queeNameConsumer;

    //
//    @Inject
//    OrderBtgPactualCreateUseCase orderBtgPactualCreateUseCase;

//    @Inject
//    @CustomObjectMapper
//    ObjectMapper objectMapper;

    @Incoming("orders-in")
    public void consumerProcessOrders(byte[] payload) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("BTG_PACTUAL_CHALLENGE - RabbitMQ evento/payload recebido na fila [{}].", queeNameConsumer);

            String message = new String(payload, StandardCharsets.UTF_8);
            System.out.println(message);
            // orderBtgPactualCreateUseCase.createOrderFrom(payloadEventInput);

            log.info("BTG_PACTUAL_CHALLENGE - RabbitMQ evento/payload processado com sucesso na fila {}.", queeNameConsumer);
        } catch (Exception e) {
            log.error("BTG_PACTUAL_CHALLENGE - RabbitMQ erro ao consumir evento/payload via mensagem na fila {}: {}", queeNameConsumer, e.getMessage(), e);
        } finally {
            MdcUtils.clear();
        }
    }
}
