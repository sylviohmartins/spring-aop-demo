package br.com.sylviomartins.spring.aop.demo.util;

import br.com.sylviomartins.spring.aop.demo.domain.document.nested.Attribute;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    public static Object getFieldValue(final Object result, final Attribute attribute) throws NoSuchFieldException, IllegalAccessException {
        Field field = result.getClass().getDeclaredField(attribute.getName());
        field.setAccessible(true);

        return field.get(result);
    }

    public static Object executeMethod(final Class<?> targetClass, final String methodName, final Object targetObject, final Object attributeValue) {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, attributeValue.getClass());

            return method.invoke(targetObject, attributeValue);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
