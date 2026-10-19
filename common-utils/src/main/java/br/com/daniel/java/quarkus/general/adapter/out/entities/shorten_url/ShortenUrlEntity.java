package br.com.daniel.java.quarkus.general.adapter.out.entities.shorten_url;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_shorten_url")
public class ShortenUrlEntity extends PanacheEntity implements Serializable {

    @NotBlank(message = "A url de origem não pode ser vazia e/ou null")
    @Column(name = "original_url_link")
    private String originalUrlLink;

    @NotBlank(message = "A url encurtada não pode vazia e/ou null")
    @Column(name = "shorten_url_link")
    private String shortenUrlLink;

    @NotNull(message = "A data de criação é obrigatória")
    @Column(name = "created_at_date")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_date")
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active;
}
