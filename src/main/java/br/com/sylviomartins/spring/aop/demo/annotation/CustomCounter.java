package br.com.sylviomartins.spring.aop.demo.annotation;

import br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag;

import java.lang.annotation.*;

/**
 * A anotação <code>CustomCounter</code> é usada para marcar um método com informações de métricas de contador personalizadas.
 * Pode ser aplicada apenas a métodos.
 *
 * <p>
 * A anotação permite especificar um chave de habilitação, nome, descrição e tags para a métrica personalizada.
 * Também é possível fornecer uma classe de origem para as tags personalizadas e um atributo na classe de origem para o valor personalizado.
 * </p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomCounter {

    /**
     * Obtém a chave de habilitação para a métrica personalizada.
     *
     * @return A chave de habilitação.
     */
    String enabledKey();

    /**
     * Obtém o nome da métrica personalizada.
     *
     * @return O nome da métrica personalizada.
     */
    String name();

    /**
     * Obtém a descrição da métrica personalizada.
     *
     * @return A descrição da métrica personalizada.
     */
    String description() default "";

    /**
     * Obtém as tags associadas à métrica personalizada.
     *
     * @return As tags associadas à métrica personalizada.
     */
    Tag[] tags() default {};

    /**
     * Obtém a classe de origem das tags personalizadas.
     *
     * @return A classe de origem das tags personalizadas.
     */
    Class<?> sourceCustomTags();

    /**
     * Obtém o nome do atributo na classe de origem para o valor personalizado.
     *
     * @return O nome do atributo para o valor personalizado.
     */
    String sourceCustomValue();

}
