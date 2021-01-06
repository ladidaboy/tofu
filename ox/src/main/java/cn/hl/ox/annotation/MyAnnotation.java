package cn.hl.ox.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hyman
 * @date 2019-10-15 13:54:32
 * @version $ Id: MyAnnotation.java, v 0.1  hyman Exp $
 */
public class MyAnnotation {
    /**
     * 注解类
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyClassAnnotation {
        String uri();

        String desc();
    }

    /**
     * 构造方法注解
     */
    @Target(ElementType.CONSTRUCTOR)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyConstructorAnnotation {
        String uri();

        String desc();
    }

    /**
     * 方法注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyMethodAnnotation {
        String uri();

        String desc();
    }

    /**
     * 字段注解
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyFieldAnnotation {
        String uri();

        String desc();
    }

    /**
     * 可以同时应用到类和方法上
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyClassAndMethodAnnotation {
        // 定义枚举
        enum EnumType {
            UTIL,
            ENTITY,
            SERVICE,
            MODEL
        }

        // 设置默认值
        EnumType classType() default EnumType.UTIL;

        // 数组
        int[] arr() default {7, 1, 2};

        String color() default "DarkBlue";
    }
}
