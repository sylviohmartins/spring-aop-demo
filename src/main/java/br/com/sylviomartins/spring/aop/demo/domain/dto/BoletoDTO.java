package br.com.sylviomartins.spring.aop.demo.domain.dto;


import br.com.sylviomartins.spring.aop.demo.domain.enumeration.ChannelSourceEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.MethodEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public record BoletoDTO(
        UUID id,

        LocalDate date,

        MethodEnum method,

        ChannelSourceEnum channelSource,

        Double paymentAmount,

        Double maximumPaymentAmount,

        StatusEnum status

) {
}
