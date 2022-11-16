package cn.hl.ox.annotation;

import cn.hl.ax.log.LogUtils;
import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation.EnumType;
import cn.hl.ox.annotation.MyAnnotation.MyClassAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyMethodAnnotation;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author hyman
 * @date 2019-10-15 13:59:53
 */
public class ParseAnnotation {
    /**
     * 解析方法注解
     *
     * @param clazz
     */
    public static <T> void parseMethod(Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                LogUtils.printSplitWave(106);
                System.out.println(method);
                MyMethodAnnotation methodAnnotation = method.getAnnotation(MyMethodAnnotation.class);
                if (methodAnnotation != null) {
                    // 通过反射调用带有此注解的方法
                    method.invoke(obj, methodAnnotation.uri());
                }
                MyClassAndMethodAnnotation myClassAndMethodAnnotation = method.getAnnotation(MyClassAndMethodAnnotation.class);
                if (myClassAndMethodAnnotation != null) {
                    System.out.println("MyClassAndMethodAnnotation.classType -> " + myClassAndMethodAnnotation.classType());
                    System.out.println("MyClassAndMethodAnnotation.color -> " + myClassAndMethodAnnotation.color());
                    System.out.println("MyClassAndMethodAnnotation.arr -> " + Arrays.toString(myClassAndMethodAnnotation.arr()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void parseType(Class<T> clazz) {
        try {
            System.out.println();
            LogUtils.printSplitLine(106);
            MyClassAndMethodAnnotation myClassAndMethodAnnotation = clazz.getAnnotation(MyClassAndMethodAnnotation.class);
            if (myClassAndMethodAnnotation != null) {
                if (EnumType.UTIL.equals(myClassAndMethodAnnotation.classType())) {
                    System.out.println("MyClassAndMethodAnnotation.classType -> UTIL");
                } else {
                    System.out.println("MyClassAndMethodAnnotation.classType -> OTHER");
                }
            }
            MyClassAnnotation myClassAnnotation = clazz.getAnnotation(MyClassAnnotation.class);
            if (myClassAnnotation != null) {
                System.out.println("MyClassAnnotation.uri -> " + myClassAnnotation.uri());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        parseMethod(TestAnnotation.class);
        parseType(TestAnnotation.class);
    }
}
