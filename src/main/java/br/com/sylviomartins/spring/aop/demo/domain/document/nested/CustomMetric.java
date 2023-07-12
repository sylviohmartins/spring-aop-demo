package br.com.sylviomartins.spring.aop.demo.domain.document.nested;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMetric {

    private Class<?> parentObjectType;

    private CustomSum customSum;

    private CustomTag customTag;

}
