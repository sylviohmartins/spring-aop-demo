package br.com.sylviomartins.spring.aop.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException() {
        super("Registro não encontrado");
    }

    public RecordNotFoundException(final String message) {
        super(message);
    }
}
