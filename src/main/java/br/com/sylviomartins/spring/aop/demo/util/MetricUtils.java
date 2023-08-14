package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A classe <code>MetricUtils</code> fornece utilitários para manipulação de métricas.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MetricUtils {

    /**
     * Formata o nome concatenando uma lista de argumentos.
     *
     * @param args Os argumentos a serem concatenados.
     * @return O nome formatado.
     */
    public static String format(final String... args) {
        if (args == null) {
            return "";
        }

        final StringBuilder complementaryName = new StringBuilder(0);

        for (final String arg : args) {
            if (arg != null) {
                complementaryName.append(String.format("%s.", arg));
            }
        }

        int length = complementaryName.length();

        if (length > 0) {
            complementaryName.deleteCharAt(length - 1);
        }

        return complementaryName.toString();
    }

}
