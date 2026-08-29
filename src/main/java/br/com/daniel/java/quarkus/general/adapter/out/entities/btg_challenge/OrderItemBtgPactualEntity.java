package br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemBtgPactualEntity {

    @BsonProperty("id_item_pedido")
    private String orderItemId;

    @BsonProperty("item")
    private Integer item;

    @BsonProperty("produto")
    private String product;

    @BsonProperty("qtde")
    private Integer quantity;

    @BsonProperty("valor_preco")
    private BigDecimal price;

    @BsonProperty("ativo")
    private Boolean active;

    @BsonProperty("data_criacao")
    private LocalDateTime createdAt;
}
