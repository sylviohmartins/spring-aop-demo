package br.com.sylviomartins.spring.aop.demo.util;

import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomMethodUtils {

    private static LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    @SuppressWarnings("unused")
    public static String isPaymentToday(final LocalDate date) {
        return getCurrentDate().isEqual(date) ? "DIA" : "AGENDADA";
    }

    @SuppressWarnings("unused")
    public static String isPaymentFull(final Double paymentAmount, final Double maximumPaymentAmount) {
        return paymentAmount.equals(maximumPaymentAmount) ? "INTEGRAL" : "PARCIAL";
    }

    @SuppressWarnings("unused")
    public static String getStatusType(final StatusEnum status) {
        Map<StatusEnum, String> statusTypeMap = new HashMap<>();

        statusTypeMap.put(StatusEnum.INCLUIDO, null);
        statusTypeMap.put(StatusEnum.AUTORIZADO, null);

        if (!statusTypeMap.containsKey(status)) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        return statusTypeMap.get(status);
    }

}
