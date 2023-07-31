package br.com.sylviomartins.spring.aop.demo.service;

import br.com.sylviomartins.spring.aop.demo.annotation.CustomCounter;
import br.com.sylviomartins.spring.aop.demo.annotation.nested.Tag;
import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import br.com.sylviomartins.spring.aop.demo.domain.mapper.BoletoMapper;
import br.com.sylviomartins.spring.aop.demo.domain.vo.InclusaoCustomTagsVO;
import br.com.sylviomartins.spring.aop.demo.exception.AlreadyExistsException;
import br.com.sylviomartins.spring.aop.demo.exception.RecordNotFoundException;
import br.com.sylviomartins.spring.aop.demo.repository.BoletoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
@RequiredArgsConstructor
public class BoletoService {

    private final BoletoRepository boletoRepository;

    private final BoletoMapper boletoMapper;

    @CustomCounter( //
            name = "inclusao", //
            description = "Custom Counter Example", //
            tags = @Tag(key = "serviceName", value = "inclusao"),
            sourceCustomTags = InclusaoCustomTagsVO.class,
            sourceCustomValue = "paymentAmount"
    )
    public Boleto include(final Boleto boleto) throws AlreadyExistsException {
        boleto.setStatus(StatusEnum.INCLUIDO);

        try {

            if (true) {
                throw new SocketTimeoutException("");
            }
            return boletoRepository.save(boleto);

        } catch (final Exception unknownException) {
            throw new AlreadyExistsException("Já existe um registro com os dados informados.");
        }
    }

    public Boleto authorize(final Boleto boleto) throws RecordNotFoundException {
        boleto.setStatus(StatusEnum.AUTORIZADO);

        final Boleto persisted = boletoRepository.findById(boleto.getId()).orElseThrow(RecordNotFoundException::new);

        try {
            boletoMapper.merge(persisted, boleto);

            return boletoRepository.save(persisted);

        } catch (final Exception unknownException) {
            throw new RecordNotFoundException();
        }
    }

}
