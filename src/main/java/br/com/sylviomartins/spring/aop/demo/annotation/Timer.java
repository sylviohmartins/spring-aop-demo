package br.com.sylviomartins.spring.aop.demo.annotation;

import br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag;

import java.lang.annotation.*;

/**
 * <h1>Timer</h1>
 * <p>Esta anotação é usada para marcar um método com informações de métricas de tempo.</p>
 * <p>Pode ser aplicada apenas a métodos.</p>
 *
 * <p>
 * A anotação pode ser usada para ativar ou desativar o registro da métrica, fornecer um nome
 * e uma descrição para a métrica e associar tags a ela.
 * </p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timer {

    /**
     * Indica se o registro da métrica está ativado ou desativado.
     *
     * @return true se o registro da métrica estiver ativado, false caso contrário.
     */
    boolean isEnabled() default true;

    /**
     * Obtém o nome da métrica.
     *
     * @return o nome da métrica.
     */
    String name();

    /**
     * Obtém a descrição da métrica.
     *
     * @return a descrição da métrica.
     */
    String description() default "";

    /**
     * Obtém as tags associadas à métrica.
     *
     * @return as tags associadas à métrica.
     */
    Tag[] tags() default {};

}
