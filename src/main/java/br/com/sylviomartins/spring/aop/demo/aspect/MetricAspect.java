package br.com.sylviomartins.spring.aop.demo.aspect;

import br.com.sylviomartins.spring.aop.demo.annotation.Counter;
import br.com.sylviomartins.spring.aop.demo.annotation.CustomCounter;
import br.com.sylviomartins.spring.aop.demo.annotation.Timer;
import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.Attribute;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomMetric;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomSum;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomTag;
import br.com.sylviomartins.spring.aop.demo.exception.HandlerException;
import br.com.sylviomartins.spring.aop.demo.exception.ProccedUnknownException;
import br.com.sylviomartins.spring.aop.demo.handler.CounterMetricHandler;
import br.com.sylviomartins.spring.aop.demo.handler.CustomCounterMetricHandler;
import br.com.sylviomartins.spring.aop.demo.handler.TimerMetricHandler;
import br.com.sylviomartins.spring.aop.demo.util.AttributeUtils;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static br.com.sylviomartins.spring.aop.demo.util.TagUtils.retrieveTags;


/**
 * <h1>MetricAspect</h1>
 * <p>Classe que representa um aspecto para lidar com métricas.</p>
 * <p>Ela intercepta métodos anotados com as anotações @Counter ou @Timer e processa as métricas correspondentes.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricAspect.class);

    private static final String ERROR_MESSAGE = "Erro desconhecido ao processar a métrica: {}";

    private final CounterMetricHandler counterMetricHandler;

    private final CustomCounterMetricHandler customCounterMetricHandler;

    private final TimerMetricHandler timerMetricHandler;

    /**
     * Intercepta métodos anotados com @Counter e processa a métrica de contador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param counter   A anotação Counter aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(counter)")
    public Object counterAspect(final ProceedingJoinPoint joinPoint, final Counter counter) throws ProccedUnknownException {
        if (!counter.isEnabled()) {
            return handleDisabledMetric(counter.name());
        }

        final List<Tag> tags = retrieveTags(joinPoint, counter.tags());

        final Metric metric = Metric.builder() //
                .name(counter.name()) //
                .description(counter.description()) //
                .tags(tags) //
                .value(1.0) //
                .build();

        try {
            return counterMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    @Around("@annotation(customCounter)")
    public Object customCounterAspect(final ProceedingJoinPoint joinPoint, final CustomCounter customCounter) throws ProccedUnknownException {
        if (!customCounter.isEnabled()) {
            return handleDisabledMetric(customCounter.name());
        }

        final List<Tag> tags = retrieveTags(joinPoint, customCounter.tags());

        final List<Attribute> sumAttributes = Arrays.stream(customCounter.customSum().attributes()) //
                .map(AttributeUtils::createAttribute) //
                .toList();

        final CustomSum customSum = CustomSum.builder() //
                .attributes(sumAttributes) //
                .build();

        final List<Attribute> tagAttributes = Arrays.stream(customCounter.customTag().attributes()) //
                .map(AttributeUtils::createAttribute) //
                .toList();

        final CustomTag customTag = CustomTag.builder() //
                .name(customCounter.customTag().name()) //
                .attributes(tagAttributes) //
                .build();

        final CustomMetric customMetric = CustomMetric.builder()
                .parentObjectType(customCounter.parentObjectType()) //
                .customSum(customSum) //
                .customTag(customTag) //
                .build();

        final Metric metric = Metric.builder() //
                .name(customCounter.name()) //
                .description(customCounter.description()) //
                .tags(tags) //
                .value(1.0) //
                .customMetric(customMetric) //
                .build();

        try {
            return customCounterMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    /**
     * Intercepta métodos anotados com @Timer e processa a métrica de temporizador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param timer     A anotação Timer aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(timer)")
    public Object timerAspect(final ProceedingJoinPoint joinPoint, final Timer timer) throws ProccedUnknownException {
        if (!timer.isEnabled()) {
            return handleDisabledMetric(timer.name());
        }

        final List<Tag> tags = retrieveTags(joinPoint, timer.tags());

        final Metric metric = Metric.builder() //
                .name(timer.name()) //
                .description(timer.description()) //
                .tags(tags) //
                .build();

        try {
            return timerMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    /**
     * Lida com o caso em que uma métrica está desabilitada.
     * Este método registra uma mensagem indicando que a métrica está desabilitada.
     *
     * @param metricName O nome da métrica desabilitada.
     * @return null
     */
    private Object handleDisabledMetric(final String metricName) {
        LOGGER.info("O registro da métrica '{}' está desabilitado", metricName);
        return null;
    }

}
