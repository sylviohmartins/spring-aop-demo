package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    public static Object getFieldValue(final Object result, final String attribute) throws NoSuchFieldException, IllegalAccessException {
        Field field = result.getClass().getDeclaredField(attribute);
        field.setAccessible(true);

        return field.get(result);
    }

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
