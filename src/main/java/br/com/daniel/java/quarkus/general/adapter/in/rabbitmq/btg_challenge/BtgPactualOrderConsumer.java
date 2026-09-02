package br.com.daniel.java.quarkus.general.adapter.in.rabbitmq.btg_challenge;

import br.com.daniel.java.quarkus.general.config.jacksonmapper.CustomObjectMapper;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.utils.logs.MdcUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;


@ApplicationScoped
@Slf4j
public class BtgPactualOrderConsumer {

    @ConfigProperty(defaultValue = "mp.messaging.incoming.orders-in.queue.name")
    String queeNameConsumer;

    @Inject
    OrderBtgPactualCreateUseCase orderBtgPactualCreateUseCase;

    @Inject
    @CustomObjectMapper
    ObjectMapper objectMapper;

    @Incoming("orders-in")
    public void consumerProcessOrders(String payload) {
        MdcUtils.putTransactionIdRandom();
        try {
            log.info("BTG_PACTUAL_CHALLENGE - RabbitMQ evento/payload recebido na fila [{}].", queeNameConsumer);

            log.debug("BTG_PACTUAL_CHALLENGE - Payload recebido: {}", payload);

            OrderCreatedEventBtgPactualInput payloadEventInput = objectMapper.readValue(
                    payload,
                    OrderCreatedEventBtgPactualInput.class
            );

            orderBtgPactualCreateUseCase.createOrderFrom(payloadEventInput);

            log.info("BTG_PACTUAL_CHALLENGE - RabbitMQ evento/payload processado com sucesso na fila {}.", queeNameConsumer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("BTG_PACTUAL_CHALLENGE - RabbitMQ erro de validação ao consumir evento/payload na fila {}: {}", 
                    queeNameConsumer, e.getMessage(), e);
            throw new RuntimeException("Erro ao processar pedido: " + e.getMessage(), e);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error("BTG_PACTUAL_CHALLENGE - RabbitMQ erro ao desserializar JSON do payload na fila {}: {}", 
                    queeNameConsumer, e.getMessage(), e);
            throw new RuntimeException("Erro ao desserializar JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("BTG_PACTUAL_CHALLENGE - RabbitMQ erro inesperado ao consumir evento/payload na fila {}: {}", 
                    queeNameConsumer, e.getMessage(), e);
            throw new RuntimeException("Erro inesperado ao processar pedido: " + e.getMessage(), e);
        } finally {
            MdcUtils.clear();
        }
    }
}
