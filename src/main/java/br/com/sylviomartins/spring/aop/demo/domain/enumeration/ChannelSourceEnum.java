package br.com.sylviomartins.spring.aop.demo.domain.enumeration;

import lombok.Getter;

public enum ChannelSourceEnum {

    BANKLINE("bankline"),
    MOBILE("mobile"),
    LEGADO("legado");

    @Getter
    private final String name;

    ChannelSourceEnum(final String name) {
        this.name = name;
    }

    public String toStringValue() {
        return name;
    }

}
