package br.com.daniel.java.quarkus.general.adapter.out.entities.btg_challenge;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCustomerBtgPactualEntity {

    @BsonProperty("customerId")
    @NotBlank(message = "O Id do cliente não poder vazio e/ou null")
    public String customerId;

    @BsonProperty("createdAt")
    public LocalDateTime createdAt;
}
