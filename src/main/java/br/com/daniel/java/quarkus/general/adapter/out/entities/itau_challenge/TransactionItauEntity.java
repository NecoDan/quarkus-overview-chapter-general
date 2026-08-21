package br.com.daniel.java.quarkus.general.adapter.out.entities.itau_challenge;

import br.com.daniel.java.quarkus.general.config.EncryptoManagerConfig;
import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_itau_transaction")
public class TransactionItauEntity extends PanacheEntityBase implements Serializable {

    @Serial private static final long serialVersionUID = -5471522162103268843L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_transacao")
    private String transactionId;

    @Column(name = "num_documento_usuario")
    private String encryptedUserDocument;

    @Column(name = "token_cartao_credito")
    private String encryptedCreditCardToken;

    @Column(name = "valor_transacao")
    private Long transactionValue;

    @Column(name = "quantia")
    private BigDecimal amount;

    @Column(name = "dt_criacao")
    private LocalDateTime createdAt;

    // atributos transientes [propriedades que não será persistida no banco de dados]
    @Transient
    private String rawUserDocument;

    @Transient
    private String rawCreditCardToken;

    @PrePersist
    public void prePersist() {
        this.encryptedUserDocument = EncryptoManagerConfig.encrypt(rawUserDocument);
        this.encryptedCreditCardToken = EncryptoManagerConfig.encrypt(rawCreditCardToken);
    }

    @PostLoad
    public void postLoad() {
        this.rawUserDocument = EncryptoManagerConfig.decrypt(encryptedUserDocument);
        this.rawCreditCardToken = EncryptoManagerConfig.decrypt(encryptedCreditCardToken);
    }

    public TransactionItauEntity(TransactionItau transactionItau) {
        try {
            BeanUtils.copyProperties(this, transactionItau);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
