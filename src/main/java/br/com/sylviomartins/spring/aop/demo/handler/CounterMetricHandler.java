package br.com.sylviomartins.spring.aop.demo.handler;

import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A classe <code>CounterMetricHandler</code> é um manipulador de métricas do contador.
 * Este manipulador é responsável por medir e registrar métricas do tipo contador para as operações executadas.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class CounterMetricHandler extends BaseMetricHandler {

    /**
     * Implementação do método abstrato {@link BaseMetricHandler#execute(Object[], Metric)}.
     * Este método é chamado para executar a ação de medição e registro da métrica do tipo contador.
     *
     * @param args   Os argumentos do método interceptado.
     * @param metric A métrica a ser registrada.
     */
    @Override
    protected void execute(final Object[] args, final Metric metric) {
        sendCounter(metric);
    }

}
