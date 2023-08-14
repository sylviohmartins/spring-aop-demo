package br.com.sylviomartins.spring.aop.demo.annotation.nested;

import java.lang.annotation.*;

/**
 * A anotação <code>Tag</code> é usada para marcar métodos ou tipos com informações de tag.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Tag {

    /**
     * Obtém a chave da tag.
     *
     * @return A chave da tag.
     */
    String key();

    /**
     * Obtém o valor da tag.
     *
     * @return O valor da tag.
     */
    String value();

}
