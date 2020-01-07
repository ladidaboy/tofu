package cn.hl.ox._feature.v8;

import cn.hl.ax.data.AryTransfer;
import cn.hl.ox.BuddhaBless;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hyman
 * @date 2020-01-07 12:48:58
 * @version $ Id: Tester4Streams3.java, v 0.1  hyman Exp $
 */
public class Tester4Streams3 {
    private static final Random rd = new Random();

    public static class Book {
        private String name;
        private int    price;

        Book(String name, int price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Book: " + price + "¥ - " + name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Book book = (Book) o;
            return price == book.price && name.equals(book.name);
        }

        @Override
        public int hashCode() {
            int _hash_ = 7;
            _hash_ = 13 * _hash_ + (name == null ? 0 : name.hashCode());
            return _hash_;
        }
    }

    private static final String[] BOOK_NAMES = new String[] {"Core Java", "Freemarker", "MySQL", "Thymeleaf", "Spring Boot"};

    //=============================================================================================

    private static void distinctSimpleDemo(List<String> list) {
        BuddhaBless.printHeadline("distinctSimpleDemo");

        long count = list.stream().distinct().count();
        System.out.println("No. of distinct elements:" + count);
        list = list.stream().distinct().collect(Collectors.toList());
        //String output = list.stream().distinct().collect(Collectors.joining(", "));
        System.out.println(list);
    }

    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    private static void distinctWithUserObjects(List<Book> list) {
        BuddhaBless.printHeadline("distinctWithUserObjects");

        long count = list.stream().distinct().count();
        System.out.println("No. of distinct books:" + count);
        list.stream().distinct().forEach(System.out::println);
    }

    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static void distinctByProperty(List<Book> list) {
        BuddhaBless.printHeadline("distinctByProperty");

        long count = list.stream().filter(distinctByKey(b -> b.name)).count();
        System.out.println("No. of distinct books:" + count);
        list.stream().filter(distinctByKey(b -> b.name)).forEach(System.out::println);

    }

    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    class BookSupplier implements Supplier<Book> {
        @Override
        public Book get() {
            return new Book(BOOK_NAMES[rd.nextInt(BOOK_NAMES.length)], 100 + rd.nextInt(5) * 50);
        }
    }

    private static void supplierTest() {
        BuddhaBless.printHeadline("supplierTest");

        Tester4Streams3 demo = new Tester4Streams3();
        Stream.generate(demo.new BookSupplier()).limit(10).forEach(System.out::println);
    }

    private static void sequenceTest() {
        BuddhaBless.printHeadline("sequenceTest");

        Stream.iterate(0, n -> n + 3).limit(10).forEach(x -> System.out.print(" > " + x));
        System.out.println();
        Stream.iterate(4, n -> n + 3).limit(10).forEach(x -> System.out.print(" > " + x));
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /*
    distinct（）返回由该流的不同元素组成的流。distinct（）是Stream接口的方法。
    distinct（）使用hashCode（）和equals（）方法来获取不同的元素。
    因此，我们的类必须实现hashCode（）和equals（）方法。
    如果distinct（）正在处理有序流，那么对于重复元素，将保留以遭遇顺序首先出现的元素，并且以这种方式选择不同元素是稳定的。
    在无序流的情况下，不同元素的选择不一定是稳定的，是可以改变的。distinct（）执行有状态的中间操作。
    在有序流的并行流的情况下，保持distinct（）的稳定性是需要很高的代价的，因为它需要大量的缓冲开销。
    如果我们不需要保持遭遇顺序的一致性，那么我们应该可以使用通过BaseStream.unordered（）方法实现的无序流。
     */

    public static void main(String[] args) {
        // 测试普通对象
        BuddhaBless.printWave(100);
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(AryTransfer.to26Ary(rd.nextInt(26)));
        }
        System.out.println(list1);
        distinctSimpleDemo(list1);

        // 测试封装对象
        BuddhaBless.printWave(100);
        List<Book> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list2.add(new Book(BOOK_NAMES[rd.nextInt(BOOK_NAMES.length)], 100 + rd.nextInt(5) * 50));
        }
        list2.forEach(System.out::println);
        distinctWithUserObjects(list2);
        distinctByProperty(list2);

        // 测试供应者
        BuddhaBless.printWave(100);
        supplierTest();
        sequenceTest();
    }
}
