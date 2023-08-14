package br.com.sylviomartins.spring.aop.demo.util;

import io.micrometer.core.instrument.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.sylviomartins.spring.aop.demo.util.MetricUtils.format;

/**
 * A classe <code>TagUtils</code> fornece utilitários para criação e manipulação de tags.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

    /**
     * Cria uma tag com a chave e o valor fornecidos.
     *
     * @param key   A chave da tag.
     * @param value O valor da tag.
     * @return A tag criada.
     */
    public static Tag createTag(final String key, final String value) {
        return Tag.of(key != null ? key : "", value != null ? value : "");
    }

    /**
     * Cria uma tag padrão com o nome do método.
     *
     * @param joinPoint O ponto de corte da execução do método.
     * @return A tag padrão criada.
     */
    public static Tag createDefault(final ProceedingJoinPoint joinPoint) {
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();

        return createTag("methodName", format(className, methodName));
    }

    /**
     * Cria uma tag para indicar falha.
     *
     * @return A tag de falha.
     */
    public static Tag createFail() {
        return createTag("outcome", "FAIL");
    }

    /**
     * Cria uma tag para indicar uma exceção.
     *
     * @param unknownException A exceção desconhecida.
     * @return A tag de exceção.
     */
    public static Tag createException(final Throwable unknownException) {
        final String value = unknownException != null ? unknownException.getClass().getName() : "";

        return createTag("exception", value);
    }

    /**
     * Cria uma tag para indicar sucesso.
     *
     * @return A tag de sucesso.
     */
    public static Tag createSuccess() {
        return createTag("outcome", "SUCCESS");
    }

    /**
     * Recupera as tags com base nos valores fornecidos.
     *
     * @param joinPoint O ponto de corte da execução do método.
     * @param arrayTags As tags fornecidas.
     * @return A lista de tags.
     */
    public static List<Tag> retrieveTags(final ProceedingJoinPoint joinPoint, final br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag[] arrayTags) {
        if (arrayTags == null) {
            return new LinkedList<>();
        }

        final List<Tag> tags = Arrays.stream(arrayTags)
                .map(tag -> createTag(tag.key(), tag.value()))
                .collect(Collectors.toList());

        tags.add(createDefault(joinPoint));

        return tags;
    }

}
