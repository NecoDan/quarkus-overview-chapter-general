package br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge;

import br.com.daniel.java.quarkus.general.utils.validations.IntervaloValido;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_uol_typeheroes")
@IntervaloValido(
        fieldStartDate = "startDateExpiration",
        fieldEndDate = "endDateExpiration",
        message = "A validade final deve ser após a validade inicial"
)
public class TypeHeroEntity extends PanacheEntityBase implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String description;

    @Column(name = "data_inicio_expiracao", nullable = false)
    private LocalDateTime startDateExpiration;

    @Column(name = "data_fim_expiracao", nullable = false)
    private LocalDateTime endDateExpiration;
}
