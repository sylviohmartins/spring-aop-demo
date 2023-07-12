package br.com.sylviomartins.spring.aop.demo.handler;

import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * <h1>TimerMetricHandler</h1>
 * <p>Manipulador de métricas de tempo.</p>
 * <p>Este manipulador é responsável por medir e registrar métricas do tipo tempo para as operações executadas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class TimerMetricHandler extends BaseMetricHandler {

    @Override
    protected void execute(final Object[] args, final Metric metric) {
        sendTimer(metric);
    }

}
