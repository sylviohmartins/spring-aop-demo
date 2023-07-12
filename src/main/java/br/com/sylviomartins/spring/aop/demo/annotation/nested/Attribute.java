package br.com.sylviomartins.spring.aop.demo.annotation.nested;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Attribute {

    String fieldName();

    Class<?> objectType();

    Method method() default @Method; // Add default value if needed

}
