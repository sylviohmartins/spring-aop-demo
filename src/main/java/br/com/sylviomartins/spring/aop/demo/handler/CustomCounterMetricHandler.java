package br.com.sylviomartins.spring.aop.demo.handler;

import br.com.sylviomartins.spring.aop.demo.domain.converter.CustomConverter;
import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.Attribute;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomMetric;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomSum;
import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomTag;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static br.com.sylviomartins.spring.aop.demo.util.AttributeUtils.isExecuteMethod;
import static br.com.sylviomartins.spring.aop.demo.util.MetricUtils.format;
import static br.com.sylviomartins.spring.aop.demo.util.ReflectionUtils.executeMethod;
import static br.com.sylviomartins.spring.aop.demo.util.ReflectionUtils.getFieldValue;
import static br.com.sylviomartins.spring.aop.demo.util.TagUtils.createTag;
import static java.util.Objects.nonNull;

/**
 * <h1>CounterMetricHandler</h1>
 * <p>Manipulador de métricas do contador.</p>
 * <p>Este manipulador é responsável por medir e registrar métricas do tipo contador para as operações executadas.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Component
@AllArgsConstructor
public class CustomCounterMetricHandler extends BaseMetricHandler {

    private final CustomConverter customConverter;

    @Override
    protected void execute(final Object[] args, final Metric metric) {
        metric.setName(format(metric.getName(), "custom"));

        final CustomMetric customMetric = metric.getCustomMetric();

        for (final Object result : args) {
            if (customMetric.getParentObjectType().isInstance(result)) {
                List<Tag> customTags = createCustomTags(customMetric.getCustomTag(), result);

                metric.getTags().addAll(customTags);

                List<Object> customSums = createCustomSum(customMetric.getCustomSum(), result);

                customSums.forEach(value -> sendCustomSumMetric(metric, value));

                sendCustomCounterMetric(metric);
            }

        }

    }

    private List<Tag> createCustomTags(final CustomTag customTag, final Object result) {
        final List<Tag> tags = new LinkedList<>();

        for (final Attribute attribute : customTag.getAttributes()) {
            try {
                Object fieldValue = getFieldValue(result, attribute);

                Object convertedFieldValue = customConverter.convertToValue(fieldValue, attribute.getObjectType());

                if (nonNull(attribute.getMethod()) && !isExecuteMethod(attribute.getMethod().getTargetClass())) {
                    convertedFieldValue = executeMethod(attribute.getMethod().getTargetClass(), attribute.getMethod().getName(), result, convertedFieldValue);
                }

                tags.add(createTag(customTag.getName(), convertedFieldValue != null ? convertedFieldValue.toString() : ""));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return tags;
    }

    private List<Object> createCustomSum(final CustomSum customSum, final Object result) {
        final List<Object> values = new LinkedList<>();

        for (final Attribute attribute : customSum.getAttributes()) {
            try {
                Object fieldValue = getFieldValue(result, attribute);

                Object convertedFieldValue = customConverter.convertToValue(fieldValue, attribute.getObjectType());

                if (nonNull(attribute.getMethod()) && !isExecuteMethod(attribute.getMethod().getTargetClass())) {
                    convertedFieldValue = executeMethod(attribute.getMethod().getTargetClass(), attribute.getMethod().getName(), result, convertedFieldValue);
                }

                values.add(convertedFieldValue);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    private void sendCustomSumMetric(final Metric metric, Object value) {
        metric.setValue(value);
        metric.setObjective("sum");

        sendCounter(metric);
    }

    private void sendCustomCounterMetric(final Metric metric) {
        metric.setValue(metric.getValue());
        metric.setObjective("counter");

        sendCounter(metric);
    }

}
