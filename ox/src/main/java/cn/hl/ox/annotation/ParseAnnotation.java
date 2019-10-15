package cn.hl.ox.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;

import cn.hl.ox.BuddhaBless;
import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation.EnumType;
import cn.hl.ox.annotation.MyAnnotation.MyClassAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyMethodAnnotation;

/**
 * @author hyman
 * @date 2019-10-15 13:59:53
 * @version $ Id: ParseAnnotation.java, v 0.1  hyman Exp $
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
                MyMethodAnnotation methodAnnotation = method.getAnnotation(MyMethodAnnotation.class);
                if (methodAnnotation != null) {
                    // 通过反射调用带有此注解的方法
                    method.invoke(obj, methodAnnotation.uri());
                }
                MyClassAndMethodAnnotation myClassAndMethodAnnotation = method.getAnnotation(MyClassAndMethodAnnotation.class);
                if (myClassAndMethodAnnotation != null) {
                    if (EnumType.UTIL.equals(myClassAndMethodAnnotation.classType())) {
                        System.out.println("this is a util method");
                    } else {
                        System.out.println("this is a other method");
                    }
                    System.out.println(Arrays.toString(myClassAndMethodAnnotation.arr()));// 打印数组
                    System.out.println(myClassAndMethodAnnotation.color());// 输出颜色
                }
                System.out.println("-----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void parseType(Class<T> clazz) {
        try {
            MyClassAndMethodAnnotation myClassAndMethodAnnotation = clazz.getAnnotation(MyClassAndMethodAnnotation.class);
            if (myClassAndMethodAnnotation != null) {
                if (EnumType.UTIL.equals(myClassAndMethodAnnotation.classType())) {
                    System.out.println("this is a util class");
                } else {
                    System.out.println("this is a other class");
                }
            }
            MyClassAnnotation myClassAnnotation = clazz.getAnnotation(MyClassAnnotation.class);
            if (myClassAnnotation != null) {
                System.err.println("class info: " + myClassAnnotation.uri());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        parseMethod(TestAnnotation.class);
        BuddhaBless.printSplitLine();
        parseType(TestAnnotation.class);
    }
}
