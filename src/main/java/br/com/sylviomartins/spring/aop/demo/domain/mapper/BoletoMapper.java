package br.com.sylviomartins.spring.aop.demo.domain.mapper;

import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.domain.dto.BoletoDTO;
import br.com.sylviomartins.spring.aop.demo.domain.vo.BoletoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BoletoMapper {

    BoletoDTO toDTO(Boleto source);

    @Mapping(target = "status", ignore = true)
    Boleto toEntity(BoletoVO source);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "channelSource", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "paymentAmount", ignore = true)
    @Mapping(target = "maximumPaymentAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    Boleto toEntity(BoletoVO source, UUID id);

    @Mapping(source = "modified.id", target = "id", ignore = true)
    @Mapping(source = "modified.date", target = "date", ignore = true)
    @Mapping(source = "modified.method", target = "method", ignore = true)
    void merge(@MappingTarget Boleto persisted, Boleto modified);

}
