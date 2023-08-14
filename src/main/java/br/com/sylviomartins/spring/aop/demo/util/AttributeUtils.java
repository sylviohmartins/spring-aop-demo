package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A classe <code>AttributeUtils</code> fornece utilitários relacionados a atributos e métodos.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeUtils {

    /**
     * Verifica se a classe do alvo é uma classe Void, que indica um método sem retorno.
     *
     * @param targetClass A classe a ser verificada.
     * @return {@code true} se a classe for uma classe Void, {@code false} caso contrário.
     */
    public static boolean isExecuteMethod(Class<?> targetClass) {
        return Void.class.getName().equals(targetClass.getName());
    }

}
