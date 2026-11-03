package br.com.daniel.java.quarkus.general.adapter.out.entities.shorten_url;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_shorten_url")
public class ShortenUrlEntity extends PanacheEntityBase implements Serializable {

    @Serial private static final long serialVersionUID = -5471522162103268843L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A url de origem não pode ser vazia e/ou null")
    @Column(name = "original_url_link")
    private String originalUrlLink;

    @NotBlank(message = "A url encurtada não pode vazia e/ou null")
    @Column(name = "shorten_url_link")
    private String shortenUrlLink;

    @NotNull(message = "A data de criação é obrigatória. Não pode ser vazio e/ou null")
    @Column(name = "created_at_date")
    private LocalDateTime expiredDtAt;

    @Column(name = "updated_at_date")
    private LocalDateTime updatedExpiredDtAt;

    @Column(name = "expired")
    private Boolean expired;

    @NotNull(message = "Dias de expiraçaão é obrigatório. Não pode ser vazio e/ou null")
    @Column(name = "days_to_expire")
    private Integer daysToExpire;
}
