package br.com.sylviomartins.spring.aop.demo.controller;

import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.domain.dto.BoletoDTO;
import br.com.sylviomartins.spring.aop.demo.domain.mapper.BoletoMapper;
import br.com.sylviomartins.spring.aop.demo.domain.vo.BoletoVO;
import br.com.sylviomartins.spring.aop.demo.exception.AlreadyExistsException;
import br.com.sylviomartins.spring.aop.demo.exception.RecordNotFoundException;
import br.com.sylviomartins.spring.aop.demo.service.BoletoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/boletos")
@RequiredArgsConstructor
public class BoletoController {

    private final BoletoService boletoService;

    private final BoletoMapper boletoMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO include(@Valid @RequestBody final BoletoVO data) throws AlreadyExistsException {
        final Boleto boleto = boletoService.include(boletoMapper.toEntity(data));

        return boletoMapper.toDTO(boleto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = BaseController.ID_PATH_VARIABLE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BoletoDTO authorize(@PathVariable("id") final UUID id, @Valid @RequestBody final BoletoVO data) throws RecordNotFoundException {
        final Boleto boleto = boletoService.authorize(boletoMapper.toEntity(data, id));

        return boletoMapper.toDTO(boleto);
    }

}
