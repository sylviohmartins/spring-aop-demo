package br.com.sylviomartins.spring.aop.demo.handler;

import br.com.sylviomartins.spring.aop.demo.annotation.nested.TagField;
import br.com.sylviomartins.spring.aop.demo.domain.converter.CustomConverter;
import br.com.sylviomartins.spring.aop.demo.domain.document.Metric;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static br.com.sylviomartins.spring.aop.demo.util.AttributeUtils.isExecuteMethod;
import static br.com.sylviomartins.spring.aop.demo.util.ReflectionUtils.executeMethod;
import static br.com.sylviomartins.spring.aop.demo.util.ReflectionUtils.getFieldValue;
import static br.com.sylviomartins.spring.aop.demo.util.TagUtils.createTag;

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
        final List<Tag> customTags = createCustomTags(args, metric);
        metric.getTags().addAll(customTags);

        sendCustomMetric(metric, metric.getValue(), "count");

        for (Object sourceResult : args) {
            final Object customValue = convertField(sourceResult, metric.getSourceCustomValue());

            sendCustomMetric(metric, customValue, "sum");
        }
    }

    private List<Tag> createCustomTags(final Object[] args, final Metric metric) {
        final Class<?> sourceCustomTags = metric.getSourceCustomTags();

        final List<Tag> tags = new LinkedList<>();

        for (Field field : sourceCustomTags.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(TagField.class)) {
                final TagField annotation = field.getAnnotation(TagField.class);

                for (final Object sourceResult : args) {
                    if (sourceResult.getClass().isAssignableFrom(annotation.clazz())) {
                        List<Object> convertedFieldValues = Arrays.stream(annotation.fields()) //
                                .map(attribute -> convertField(sourceResult, attribute)) //
                                .toList();

                        Object convertedFieldValue = convertedFieldValues.stream() //
                                .findFirst() //
                                .orElse(null);

                        if (!isExecuteMethod(annotation.expressionClass())) {
                            convertedFieldValue = executeMethod(annotation.expressionClass(), annotation.expressionMethod(), sourceResult, convertedFieldValues);
                        }

                        tags.add(createTag(annotation.key(), String.valueOf(convertedFieldValue)));
                    }
                }
            }
        }

        return tags;
    }

    private Object convertField(final Object sourceResult, final String attribute) {
        try {
            final Object fieldValue = getFieldValue(sourceResult, attribute);

            return customConverter.convertToValue(fieldValue, fieldValue.getClass());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error converting custom tag field.", e);
        }
    }

    private void sendCustomMetric(final Metric metric, final Object value, final String objective) {
        metric.setValue(value);
        metric.setObjective(objective);

        sendCounter(metric);
    }

}
