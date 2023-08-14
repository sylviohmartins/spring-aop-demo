package br.com.sylviomartins.spring.aop.demo.annotation.nested;

import java.lang.annotation.*;

/**
 * A anotação <code>TagField</code> é usada para marcar campos de uma classe que são usados para criar tags personalizadas.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TagField {

    /**
     * Obtém a classe que deve ser comparada ao objeto para determinar se a anotação deve ser aplicada.
     *
     * @return A classe alvo.
     */
    Class<?> clazz();

    /**
     * Obtém os nomes dos campos na classe alvo que serão usados para criar tags.
     *
     * @return Os nomes dos campos.
     */
    String[] fields();

    /**
     * Obtém a classe que contém o método de expressão para avaliar a tag personalizada.
     *
     * @return A classe de expressão.
     */
    Class<?> expressionClass() default Void.class;

    /**
     * Obtém o nome do método de expressão para avaliar a tag personalizada.
     *
     * @return O nome do método de expressão.
     */
    String expressionMethod() default "";

    /**
     * Obtém a chave da tag personalizada.
     *
     * @return A chave da tag personalizada.
     */
    String key();

}
