package cn.hl.ox._feature.v8;

import java.util.function.Supplier;

/**
 * @author hyman
 * @date 2019-07-16 11:52:27
 * @version $ Id: Tester4DefaultMethod.java, v 0.1  hyman Exp $
 */
public class Tester4DefaultMethod {
    /*
     Java 8使用两个新概念扩展了接口的含义：默认方法和静态方法。
     默认方法使得接口有点类似traits，不过要实现的目标不一样。
     默认方法使得开发者可以在 不破坏二进制兼容性的前提下，往现存接口中添加新的方法，即不强制那些实现了该接口的类也同时实现这个新加的方法。
     */

    // Defaulable接口使用关键字default定义了一个默认方法notRequired()。
    // 默认方法和抽象方法之间的区别在于抽象方法需要实现，而默认方法不需要。接口提供的默认方法会被接口的实现类继承或者覆写，例子代码如下：
    private interface Defaulable {
        // Interfaces now allow default methods, the implementer may or may not implement (override) them.
        default String notRequired() {
            return "Default implementation";
        }
    }

    // DefaultableImpl类实现了这个接口，同时默认继承了这个接口中的默认方法；
    private static class DefaultableImpl implements Defaulable {
    }

    // OverridableImpl类也实现了这个接口，但覆写了该接口的默认方法，并提供了一个不同的实现；
    private static class OverridableImpl implements Defaulable {
        @Override
        public String notRequired() {
            return "Overridden implementation";
        }
    }

    // Java 8带来的另一个有趣的特性是在接口中可以定义静态方法，例子代码如下：
    private interface DefaulableFactory {
        // Interfaces now allow static methods
        static Defaulable create(Supplier<Defaulable> supplier) {
            return supplier.get();
        }
    }

    public static void main(String[] args) {
        Defaulable defaulable = DefaulableFactory.create(DefaultableImpl::new);
        System.out.println(defaulable.notRequired());

        defaulable = DefaulableFactory.create(OverridableImpl::new);
        System.out.println(defaulable.notRequired());
    }

    /*
     * 由于JVM上的默认方法的实现在字节码层面提供了支持，因此效率非常高。默认方法允许在不打破现有继承体系的基础上改进接口。
     * 该特性在官方库中的应用是：给java.util.Collection接口添加新方法，如stream()、parallelStream()、forEach()和removeIf()等等。
     * 尽管默认方法有这么多好处，但在实际开发中应该谨慎使用：在复杂的继承体系中，默认方法可能引起歧义和编译错误。
     */
}