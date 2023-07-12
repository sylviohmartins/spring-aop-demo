package br.com.sylviomartins.spring.aop.demo.domain.vo;


import br.com.sylviomartins.spring.aop.demo.domain.enumeration.ChannelSourceEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.MethodEnum;

import java.time.LocalDate;
import java.util.UUID;

public record BoletoVO(

        UUID id,

        LocalDate date,

        ChannelSourceEnum channelSource,

        MethodEnum method,

        Double paymentAmount,

        Double maximumPaymentAmount

) {
}
