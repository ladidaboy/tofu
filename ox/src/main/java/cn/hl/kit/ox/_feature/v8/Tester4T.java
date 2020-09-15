package cn.hl.kit.ox._feature.v8;

/**
 * @author hyman
 * @date 2019-07-16 14:06:47
 * @version $ Id: Tester4T.java, v 0.1  hyman Exp $
 */

/**
 * Java 8编译器在类型推断方面有很大的提升，在很多场景下编译器可以推导出某个参数的数据类型，从而使得代码更为简洁。例子代码如下：
 * @param <T>
 */
public class Tester4T<T> {
    public static <T> T defaultValue() {
        return null;
    }

    public T getOrDefault(T value, T defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    public static void main(String[] args) {
        final Tester4T<String> value = new Tester4T<>();
        // 参数Value.defaultValue()的类型由编译器推导得出，不需要显式指明。
        // 在Java 7中这段代码会有编译错误，除非使用Value.<String>defaultValue()。
        value.getOrDefault("22", Tester4T.defaultValue());
    }
}
