package br.com.sylviomartins.spring.aop.demo.service;

import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import br.com.sylviomartins.spring.aop.demo.domain.mapper.BoletoMapper;
import br.com.sylviomartins.spring.aop.demo.exception.AlreadyExistsException;
import br.com.sylviomartins.spring.aop.demo.exception.RecordNotFoundException;
import br.com.sylviomartins.spring.aop.demo.repository.BoletoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoletoService.class);

    private final BoletoRepository boletoRepository;

    private final BoletoMapper boletoMapper;

    @Value("${retry.maxAttempts:3}")
    private int maxAttempts;

    @Value("${retry.delay:1000}")
    private long delay;

    @Value("#{'${retry.exceptions}'.split(',')}")
    private List<Class<? extends Exception>> retryExceptions;

    /*@CustomCounter( //
            name = "inclusao", //
            description = "Custom Counter Example", //
            tags = @Tag(name = "serviceName", value = "inclusao"), //
            parentObjectType = Boleto.class, //
            customSum = @CustomSum( //
                    attributes = { //
                            @Attribute( //
                                    fieldName = "paymentAmount", //
                                    objectType = Double.class //
                            ) //
                    } //
            ), //
            customTag = @CustomTag( //
                    name = "tipo", //
                    attributes = { //
                            @Attribute( //
                                    fieldName = "status", //
                                    objectType = StatusEnum.class, //
                                    method = @Method(targetClass = CustomMethodUtils.class, methodName = GET_STATUS_TYPE) //
                            ) //
                    } //
            ) //
    )*/
    public Boleto include(final Boleto boleto) throws AlreadyExistsException {
        boleto.setStatus(StatusEnum.INCLUIDO);

        try {
            return performWithRetry(() -> {
                boletoRepository.save(boleto);
                return boleto;
            });

        } catch (final Exception unknownException) {
            throw new AlreadyExistsException("Já existe um registro com os dados informados.");
        }
    }

    public Boleto authorize(final Boleto boleto) throws RecordNotFoundException {
        boleto.setStatus(StatusEnum.AUTORIZADO);

        try {
            final Boleto persisted = boletoRepository.findById(boleto.getId()).orElseThrow(RecordNotFoundException::new);
            return performWithRetry(() -> {
                boletoMapper.merge(persisted, boleto);
                return boletoRepository.save(persisted);
            });

        } catch (final Exception unknownException) {
            throw new RecordNotFoundException();
        }
    }

    private <T> T performWithRetry(RetryableAction<T> action) throws Exception {
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

    private boolean isRetryException(Exception ex) {
        return retryExceptions.stream().anyMatch(e -> e.isInstance(ex));
    }

    private void logRetryAttempt(int retryCount, String reason, int maxAttempts) {
        LOGGER.warn("[Retry: {}/{} - Reason: '{}']", retryCount, maxAttempts, reason);
    }

    @FunctionalInterface
    private interface RetryableAction<T> {
        T execute() throws Exception;
    }
}
