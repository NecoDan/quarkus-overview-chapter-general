package br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge;

import br.com.daniel.java.quarkus.general.config.EncryptoManagerConfig;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.GamePlayerUol;
import br.com.daniel.java.quarkus.general.core.domain.uol_challenge.TypeHeroGroup;
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

    @Column(name = "telefone")
    private String encryptedPhoneNumber;

    @Column(name = "codinome", nullable = false)
    private String codeName;

    @Column(name = "codigo_grupo", columnDefinition = "int")
    private Integer groupCodeInt;

    @Column(name = "dt_criacao")
    private LocalDateTime createdAt;

    @Column(name = "dt_atualizacao")
    private LocalDateTime updateAt;

    @Column(name = "ativo", nullable = false, columnDefinition = "tinyint")
    private boolean indicadorAtivo;

    // atributos transientes [propriedades que não será persistida no banco de dados]
    @Transient
    private String rawPhoneNumber;

    @Transient
    private TypeHeroGroup groupCode;

    @PrePersist
    public void prePersist() {
        defineEncryptedPhoneNumber();
        defineGroupCodeInt();
    }

    @PostLoad
    public void postLoad() {
        this.rawPhoneNumber = EncryptoManagerConfig.decrypt(encryptedPhoneNumber);
        this.groupCode = TypeHeroGroup.of(this.groupCodeInt);
    }

    public GamePlayerUolEntity(GamePlayerUol entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public void defineEncryptedPhoneNumber() {
        this.encryptedPhoneNumber = EncryptoManagerConfig.encrypt(rawPhoneNumber);
    }

    public void defineGroupCodeInt() {
        this.groupCodeInt = this.groupCode.getCode();
    }
}
