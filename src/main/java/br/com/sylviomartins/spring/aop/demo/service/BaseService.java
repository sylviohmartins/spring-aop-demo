package br.com.sylviomartins.spring.aop.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public abstract class BaseService {

    @Value("${retry.maxAttempts}")
    private int maxAttempts;

    @Value("${retry.delay}")
    private long delay;

    @Value("#{'${retry.exceptions}'.split(',')}")
    private List<Class<? extends Exception>> retryExceptions;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    protected boolean isRetryException(Exception ex) {
        return retryExceptions.stream().anyMatch(e -> e.isInstance(ex));
    }

    protected void logRetryAttempt(int retryCount, String reason, int maxAttempts) {
        LOGGER.warn("[Retry: {}/{} - Reason: '{}']", retryCount, maxAttempts, reason);
    }

    protected <T> T performWithRetry(RetryableAction<T> action) throws Exception {
        int retryCount = 0;

        while (retryCount <= maxAttempts) {
            try {
                return action.execute();
            } catch (Exception ex) {
                if (retryCount < maxAttempts && isRetryException(ex)) {
                    logRetryAttempt(retryCount, ex.getMessage(), maxAttempts);
                    Thread.sleep(delay);
                }
                retryCount++;
            }
        }

        throw new Exception("Falha após várias tentativas");
    }

    @FunctionalInterface
    protected interface RetryableAction<T> {
        T execute() throws Exception;
    }
}
