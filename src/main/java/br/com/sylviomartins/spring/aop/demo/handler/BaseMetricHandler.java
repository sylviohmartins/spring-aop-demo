package br.com.sylviomartins.spring.aop.demo.handler;

import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import br.com.sylviomartins.spring.aop.demo.exception.HandlerException;
import br.com.sylviomartins.spring.aop.demo.exception.ProccedUnknownException;
import br.com.sylviomartins.spring.aop.demo.exception.ServiceException;
import br.com.sylviomartins.spring.aop.demo.service.PrometheusService;
import io.micrometer.core.instrument.Tag;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static br.com.sylviomartins.spring.aop.demo.util.TagUtils.*;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@NoArgsConstructor
public abstract class BaseMetricHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMetricHandler.class);
    public static final String ERROR_MESSAGE = "Erro ao solicitar o registro da métrica: {}";

    @Autowired
    private PrometheusService prometheusService;

    public void sendCounter(Metric metric) {
        try {
            prometheusService.counter(metric.getObjective(), metric.getName(), metric.getDescription(), null, metric.getTags(), (Double) metric.getValue());

        } catch (ServiceException serviceException) {
            LOGGER.error(ERROR_MESSAGE, metric.getName(), serviceException);
        }
    }

    public void sendTimer(Metric metric) {
        try {
            prometheusService.timer(metric.getName(), metric.getDescription(), metric.getTags(), (StopWatch) metric.getValue());

        } catch (ServiceException serviceException) {
            LOGGER.error(ERROR_MESSAGE, metric.getName(), serviceException);
        }
    }

    public Object process(ProceedingJoinPoint joinPoint, Metric metric) throws ProccedUnknownException, HandlerException {
        StopWatch watchRuntime = new StopWatch();
        watchRuntime.start();

        try {
            StopWatch watchProceed = new StopWatch();
            watchProceed.start();

            Object proceed = joinPoint.proceed();

            watchProceed.stop();

            defineMetricValue(metric, watchProceed);

            execute(null, metric, joinPoint.getArgs());

            return proceed;

        } catch (HandlerException handlerException) {
            throw handlerException;

        } catch (Throwable proccedUnknownException) {
            execute(proccedUnknownException, metric, joinPoint.getArgs());

            throw new ProccedUnknownException(proccedUnknownException.getMessage(), BAD_REQUEST);

        } finally {
            watchRuntime.stop();

            LOGGER.info("Tempo de execução: {} ms", watchRuntime.getTime(TimeUnit.MILLISECONDS));
        }
    }

    protected abstract void execute(Object[] args, Metric metric);

    private void defineMetricValue(Metric metric, StopWatch watchProceed) {
        if (this instanceof CounterMetricHandler || this instanceof CustomCounterMetricHandler) {
            metric.setValue(1.0);

        } else if (this instanceof TimerMetricHandler) {
            metric.setValue(watchProceed);
        }
    }

    public void execute(Throwable proccedUnknownException, Metric metric, Object[] args) throws HandlerException {
        try {
            List<Tag> executionTags = metric.getTags();

            if (isNull(proccedUnknownException)) {
                executionTags.add(createException(null));
                executionTags.add(createSuccess());
            } else {
                executionTags.add(createException(proccedUnknownException));
                executionTags.add(createFail());
            }

            metric.getTags().addAll(executionTags);

            this.execute(args, metric);

        } catch (Exception unknownException) {
            throw new HandlerException(unknownException.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

}
