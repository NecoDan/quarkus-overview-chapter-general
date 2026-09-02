package br.com.daniel.java.quarkus.general.core.domain.btg_challenge;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderCustomerBtgPactual implements Serializable {

    private String customerId;
    private LocalDateTime createdAt;

    public OrderCustomerBtgPactual defineCreatedAt() {
        this.createdAt = LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
        return this;
    }
}
