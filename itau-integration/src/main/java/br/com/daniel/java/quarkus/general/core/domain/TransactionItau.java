package br.com.daniel.java.quarkus.general.core.domain;

import br.com.daniel.java.quarkus.general.adapter.out.entities.TransactionItauEntity;
import br.com.daniel.java.quarkus.general.config.EncryptoManagerConfig;
import br.com.daniel.java.quarkus.general.core.usecase.input.TransactionItauInput;
import br.com.daniel.java.quarkus.general.exceptions.ParseEntityFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionItau implements Serializable {

    @Serial private static final long serialVersionUID = 7757301350145133274L;

    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private String encryptedUserDocument;
    private String encryptedCreditCardToken;
    private Long transactionValue;
    private LocalDateTime createdAt;
    private String rawUserDocument;
    private String rawCreditCardToken;

    public TransactionItau(TransactionItauInput input) {
        try {
            BeanUtils.copyProperties(this, input);

            this.createdAt = input.createdAt().toLocalDateTime();
            this.rawUserDocument = input.documentNumber();
            this.rawCreditCardToken = input.creditCardToken();

            if (StringUtils.isNotEmpty(this.rawUserDocument)) {
                this.encryptedUserDocument = EncryptoManagerConfig.encrypt(this.rawUserDocument);
            }

            if (StringUtils.isNotEmpty(this.rawCreditCardToken)) {
                this.encryptedCreditCardToken = EncryptoManagerConfig.encrypt(this.rawCreditCardToken);
            }

            if (Objects.isNull(this.amount)) {
                this.amount = input.amount();
                this.transactionValue = this.amount.longValue();
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }

    public TransactionItau(TransactionItauEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
