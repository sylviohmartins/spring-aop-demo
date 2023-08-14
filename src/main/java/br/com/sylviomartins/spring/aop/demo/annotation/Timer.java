package br.com.sylviomartins.spring.aop.demo.annotation;

import br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag;

import java.lang.annotation.*;

/**
 * A anotação <code>Timer</code> é usada para marcar um método com informações de métricas de tempo.
 * Pode ser aplicada apenas a métodos.
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
     * Obtém a chave de habilitação para a métrica personalizada.
     *
     * @return A chave de habilitação.
     */
    String enabledKey();

    /**
     * Obtém o nome da métrica.
     *
     * @return O nome da métrica.
     */
    String name();

    /**
     * Obtém a descrição da métrica.
     *
     * @return A descrição da métrica.
     */
    String description() default "";

    /**
     * Obtém as tags associadas à métrica.
     *
     * @return As tags associadas à métrica.
     */
    Tag[] tags() default {};

}
