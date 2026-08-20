package br.com.daniel.java.quarkus.general.core.usecase;

import br.com.daniel.java.quarkus.general.core.usecase.output.StatisticsItauOutput;

/**
 * Interface responsável por definir o contrato para o cálculo de estatísticas
 * relacionadas às transações do Itaú dentro de um intervalo de tempo específico.
 */
public interface StatiticsTransactionItauUseCase {

    /**
     * Calcula estatísticas para transações do Itaú dentro de um intervalo de tempo fornecido.
     *
     * <p>As estatísticas incluem:
     * <ul>
     *   <li>Contagem de transações</li>
     *   <li>Soma dos valores das transações</li>
     *   <li>Média dos valores das transações</li>
     *   <li>Valor mínimo das transações</li>
     *   <li>Valor máximo das transações</li>
     * </ul>
     * </p>
     *
     * @param secondsRange O intervalo de tempo em segundos a ser considerado para as estatísticas.
     *                     Deve ser um valor inteiro positivo.
     * @return Um objeto {@link StatisticsItauOutput} contendo os resultados das estatísticas calculadas.
     */
    StatisticsItauOutput calculateStatistics(Integer secondsRange);
}
