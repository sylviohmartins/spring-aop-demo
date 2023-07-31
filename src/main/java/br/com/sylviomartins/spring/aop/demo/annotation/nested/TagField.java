package br.com.sylviomartins.spring.aop.demo.annotation.nested;

import java.lang.annotation.*;

/**
 * <h1>Tag</h1>
 * <p>Anotação usada para marcar métodos ou tipos com informações de tag.</p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@autor</strong> Sylvio Humberto Martins</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TagField {

    Class<?> clazz();

    String[] fields();

    Class<?> expressionClass() default Void.class;

    String expressionMethod() default "";

    String key();
}
