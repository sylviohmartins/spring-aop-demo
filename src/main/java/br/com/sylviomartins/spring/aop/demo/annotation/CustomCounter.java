package br.com.sylviomartins.spring.aop.demo.annotation;

import br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomCounter {

    boolean isEnabled() default true;

    String name();

    String description() default "";

    Tag[] tags() default {};

    Class<?> sourceCustomTags();

    String sourceCustomValue();

}
