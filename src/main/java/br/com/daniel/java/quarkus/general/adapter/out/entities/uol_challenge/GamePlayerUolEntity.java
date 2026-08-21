package br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge;

import br.com.daniel.java.quarkus.general.config.EncryptoManagerConfig;
import br.com.daniel.java.quarkus.general.core.domain.GamePlayerUol;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_uol_gameplayers")
public class GamePlayerUolEntity extends PanacheEntityBase implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone", nullable = false)
    private String encryptedPhoneNumber;

    @Column(name = "codinome")
    private String codeName;

    @Column(name = "codigo_grupo", columnDefinition = "int")
    private Integer groupCode;

    @Column(name = "dt_criacao", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "dt_atualizacao", columnDefinition = "datetime")
    private LocalDateTime updateAt;

    @Column(name = "ativo", nullable = false, columnDefinition = "tinyint")
    private boolean indicadorAtivo;

    // atributos transientes [propriedades que não será persistida no banco de dados]
    @Transient
    private String rawPhoneNumber;

    @PrePersist
    public void prePersist() {
        this.encryptedPhoneNumber = EncryptoManagerConfig.encrypt(rawPhoneNumber);
    }

    @PostLoad
    public void postLoad() {
        this.rawPhoneNumber = EncryptoManagerConfig.decrypt(encryptedPhoneNumber);
    }

    public GamePlayerUolEntity(GamePlayerUol entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
