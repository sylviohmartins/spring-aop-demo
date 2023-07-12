package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoubleUtils {

    /**
     * Verifica se o pagamento é integral com base nos valores do pagamento e valor máximo do pagamento.
     *
     * @param paymentAmount        O valor do pagamento.
     * @param maximumPaymentAmount O valor máximo do pagamento.
     * @return true se o pagamento for integral, caso contrário, false.
     */
    public static boolean isPaymentIntegral(final Double paymentAmount, final Double maximumPaymentAmount) {
        return paymentAmount.equals(maximumPaymentAmount);
    }

}
