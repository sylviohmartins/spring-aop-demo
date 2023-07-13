package br.com.sylviomartins.spring.aop.demo.aspect;

import br.com.sylviomartins.spring.aop.demo.annotation.Retryable;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <h1>RetryAspect</h1>
 * <p>Classe que representa um aspecto para lidar com retentativas.</p>
 * <p>Ela intercepta métodos anotados com a anotação @Retryable e executa as retentativas definidas.</p>
 * <p><strong>@since</strong> 13 de Julho de 2023</p>
 * <p><strong>@author</strong> Sylvio H. Martins</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RetryAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryAspect.class);

    @Value("${application.retry.enabled:false}")
    private boolean enabled;

    private final Environment env;

    @SuppressWarnings("unused")
    @Around("@annotation(retryable)")
    public Object retryableAspect(final ProceedingJoinPoint joinPoint, final Retryable retryable) throws Throwable {
        if (!enabled) {
            return joinPoint.proceed();
        }

        int maxAttempts = Integer.parseInt(this.env.resolvePlaceholders(retryable.maxAttemptsExpression()));
        long maxDelay = Long.parseLong(this.env.resolvePlaceholders(retryable.maxDelayExpression()));
        Class<? extends Throwable>[] retryExceptions = retryable.retryFor();
        Class<? extends Throwable>[] noRetryExceptions = retryable.noRetryFor();

        int retryCount = 0;
        Object result = null;

        while (retryCount < maxAttempts) {
            try {
                result = joinPoint.proceed();
                break;
            } catch (Exception ex) {
                if (isRetryException(ex, retryExceptions) && isNoRetryException(ex, noRetryExceptions)) {
                    LOGGER.warn("Retry attempt [{}/{}] failed - Reason: '{}'", retryCount + 1, maxAttempts, ex.getMessage());
                    Thread.sleep(maxDelay);
                }
                retryCount++;
            }
        }

        return result;
    }

    private boolean isRetryException(Exception ex, Class<? extends Throwable>[] retryExceptions) {
        for (Class<? extends Throwable> retryException : retryExceptions) {
            if (retryException.isInstance(ex)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNoRetryException(Exception ex, Class<? extends Throwable>[] noRetryExceptions) {
        return !isRetryException(ex, noRetryExceptions);
    }

}
