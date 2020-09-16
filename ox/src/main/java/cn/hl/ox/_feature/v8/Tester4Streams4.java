package cn.hl.ox._feature.v8;

import cn.hl.ax.enums.EnumUtils;
import cn.hl.ax.log.LogUtils;
import cn.hl.ox._feature.v8.Tester4Streams4.Transaction.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author hyman
 * @date 2020-01-07 15:15:42
 * @version $ Id: Tester4Streams4.java, v 0.1  hyman Exp $
 */
public class Tester4Streams4 {
    public static void main(String[] args) {
        test1sorted();
        LogUtils.printCornerTitle("01");
        test2construct();
        LogUtils.printCornerTitle("02");
        test3intStream();
        LogUtils.printCornerTitle("03");
        test4change();
        LogUtils.printCornerTitle("04");
        test5map();
        LogUtils.printCornerTitle("05");
        test6square();
        LogUtils.printCornerTitle("06");
        test7flatMap();
        LogUtils.printCornerTitle("07");
        test8filter();
        LogUtils.printCornerTitle("08");
        test9pickAllWords();
        LogUtils.printCornerTitle("09");
        test10foreach();
        LogUtils.printCornerTitle("10");
        test11peek();
        LogUtils.printCornerTitle("11");
        test12reduce();
        LogUtils.printCornerTitle("12");
        test13limit();
        LogUtils.printCornerTitle("13");
        test14MinMax();
        LogUtils.printCornerTitle("14");
        test15operateWords();
        LogUtils.printCornerTitle("15");
        test16match();
        LogUtils.printCornerTitle("16");
        test17groupingBy();
        LogUtils.printCornerTitle("17");
        test18partitioningBy();
        LogUtils.printCornerTitle("18");
    }

    //=============================================================================================

    static class Transaction {
        private final int     id;
        private final Integer value;
        private final Type    type;

        Transaction(int id, Integer value, Type type) {
            this.id = id;
            this.value = value;
            this.type = type;
        }

        enum Type {
            A,
            B,
            C,
            D,
            GROCERY
        }

        @Override
        public String toString() {
            return "Transaction{" + "id=" + id + ", value=" + value + ", type=" + type + '}';
        }

        public int getId() {
            return id;
        }

        public Integer getValue() {
            return value;
        }

        public Type getType() {
            return type;
        }
    }

    class MySupplier implements Supplier<Transaction> {
        @Override
        public Transaction get() {
            return new Transaction(rd.nextInt(10), rd.nextInt(900) + 100, EnumUtils.fromOrdinal(Type.class, rd.nextInt(5)));
        }
    }

    private static final Random rd = new Random();

    private static final List<Transaction> transactions = new ArrayList<>();

    static {
        LogUtils.printHeadline("Initial transactions");
        for (int i = 0; i < 10; i++) {
            transactions.add(new Transaction(rd.nextInt(10), rd.nextInt(900) + 100, EnumUtils.fromOrdinal(Type.class, rd.nextInt(5))));
        }
        transactions.forEach(System.out::println);
        LogUtils.printCornerTitle("Initial DONE");
    }

    private static String getJavaFilePath(Class clz) {
        URL url = clz.getResource(".");
        String path = url.getPath().replace("target/classes/", "src/main/java/");
        return path + clz.getSimpleName() + ".java";
    }

    //=============================================================================================

    private static void test1sorted() {
        List<Integer> transactionsIds = transactions.parallelStream().filter(t -> t.type == Type.GROCERY).sorted(
                Comparator.comparing(Transaction::getValue).reversed()).map(Transaction::getId).collect(Collectors.toList());
        System.out.println(transactionsIds);
    }

    private static void test2construct() {
        // 1. Individual values 单独值
        Stream stream = Stream.of("a1", "b1", "c1");
        stream.forEach(System.out::print);
        System.out.println();

        // 2. Arrays 数组
        String[] strArray = new String[] {"a2", "b2", "c2"};
        stream = Stream.of(strArray);
        stream = Arrays.stream(strArray);
        System.out.println(stream.collect(Collectors.joining(",")).toString());

        // 3. Collections 集合
        List<String> list = Arrays.asList(strArray);
        stream = list.stream();
    }

    private static void test3intStream() {
        // IntStream, LongStream, DoubleStream. 当然我们也可以用Stream<Integer>, Stream<Long>, Stream<Double>,
        // 但是boxing 和 unboxing会很耗时, 所以特别为这三个基本数值型提供了对应的Stream
        IntStream.of(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}).forEach(System.out::print);
        System.out.println();
        IntStream.range(1, 11).forEach(System.out::print);
        System.out.println();
        IntStream.rangeClosed(1, 11).forEach(System.out::print);
        System.out.println();
    }

    private static void test4change() {
        Stream stream = Stream.of("a1", "b1", "c1");
        // 1. Array
        String[] strArray1 = (String[]) stream.toArray(String[]::new);
        for (String s : strArray1) {
            System.out.print(s);
        }
        System.out.println();
        // 2.Collection list
        stream = Stream.of("a1", "b1", "c1");// stream has already been operated upon or closed
        List<String> list1 = (List<String>) stream.collect(Collectors.toList());
        for (String s : list1) {
            System.out.print(s);
        }
        System.out.println();
        // 2.Collection list
        stream = Stream.of("a1", "b1", "c1");// stream has already been operated upon or closed
        List<String> list2 = (List<String>) stream.collect(Collectors.toCollection(ArrayList::new));
        for (String s : list2) {
            System.out.print(s);
        }
        System.out.println();
        // 2.Collection set
        stream = Stream.of("a1", "b1", "c1");// stream has already been operated upon or closed
        Set<String> set = (Set<String>) stream.collect(Collectors.toSet());
        for (String s : set) {
            System.out.print(s);
        }
        System.out.println();
        // 2.Collection stack
        stream = Stream.of("a1", "b1", "c1");// stream has already been operated upon or closed
        Stack<String> stack = (Stack<String>) stream.collect(Collectors.toCollection(Stack::new));
        for (String s : stack) {
            System.out.print(s);
        }
        System.out.println();
        // 3. String
        stream = Stream.of("a1", "b1", "c1");// stream has already been operated upon or closed
        String str = stream.collect(Collectors.joining()).toString();
        System.out.print(str);
        System.out.println();

        /*
        流的操作
        Intermediate
            map(mapToInt,flatMap等), filter, distinct, sorted, peek, limit, skip, parallel, sequential, unordered
        Terminal
            forEach, forEachOrdered, toArray, reduce, collect, min, max, count, anyMatch, allMatch, noneMatch, findFirst, findAny, iterator
        Short-cricuiting
            anyMatch, allMatch, noneMatch, findFirst, findAny, limit
         */
    }

    private static void test5map() {
        Stream<String> stream = Stream.of("hello", "world", "java8", "stream");
        List<String> wordList = stream.map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(wordList.toString());

        stream = Stream.of("hello", "world", "java8", "stream");
        wordList = stream.map(s -> {
            return s.toUpperCase();
        }).collect(Collectors.toList());
        System.out.println(wordList.toString());

        stream = Stream.of("hello", "world", "java8", "stream");
        wordList = stream.map(s -> s.toUpperCase()).collect(Collectors.toList());
        System.out.println(wordList.toString());
    }

    private static void test6square() {
        // map 生产的是个1:1的映射，每个输入元素，都按照规则转换成另一个元素
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4).stream();
        List<Integer> squareList = stream.map(n -> n * n).collect(Collectors.toList());
        System.out.println(squareList.toString());
    }

    private static void test7flatMap() {
        // flatMap把input stream中的层级结构扁平化，就是将底层元素抽出来放到一起，最终output的Stream里面已经没有List了，都是直接的数字
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        Stream<Integer> outputStream = inputStream.flatMap(childList -> childList.stream());
        System.out.println(outputStream.collect(Collectors.toList()).toString());
    }

    private static void test8filter() {
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Integer[] evens = Stream.of(sixNums).filter(n -> n % 2 == 0).toArray(Integer[]::new);
        System.out.println(Arrays.toString(evens));
    }

    private static void test9pickAllWords() {
        Path path = Paths.get(getJavaFilePath(Tester4T.class));

        // 1. Java 8 Read File + Stream
        try (Stream<String> stream = Files.lines(path)) {
            List<String> output = stream.flatMap(line -> Stream.of(line.split(" "))).filter(word -> word.length() > 0).collect(
                    Collectors.toList());
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. BufferedReader + Stream
        try (BufferedReader br = Files.newBufferedReader(path)) {
            List<String> output = br.lines().flatMap(line -> Stream.of(line.split(" "))).filter(word -> word.length() > 0).collect(
                    Collectors.toList());
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test10foreach() {
        //(forEach 和pre-java8的对比) 【forEach 不能修改自己包含的本地变量值，也不能用break/return 之类的关键字提前结束循环】
        transactions.stream().filter(p -> p.type == Type.C).forEach(p -> System.out.println(p.value));
    }

    private static void test11peek() {
        // 对每个元素执行操作并且返回一个新的Stream 【peek : 偷窥】注意执行顺序
        Stream.of("one", "two", "three", "four", "five", "six", "seven")//
                .filter(p -> p.length() > 3)//
                .peek(v -> System.out.println("Filtered Value: " + v))//
                .map(String::toUpperCase)//
                .peek(v -> System.out.println("Mapped Value: " + v))//
                .collect(Collectors.toList());
    }

    private static void test12reduce() {
        // 1. 求和 SUM 10
        Integer sum = Stream.of(1, 2, 3, 4).reduce(0, (a, b) -> a + b);
        sum = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum); //有起始值
        sum = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get(); //无起始值
        System.out.println(sum);
        // 2. 最小值 minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double::min).get();
        System.out.println(minValue);
        // 2. 最大数值 maxValue = 1.0
        double maxValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MIN_VALUE, Double::max);
        maxValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double::max).get();
        System.out.println(maxValue);
        // 3. 字符串连接 Concat "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        // 4. 过滤和字符串连接 Filter & Concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
        System.out.println(concat);
    }

    private static void test13limit() {
        // limit/skip (limit 返回Stream的前面n个元素; skip 则是扔掉前n个元素; 它是由一个叫subStream的方法改名而来.)
        List<Integer> values = transactions.stream()//
                .map(Transaction::getValue) //
                .limit(7)//
                .skip(3)//
                .collect(Collectors.toList());
        System.out.println(values);

        // limit和skip对sorted后的运行次数无影响
        values = transactions.stream()//
                .sorted((p1, p2) -> p1.type.compareTo(p2.type))//
                .map(Transaction::getValue)//
                .limit(2)//
                .collect(Collectors.toList());
        System.out.println(values);

        // 排序前进行limit和skip (这种优化是有business logic上的局限性的: 既不需要排序后再取值)
        values = transactions.stream()//
                .limit(2)//
                .sorted((p1, p2) -> p1.type.compareTo(p2.type))//
                .map(Transaction::getValue)//
                .collect(Collectors.toList());
        System.out.println(values);
    }

    private static void test14MinMax() {
        // min/max/distinct 【min和max的功能也可以通过对Stream元素先排序，再findFirst来实现，但前者的性能会更好，为O(n)，而sorted的成本是O(n log n)】
        Path path = Paths.get(getJavaFilePath(Tester4T.class));

        try (BufferedReader br = Files.newBufferedReader(path)) {
            int output = br.lines().mapToInt(String::length).max().getAsInt();
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test15operateWords() {
        //找出全文的单词，转小写，并且排序
        Path path = Paths.get(getJavaFilePath(Tester4T.class));

        try (BufferedReader br = Files.newBufferedReader(path)) {
            List<String> output = br.lines()//
                    .flatMap(line -> Stream.of(line.split(" ")))//
                    .map(String::toLowerCase)//
                    .distinct()//
                    .sorted()//
                    .collect(Collectors.toList());//
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test16match() {
        //1. allMatch: Stream 中全部元素符合传入的predicate，返回true
        //2. anyMatch: Stream 中只要有一个元素符合传入的predicate，返回true
        //3. noneMatch: Stream 中没有一个元素符合传入的predicate，返回true
        boolean isAllMatch = transactions.stream().allMatch(p -> p.value < 200);
        System.out.println("Value < 200, All match? " + isAllMatch);
        boolean isAnyMatch = transactions.stream().anyMatch(p -> p.value < 200);
        System.out.println("Value < 200, Any match? " + isAnyMatch);
        boolean isNoneMatch = transactions.stream().noneMatch(p -> p.value < 200);
        System.out.println("Value < 200, None match? " + isNoneMatch);
    }

    private static void test17groupingBy() {
        Tester4Streams4 demo = new Tester4Streams4();
        Map<Integer, List<Transaction>> transactionMap = Stream.generate(demo.new MySupplier()).limit(100)//
                .collect(Collectors.groupingByConcurrent(Transaction::getId));

        Iterator it = transactionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, List<Transaction>> tr = (Map.Entry) it.next();
            System.out.println("ID: " + tr.getKey() + " = " + tr.getValue().size());
        }
    }

    private static void test18partitioningBy() {
        Tester4Streams4 demo = new Tester4Streams4();
        Map<Boolean, List<Transaction>> partition = Stream.generate(demo.new MySupplier()).limit(100)//
                .collect(Collectors.partitioningBy(p -> p.value < 500));

        System.out.println("Value ≧ 500 := " + partition.get(false).size());
        System.out.println("Value < 500 := " + partition.get(true).size());
    }

    /**
     总之，Stream 的特性可以归纳为：
     01. 不是数据结构
     02. 它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、IO channel）抓取数据。
     03. 它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 filter 操作会产生一个不包含被过滤元素的新 Stream，而不是从 source 删除那些元素。
     04. 所有 Stream 的操作必须以 lambda 表达式为参数
     05. 不支持索引访问
     06. 你可以请求第一个元素，但无法请求第二个，第三个，或最后一个。不过请参阅下一项。
     07. 很容易生成数组或者 List
     08. 惰性化
     09. 很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始。
     10. Intermediate 操作永远是惰性化的。
     11. 并行能力
     12. 当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。
     13. 可以是无限的
     14. 集合有固定大小，Stream 则不必。limit(n) 和 findFirst() 这类的 short-circuiting 操作可以对无限的 Stream 进行运算并很快完成。

     流的操作
     Intermediate
     map(mapToInt,flatMap等), filter, distinct, sorted, peek, limit, skip, parallel, sequential, unordered
     Terminal
     forEach, forEachOrdered, toArray, reduce, collect, min, max, count, anyMatch, allMatch, noneMatch, findFirst, findAny, iterator
     Short-cricuiting
     anyMatch, allMatch, noneMatch, findFirst, findAny, limit
     */
}
