package br.com.sylviomartins.spring.aop.demo.aspect;

import br.com.sylviomartins.spring.aop.demo.annotation.Counter;
import br.com.sylviomartins.spring.aop.demo.annotation.CustomCounter;
import br.com.sylviomartins.spring.aop.demo.annotation.Timer;
import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import br.com.sylviomartins.spring.aop.demo.exception.HandlerException;
import br.com.sylviomartins.spring.aop.demo.exception.ProccedUnknownException;
import br.com.sylviomartins.spring.aop.demo.handler.CounterMetricHandler;
import br.com.sylviomartins.spring.aop.demo.handler.CustomCounterMetricHandler;
import br.com.sylviomartins.spring.aop.demo.handler.TimerMetricHandler;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static br.com.sylviomartins.spring.aop.demo.util.MetricUtils.format;
import static br.com.sylviomartins.spring.aop.demo.util.TagUtils.retrieveTags;


/**
 * A classe <code>MetricAspect</code> representa um aspecto para lidar com métricas.
 * Ela intercepta métodos anotados com as anotações {@link Counter}, {@link CustomCounter} ou {@link Timer}
 * e processa as métricas correspondentes.
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

    @Value("#{${application.metrics.enabled}}")
    private Map<String, Boolean> enabledKeys;

    private final CounterMetricHandler counterMetricHandler;

    private final CustomCounterMetricHandler customCounterMetricHandler;

    private final TimerMetricHandler timerMetricHandler;

    /**
     * Intercepta métodos anotados com {@link Counter} e processa a métrica de contador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param counter   A anotação {@link Counter} aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(counter)")
    public Object counterAspect(final ProceedingJoinPoint joinPoint, final Counter counter) throws ProccedUnknownException {
        if (!isEnabled(counter.enabledKey())) {
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

    /**
     * Intercepta métodos anotados com {@link CustomCounter} e processa a métrica de contador personalizado.
     *
     * @param joinPoint     O ponto de junção em execução que representa o método interceptado.
     * @param customCounter A anotação {@link CustomCounter} aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(customCounter)")
    public Object customCounterAspect(final ProceedingJoinPoint joinPoint, final CustomCounter customCounter) throws ProccedUnknownException {
        if (!isEnabled(customCounter.enabledKey())) {
            return handleDisabledMetric(customCounter.name());
        }

        final List<Tag> tags = retrieveTags(joinPoint, customCounter.tags());

        final Metric metric = Metric.builder() //
                .name(format(customCounter.name(), "custom")) //
                .description(customCounter.description()) //
                .tags(tags) //
                .sourceCustomTags(customCounter.sourceCustomTags()) //
                .value(1.0) //
                .sourceCustomValue(customCounter.sourceCustomValue()) //
                .build();

        try {
            return customCounterMetricHandler.process(joinPoint, metric);

        } catch (final HandlerException handlerException) {
            LOGGER.error(ERROR_MESSAGE, metric, handlerException);

            return null;
        }
    }

    /**
     * Intercepta métodos anotados com {@link Timer} e processa a métrica de temporizador.
     *
     * @param joinPoint O ponto de junção em execução que representa o método interceptado.
     * @param timer     A anotação {@link Timer} aplicada ao método.
     * @return O resultado do método interceptado.
     * @throws ProccedUnknownException Se ocorrer uma exceção durante a interceptação.
     */
    @SuppressWarnings("unused")
    @Around("@annotation(timer)")
    public Object timerAspect(final ProceedingJoinPoint joinPoint, final Timer timer) throws ProccedUnknownException {
        if (!isEnabled(timer.enabledKey())) {
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
     * Verifica se uma métrica personalizada está habilitada com base na chave fornecida.
     *
     * @param enabledKey A chave que indica se a métrica está habilitada.
     * @return {@code true} se a métrica estiver habilitada, {@code false} caso contrário.
     */
    private boolean isEnabled(final String enabledKey) {
        return enabledKeys.getOrDefault(enabledKey, false);
    }

    /**
     * Lida com o caso em que uma métrica está desabilitada.
     * Este método registra uma mensagem indicando que a métrica está desabilitada.
     *
     * @param metricName O nome da métrica desabilitada.
     * @return {@code null}
     */
    private Object handleDisabledMetric(final String metricName) {
        LOGGER.info("O registro da métrica '{}' está desabilitado", metricName);
        return null;
    }

}
