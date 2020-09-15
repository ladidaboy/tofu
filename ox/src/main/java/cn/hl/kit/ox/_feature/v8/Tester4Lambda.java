package cn.hl.kit.ox._feature.v8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hyman
 * @date 2019-07-16 11:31:51
 * @version $ Id: Tester4Lambda.java, v 0.1  hyman Exp $
 */
public class Tester4Lambda {
    public static void main(String args[]) {

        List<String> names1 = new ArrayList<>();
        names1.add("Sina");
        names1.add("Baidu");
        names1.add("Google");
        names1.add("Runoob");
        names1.add("Alibaba");

        List<String> names2 = new ArrayList<>();
        names2.add("Alibaba");
        names2.add("Google");
        names2.add("Runoob");
        names2.add("Baidu");
        names2.add("Sina");

        Tester4Lambda tester = new Tester4Lambda();

        System.out.println("使用 Java 7 语法: ");
        tester.sortUsingJava7(names1);
        System.out.println(names1);

        System.out.println("使用 Java 8 语法: ");
        tester.sortUsingJava8(names2);
        System.out.println(names2);

        // 最简单的Lambda表达式可由逗号分隔的参数列表、->符号和语句块组成，例如：
        Arrays.asList("L", "a", "m", "b", "d", "a").forEach(e -> System.out.println(e));
        // 在上面这个代码中的参数e的类型是由编译器推理得出的，你也可以显式指定该参数的类型，例如：
        Arrays.asList("L", "a", "m", "b", "d", "a").forEach((String e) -> System.out.println(e));
        // 如果Lambda表达式需要更复杂的语句块，则可以使用花括号将该语句块括起来，类似于Java中的函数体，例如：
        Arrays.asList("L", "a", "m", "b", "d", "a").forEach(e -> {
            System.out.print(e);
            System.err.println(e);
        });
        // Lambda表达式可以引用类成员和局部变量（会将这些变量隐式得转换成final的），例如下列两个代码块的效果完全相同：
        /* CODE 1 */
        String separator1 = ",";
        Arrays.asList("L", "a", "m", "b", "d", "a").forEach((String e) -> System.out.print(e + separator1));
        /* CODE 2 */
        final String separator2 = ",";
        Arrays.asList("L", "a", "m", "b", "d", "a").forEach((String e) -> System.out.print(e + separator2));
        // Lambda表达式有返回值，返回值的类型也由编译器推理得出。如果Lambda表达式中的语句块只有一行，则可以不用使用return语句，下列两个代码片段效果相同：
        /* CODE 1 */
        Arrays.asList("L", "a", "m", "b", "d", "a").sort((e1, e2) -> e1.compareTo(e2));
        /* CODE 2 */
        Arrays.asList("L", "a", "m", "b", "d", "a").sort((e1, e2) -> {
            int result = e1.compareTo(e2);
            return result;
        });
    }

    /**
     * 使用 java 7 排序
     * @param names
     */
    private void sortUsingJava7(List<String> names) {
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
    }

    /**
     * 使用 java 8 排序
     * @param names
     */
    private void sortUsingJava8(List<String> names) {
        Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
    }
}
