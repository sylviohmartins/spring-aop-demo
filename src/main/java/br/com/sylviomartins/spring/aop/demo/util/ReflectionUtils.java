package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A classe <code>ReflectionUtils</code> fornece utilitários relacionados a reflexão.
 *
 * <p><strong>@since</strong> 19 de junho de 2023</p>
 * <p><strong>@author</strong> Sylvio Humberto Martins</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    /**
     * Obtém o valor de um campo em um objeto.
     *
     * @param result    O objeto alvo.
     * @param attribute O nome do campo.
     * @return O valor do campo.
     * @throws NoSuchFieldException   se o campo não for encontrado.
     * @throws IllegalAccessException se houver um erro de acesso ao campo.
     */
    public static Object getFieldValue(final Object result, final String attribute) throws NoSuchFieldException, IllegalAccessException {
        Field field = result.getClass().getDeclaredField(attribute);
        field.setAccessible(true);

        return field.get(result);
    }

    /**
     * Executa um método em um objeto alvo.
     *
     * @param targetClass     A classe que contém o método.
     * @param methodName      O nome do método.
     * @param targetObject    O objeto alvo.
     * @param attributeValues Os valores dos atributos.
     * @return O resultado da execução do método.
     */
    public static Object executeMethod(final Class<?> targetClass, final String methodName, final Object targetObject, final List<Object> attributeValues) {
        try {
            Class<?>[] parameterTypes = attributeValues.stream()
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);

            Method method = targetClass.getDeclaredMethod(methodName, parameterTypes);

            return method.invoke(targetObject, attributeValues.toArray());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null; //"Error executing method."
        }
    }

}
