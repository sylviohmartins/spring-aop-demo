package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    /**
     * Obtém a data atual.
     *
     * @return A data atual.
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    /**
     * Verifica se o pagamento é feito hoje com base na data fornecida.
     *
     * @param date A data fornecida.
     * @return true se o pagamento for hoje, caso contrário, false.
     */
    public static boolean isPaymentToday(final LocalDate date) {
        return getCurrentDate().isEqual(date);
    }

}
