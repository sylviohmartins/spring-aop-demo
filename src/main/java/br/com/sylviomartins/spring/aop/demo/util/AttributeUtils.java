package br.com.sylviomartins.spring.aop.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeUtils {

    /*public static br.com.sylviomartins.spring.aop.demo.domain.document.nested.Attribute createAttribute(final Attribute attribute) {
        return br.com.sylviomartins.spring.aop.demo.domain.document.nested.Attribute.builder() //
                .name(attribute.fieldName()) //
                .objectType(attribute.objectType())//
                .method(createMethod(attribute.method())) //
                .build();
    }

    public static br.com.sylviomartins.spring.aop.demo.domain.document.nested.Method createMethod(final Method method) {
        return br.com.sylviomartins.spring.aop.demo.domain.document.nested.Method.builder() //
                .targetClass(method.targetClass()) //
                .name(method.methodName()) //
                .build();
    }*/

    public static boolean isExecuteMethod(Class<?> targetClass) {
        return Void.class.getName().equals(targetClass.getName());
    }

}
