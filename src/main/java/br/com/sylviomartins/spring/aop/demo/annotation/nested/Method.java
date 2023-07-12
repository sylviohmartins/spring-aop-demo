package br.com.sylviomartins.spring.aop.demo.annotation.nested;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Method {

    Class<?> targetClass() default Void.class;

    String methodName() default "";

}
