package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output;

import br.com.daniel.java.quarkus.general.core.domain.TransactionItau;
import br.com.daniel.java.quarkus.general.utils.FunctionalUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "transactionId", "amount", "userDocument", "creditCardToken", "createdAt"})
public record TransactionItauOutput(String id,
                                    String transactionId,
                                    String amount,
                                    String userDocument,
                                    String creditCardToken,
                                    String createdAt) {
    @JsonIgnore
    public static TransactionItauOutput from(TransactionItau entity) {
        final var dtCreatedAt = Objects.isNull(entity.getCreatedAt())
                ? StringUtils.EMPTY
                : FunctionalUtils.formatCreationDate(entity.getCreatedAt());

        return new TransactionItauOutput(
                String.valueOf(entity.getId()),
                String.valueOf(entity.getTransactionId()),
                FunctionalUtils.formatDecimalNumber(entity.getAmount()),
                entity.getRawUserDocument(),
                entity.getRawCreditCardToken(),
                dtCreatedAt
        );
    }
}
