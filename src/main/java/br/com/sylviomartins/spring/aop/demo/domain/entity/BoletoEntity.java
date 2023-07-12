package br.com.sylviomartins.spring.aop.demo.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class BoletoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

}
