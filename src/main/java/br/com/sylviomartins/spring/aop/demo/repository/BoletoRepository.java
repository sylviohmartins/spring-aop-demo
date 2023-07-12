package br.com.sylviomartins.spring.aop.demo.repository;

import br.com.sylviomartins.spring.aop.demo.domain.document.Boleto;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.MethodEnum;
import br.com.sylviomartins.spring.aop.demo.domain.enumeration.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BoletoRepository {

    private final static UUID ID = UUID.fromString("3f1eeb33-8624-41f0-a1c8-0132ae813596");

    @SneakyThrows
    public Boleto save(final Boleto boleto) {
        if (true) {
            throw new SocketTimeoutException("Só um teste, viu?!");
        }

        return boleto;
    }

    public Optional<Boleto> findById(final UUID id) {
        if (ID.equals(id)) {
            final Boleto boleto = new Boleto();

            boleto.setId(ID);
            boleto.setDate(LocalDate.now());
            boleto.setMethod(MethodEnum.ITAU);
            boleto.setStatus(StatusEnum.INCLUIDO);

            return Optional.of(boleto);
        }

        return Optional.empty();
    }

}
