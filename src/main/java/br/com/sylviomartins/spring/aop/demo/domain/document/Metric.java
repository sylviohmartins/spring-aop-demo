package br.com.sylviomartins.spring.aop.demo.domain.document;

import io.micrometer.core.instrument.Tag;
import lombok.*;

import java.util.List;

/**
 * <h1>Metric</h1>
 * <p>
 * Classe que representa uma métrica para ser usada em operações de monitoramento e registro.
 * </p>
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    /**
     * O nome da métrica.
     */
    private String name;

    /**
     * A descrição da métrica.
     */
    private String description;

    /**
     * As tags associadas à métrica.
     */
    private List<Tag> tags;

    /**
     * O valor da métrica.
     */
    private Object value;

    /**
     * O valor personalizado da métrica proveniente da fonte.
     */
    private String sourceCustomValue;

    /**
     * O objetivo da métrica (por exemplo, "count" ou "sum").
     */
    private String objective;

    /**
     * A classe que fornece as tags personalizadas da métrica.
     */
    private Class<?> sourceCustomTags;

}
