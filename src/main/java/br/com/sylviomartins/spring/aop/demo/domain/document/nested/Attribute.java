package br.com.sylviomartins.spring.aop.demo.domain.document.nested;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    private String name;

    private Class<?> objectType;

    private Method method;

}
