package br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "O Id do item do pedido não pode vazio e/ou null")
    private String orderItemId;

    @NotNull(message = "O valor da ordem do item é obrigatória")
    @BsonProperty("item")
    private Integer item;

    @BsonProperty("produto")
    @NotBlank(message = "A descricao do produto não pode ser vazia e/ou null")
    private String product;

    @NotNull(message = "A quantidade referente ao produto é obrigatória")
    @Min(value = 1, message = "A quantidade referente ao produto deve ser maior que zero")
    @BsonProperty("qtde")
    private Integer quantity;

    @NotNull(message = "O valor do preço do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor do preço do produto deve ser maior que zero")
    @BsonProperty("valor_preco")
    private BigDecimal price;

    @BsonProperty("ativo")
    private Boolean active;

    @BsonProperty("data_criacao")
    private LocalDateTime createdAt;
}
