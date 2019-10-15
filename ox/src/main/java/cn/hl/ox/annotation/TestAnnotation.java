package cn.hl.ox.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyClassAndMethodAnnotation.EnumType;
import cn.hl.ox.annotation.MyAnnotation.MyClassAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyConstructorAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyFieldAnnotation;
import cn.hl.ox.annotation.MyAnnotation.MyMethodAnnotation;

/**
 * @author hyman
 * @date 2019-10-15 13:57:00
 * @version $ Id: TestAnnotation.java, v 0.1  hyman Exp $
 */
@MyClassAnnotation(desc = "The Class", uri = "cn.hl.ox.annotation")
@MyClassAndMethodAnnotation(classType = EnumType.UTIL)
public class TestAnnotation {
    @MyFieldAnnotation(desc = "The Class Field", uri = "cn.hl.ox.annotation#id")
    private String id;

    @MyConstructorAnnotation(desc = "The Class Constructor", uri = "cn.hl.ox.annotation#constructor")
    public TestAnnotation() {
    }

    @MyMethodAnnotation(desc = "The Class Method", uri = "cn.hl.ox.annotation#setId")
    public void setId(String id) {
        this.id = id;
    }

    @MyMethodAnnotation(desc = "The Class Method sayHello", uri = "cn.hl.ox.annotation#sayHello")
    public void sayHello(String name) {
        if (name == null || name.equals("")) {
            System.out.println("hello world!");
        } else {
            System.out.println(name + " : Hi!");
        }
    }

    public static void main(String[] args) throws Exception {
        Class<TestAnnotation> clazz = TestAnnotation.class;
        // 获取类注解
        MyClassAnnotation myClassAnnotation = clazz.getAnnotation(MyClassAnnotation.class);
        System.out.println(myClassAnnotation.desc() + "+" + myClassAnnotation.uri());

        // 获得构造方法注解
        Constructor<TestAnnotation> constructors = clazz.getConstructor();// 先获得构造方法对象
        MyConstructorAnnotation myConstructorAnnotation = constructors.getAnnotation(MyConstructorAnnotation.class);// 拿到构造方法上面的注解实例
        System.out.println(myConstructorAnnotation.desc() + "+" + myConstructorAnnotation.uri());

        // 获得方法注解
        Method method = clazz.getMethod("setId", String.class);// 获得方法对象
        MyMethodAnnotation myMethodAnnotation = method.getAnnotation(MyMethodAnnotation.class);
        System.out.println(myMethodAnnotation.desc() + "+" + myMethodAnnotation.uri());

        // 获得字段注解
        Field field = clazz.getDeclaredField("id");// 暴力获取private修饰的成员变量
        MyFieldAnnotation myFieldAnnotation = field.getAnnotation(MyFieldAnnotation.class);
        System.out.println(myFieldAnnotation.desc() + "+" + myFieldAnnotation.uri());
    }
}
