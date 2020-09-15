package cn.hl.kit.ox._feature.v8;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hyman
 * @date 2019-07-16 12:13:36
 * @version $ Id: Tester4RepeatingAnnotations.java, v 0.1  hyman Exp $
 */
public class Tester4RepeatingAnnotations {
    /*
     自从Java 5中引入注解以来，这个特性开始变得非常流行，并在各个框架和项目中被广泛使用。
     不过，注解有一个很大的限制是：在同一个地方不能多次使用同一个注解。
     Java 8打破了这个限制，引入了重复注解的概念，允许在同一个地方多次使用同一个注解。
     */

    // 在Java 8中使用@Repeatable注解定义重复注解，实际上，这并不是语言层面的改进，而是编译器做的一个trick，底层的技术仍然相同。可以利用下面的代码说明：
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filters {
        Filter[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Filters.class)
    public @interface Filter {
        String value();
    }

    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    public static void main(String[] args) {
        for (Filter filter : Filterable.class.getAnnotationsByType(Filter.class)) {
            System.out.println(filter.value());
        }
    }

    /*
    正如我们所见，这里的Filter类使用@Repeatable(Filters.class)注解修饰，而Filters是存放Filter注解的容器，编译器尽量对开发者屏蔽这些细节。
    这样，Filterable接口可以用两个Filter注解注释（这里并没有提到任何关于Filters的信息）。
    另外，反射API提供了一个新的方法：getAnnotationsByType()，可以返回某个类型的重复注解，
    例如Filterable.class.getAnnoation(Filters.class)将返回两个Filter实例，输出到控制台的内容如下所示：
        filter1
        filter2
     */
}
