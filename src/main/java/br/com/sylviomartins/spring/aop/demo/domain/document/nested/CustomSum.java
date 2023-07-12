package br.com.sylviomartins.spring.aop.demo.domain.document.nested;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomSum {

    private String name;

    private List<Attribute> attributes;

}
