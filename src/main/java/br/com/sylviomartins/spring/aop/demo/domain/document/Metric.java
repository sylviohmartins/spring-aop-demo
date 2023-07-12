package br.com.sylviomartins.spring.aop.demo.domain.document;

import br.com.sylviomartins.spring.aop.demo.domain.document.nested.CustomMetric;
import io.micrometer.core.instrument.Tag;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    private String name;

    private String description;

    private List<Tag> tags;

    private Object value;

    private String objective;

    private CustomMetric customMetric;

}
