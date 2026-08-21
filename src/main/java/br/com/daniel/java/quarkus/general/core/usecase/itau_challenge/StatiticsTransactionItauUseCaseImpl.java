package br.com.daniel.java.quarkus.general.core.usecase.itau_challenge;

import br.com.daniel.java.quarkus.general.adapter.out.database.itau_challenge.TransactionItauMemoryAdapter;
import br.com.daniel.java.quarkus.general.core.usecase.itau_challenge.output.StatisticsItauOutput;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

@Singleton
@Slf4j
public class StatiticsTransactionItauUseCaseImpl implements StatiticsTransactionItauUseCase {

    @Inject
    TransactionItauMemoryAdapter transactionItauBusiness;

    @Override
    public StatisticsItauOutput calculateStatistics(Integer secondsRange) {
        log.info("Calculating statistics for Itau transactions");

        var dateTimeRange = OffsetDateTime.now().minusSeconds(secondsRange);

        final var doubleSummaryStatistics = transactionItauBusiness.getTransactionsByDateTime(dateTimeRange)
                .stream()
                .mapToDouble(value -> value.getAmount().doubleValue())
                .summaryStatistics();

        return new StatisticsItauOutput(
                doubleSummaryStatistics.getCount(),
                doubleSummaryStatistics.getSum(),
                doubleSummaryStatistics.getAverage(),
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getAverage()
        );
    }
}
