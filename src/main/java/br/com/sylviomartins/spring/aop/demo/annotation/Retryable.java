package br.com.sylviomartins.spring.aop.demo.annotation;

import br.com.sylviomartins.spring.aop.demo.exception.BusinessException;

import java.lang.annotation.*;
import java.net.SocketTimeoutException;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Retryable {

    Class<? extends Throwable>[] retryFor() default {SocketTimeoutException.class};

    Class<? extends Throwable>[] noRetryFor() default {BusinessException.class};

    String maxAttemptsExpression() default "3";

    String maxDelayExpression() default "1000";

}
