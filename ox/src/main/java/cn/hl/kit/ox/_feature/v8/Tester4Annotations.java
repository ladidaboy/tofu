package cn.hl.kit.ox._feature.v8;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author hyman
 * @date 2019-07-16 14:10:09
 * @version $ Id: Tester4Annotations.java, v 0.1  hyman Exp $
 */
public class Tester4Annotations {
    /*
    Java 8拓宽了注解的应用场景。
    现在，注解几乎可以使用在任何元素上：局部变量、接口类型、超类和接口实现类，甚至可以用在函数的异常定义上。
    下面是一些例子：
     */

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    public @interface NonEmpty {
    }

    public static class Holder<@NonEmpty T> extends @NonEmpty Object {
        public void method() throws @NonEmpty Exception {
        }
    }

    public static void main(String[] args) {
        final Holder<String> holder = new @NonEmpty Holder<>();
        @NonEmpty Collection<@NonEmpty String> strings = new ArrayList<>();
    }

    /*
    ElementType.TYPE_USE 和 ElementType.TYPE_PARAMETER 是Java 8新增的两个注解，用于描述注解的使用场景。
    Java 语言也做了对应的改变，以识别这些新增的注解。
     */
}
