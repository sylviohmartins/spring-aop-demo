package br.com.sylviomartins.spring.aop.demo.domain.document.nested;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTag {

    private Class<?> source;

    private Class<?> target;

}
