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

    /**
     * Implementação do método abstrato {@link BaseMetricHandler#execute(Object[], Metric)}.
     * Este método é chamado para executar a ação de medição e registro das métricas personalizadas do tipo contador.
     *
     * @param args   Os argumentos do método interceptado.
     * @param metric A métrica a ser registrada.
     */
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

    /**
     * Cria tags personalizadas com base nas anotações {@link TagField} presentes na classe da métrica.
     *
     * @param args   Os argumentos do método interceptado.
     * @param metric A métrica para a qual as tags personalizadas estão sendo criadas.
     * @return A lista de tags personalizadas.
     */
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

    /**
     * Converte o valor de um campo da classe da métrica para o formato apropriado.
     *
     * @param sourceResult O objeto do qual o valor do campo está sendo extraído.
     * @param attribute    O nome do campo.
     * @return O valor convertido do campo.
     */
    private Object convertField(final Object sourceResult, final String attribute) {
        try {
            final Object fieldValue = getFieldValue(sourceResult, attribute);

            return customConverter.convertToValue(fieldValue, fieldValue.getClass());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error converting custom tag field.", e);
        }
    }

    /**
     * Envia a métrica personalizada com o valor apropriado.
     *
     * @param metric    A métrica a ser enviada.
     * @param value     O valor a ser registrado na métrica.
     * @param objective O objetivo da métrica (por exemplo, "count" ou "sum").
     */
    private void sendCustomMetric(final Metric metric, final Object value, final String objective) {
        metric.setValue(value);
        metric.setObjective(objective);

        sendCounter(metric);
    }

}
