package br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@MongoEntity(collection = "tb_btg_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderBtgPactualEntity extends PanacheMongoEntityBase {

    @BsonId
    public ObjectId id;

    @BsonProperty("orderId")
    @NotBlank(message = "O Id do pedido não poder vazio e/ou null")
    public String orderId;

    @BsonProperty("customer")
    public OrderCustomerBtgPactualEntity customer;

    @NotNull(message = "O valor total do pedido é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor total do pedido deve ser maior que zero")
    @BsonProperty("totalValue")
    public BigDecimal totalValue;

    // Data da solicitação não pode ser no futuro
    @NotNull(message = "A data de criação é obrigatória")
    @PastOrPresent(message = "A data de criação deve ser no passado ou presente")
    @BsonProperty("createdAt")
    public LocalDateTime createdAt;

    @BsonProperty("updateAt")
    public LocalDateTime updateAt;

    @BsonProperty("items")
    public List<OrderItemBtgPactualEntity> items;
}
