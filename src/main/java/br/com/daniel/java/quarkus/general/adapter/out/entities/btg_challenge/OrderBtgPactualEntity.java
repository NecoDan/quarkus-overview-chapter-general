package br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge;

import br.com.daniel.java.quarkus.general.core.domain.btg_challenge.OrderBtgPactual;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@MongoEntity(collection = "tb_btg_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBtgPactualEntity extends PanacheMongoEntityBase {

    @BsonId
    @BsonProperty("id_pedido")
    @NotBlank(message = "O Id do pedido não poder vazio e/ou null")
    private String orderId;

    @BsonProperty("id_cliente")
    @NotBlank(message = "O Id do cliente não poder vazio e/ou null")
    private String customerId;

    @NotNull(message = "O valor total do pedido é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor total do pedido deve ser maior que zero")
    @BsonProperty("valor_total")
    private BigDecimal totalValue;

    // Data da solicitação não pode ser no futuro
    @NotNull(message = "A data de criação é obrigatória")
    @PastOrPresent(message = "A data de criação deve ser no passado ou presente")
    @BsonProperty("data_criacao")
    private LocalDateTime createdAt;

    @BsonProperty("data_atualizacao")
    private LocalDateTime updateAt;

    @BsonProperty("itens_pedido")
    private List<OrderItemBtgPactualEntity> items;

    public OrderBtgPactualEntity(OrderBtgPactual orderBtgPactual) {
        try {
            BeanUtils.copyProperties(this, orderBtgPactual);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
