package br.com.sylviomartins.spring.aop.demo.util;

import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomMethodUtils {

    public static final String IS_PAYMENT_TODAY = "isPaymentToday";

    public static final String IS_PAYMENT_FULL = "isPaymentFull";

    public static final String GET_STATUS_TYPE = "getStatusType";


    private static LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    public static String isPaymentToday(final LocalDate date) {
        return getCurrentDate().isEqual(date) ? "dia" : "agendada";
    }

    public static boolean isPaymentFull(final Double paymentAmount, final Double maximumPaymentAmount) {
        return paymentAmount.equals(maximumPaymentAmount);
    }

    public static String getStatusType(StatusEnum status) {
        Map<StatusEnum, String> statusTypeMap = new HashMap<>();

        statusTypeMap.put(StatusEnum.INCLUIDO, null);
        statusTypeMap.put(StatusEnum.AUTORIZADO, null);

        if (!statusTypeMap.containsKey(status)) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        return statusTypeMap.get(status);
    }


}
