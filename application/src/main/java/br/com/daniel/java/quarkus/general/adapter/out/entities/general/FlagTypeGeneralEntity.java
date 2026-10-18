package br.com.daniel.java.quarkus.general.adapter.out.entities.general;

import br.com.daniel.java.quarkus.general.utils.validations.IntervaloValido;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_flag_types")
@IntervaloValido
public class FlagTypeGeneralEntity extends PanacheEntityBase implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String description;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "data_fim", nullable = false)
    private LocalDate endDate;
}
