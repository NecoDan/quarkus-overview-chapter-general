package br.com.daniel.java.quarkus.general.core.domain;

import br.com.daniel.java.quarkus.general.adapter.out.entities.uol_challenge.GamePlayerUolEntity;
import br.com.daniel.java.quarkus.general.config.EncryptoManagerConfig;
import br.com.daniel.java.quarkus.general.core.usecase.uol_challenge.input.GamePlayerInput;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
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
public class GamePlayerUol implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String encryptedPhoneNumber;
    private String codeName;
    private TypeHeroGroup groupCode;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private boolean indicadorAtivo;
    private String rawPhoneNumber;

    public GamePlayerUol(GamePlayerUolEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
            this.rawPhoneNumber = EncryptoManagerConfig.decrypt(this.encryptedPhoneNumber);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public GamePlayerUol(GamePlayerInput input) {
        try {
            BeanUtils.copyProperties(this, input);

            this.rawPhoneNumber = input.phone();
            this.groupCode = TypeHeroGroup.of(input.codeHeroGroup());
            this.createdAt = LocalDateTime.now();
            this.updateAt = LocalDateTime.now();
            this.indicadorAtivo = Boolean.TRUE;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
