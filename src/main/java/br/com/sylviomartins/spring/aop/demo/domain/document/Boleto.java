package br.com.sylviomartins.spring.aop.demo.domain.document;

import br.com.sylviomartins.spring.aop.demo.domain.enumeration.ChannelSourceEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.MethodEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Boleto {

    private UUID id;

    private LocalDate date;

    private MethodEnum method;

    private ChannelSourceEnum channelSource;

    private Double paymentAmount;

    private Double maximumPaymentAmount;

    @JsonIgnore
    private StatusEnum status;

}
