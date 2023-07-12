package br.com.sylviomartins.spring.aop.demo.util;

public class MyTest {

    public static void main(String[] args) {
        Class<?> targetClass = Void.class;

        System.out.println(isExecuteMethod(targetClass));

    }

    public static boolean isExecuteMethod(Class<?> targetClass) {
        return Void.class.getName().equals(targetClass.getName());
    }
}
