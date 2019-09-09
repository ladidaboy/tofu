package cn.hl.ox._feature.v8;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hyman
 * @date 2019-07-16 17:58:31
 * @version $ Id: Tester4Streams.java, v 0.1  hyman Exp $
 */
public class Tester4Streams {
    /*
     新增的Stream API（java.util.stream）将生成环境的函数式编程引入了Java库中。
     这是目前为止最大的一次对Java库的完善，以便开发者能够写出更加有效、更加简洁和紧凑的代码。
     Steam API极大得简化了集合操作（后面我们会看到不止是集合），首先看下这个叫Task的类：
     */

    private enum Status {
        OPEN,
        CLOSED
    }

    private static final class Task {
        private final Status  status;
        private final Integer points;

        Task(final Status status, final Integer points) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("<%s, %d>", status, points);
        }
    }

    public static void main(String[] args) {
        // Task类有一个分数（或伪复杂度）的概念，另外还有两种状态：OPEN或者CLOSED。现在假设有一个task集合：
        final Collection<Task> tasks = Arrays.asList(
                new Task(Status.OPEN, 5),
                new Task(Status.OPEN, 13),
                new Task(Status.CLOSED, 8)
        );
        // 首先看一个问题：在这个task集合中一共有多少个OPEN状态的点？
        // 在Java 8之前，要解决这个问题，则需要使用foreach循环遍历task集合；
        // 但是在Java 8中可以利用steams解决：包括一系列元素的列表，并且支持顺序和并行处理。
        // Calculate total points of all active tasks using sum()
        final long totalPointsOfOpenTasks = tasks
                .stream()
                .filter(task -> task.getStatus() == Status.OPEN)
                .mapToInt(Task::getPoints)
                .sum();

        System.out.println("Total points: " + totalPointsOfOpenTasks);
        /*
         这里有很多知识点值得说。
         首先，tasks集合被转换成steam表示；
         其次，在steam上的filter操作会过滤掉所有CLOSED的task；
         第三，mapToInt操作基于每个task实例的Task::getPoints方法将task流转换成Integer集合；
         最后，通过sum方法计算总和，得出最后的结果。

         在学习下一个例子之前，还需要记住一些steams（点此更多细节）的知识点。Steam之上的操作可分为中间操作和晚期操作。
         ➤中间操作会返回一个新的steam——执行一个中间操作（例如filter）并不会执行实际的过滤操作，
         而是创建一个新的steam，并将原steam中符合条件的元素放入新创建的steam。
         ➤晚期操作（例如forEach或者sum），会遍历steam并得出结果或者附带结果；
         在执行晚期操作之后，steam处理线已经处理完毕，就不能使用了。
         在几乎所有情况下，晚期操作都是立刻对steam进行遍历。
         */

        // Steam的另一个价值是创造性地支持并行处理（parallel processing）。对于上述的tasks集合，我们可以用下面的代码计算所有任务的点数之和：
        // Calculate total points of all tasks
        final double totalPoints = tasks
                .stream()
                .parallel()
                .map( task -> task.getPoints() ) // or map( Task::getPoints )
                .reduce( 0, Integer::sum );

        System.out.println("Total points (all tasks): " + totalPoints);
        /*这里我们使用parallel方法并行处理所有的task，并使用reduce方法计算最终的结果。*/

        // 对于一个集合，经常需要根据某些条件对其中的元素分组。利用steam提供的API可以很快完成这类任务，代码如下：
        // Group tasks by their status
        final Map<Status, List<Task>> map = tasks
                .stream()
                .collect(Collectors.groupingBy(Task::getStatus));
        System.out.println(map);

        // 最后一个关于tasks集合的例子问题是：如何计算集合中每个任务的点数在集合中所占的比重，具体处理的代码如下：
        // Calculate the weight of each tasks (as percent of total points)
        final Collection< String > result = tasks
                .stream()                                        // Stream< String >
                .mapToInt( Task::getPoints )                     // IntStream
                .asLongStream()                                  // LongStream
                .mapToDouble( points -> points / totalPoints )   // DoubleStream
                .boxed()                                         // Stream< Double >
                .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
                .mapToObj( percentage -> percentage + "%" )      // Stream< String>
                .collect( Collectors.toList() );                 // List< String >

        System.out.println( result );

        // 最后，正如之前所说，Steam API不仅可以作用于Java集合，传统的IO操作（从文件或者网络一行一行得读取数据）可以受益于steam处理，这里有一个小例子：
        final Path path = Paths.get(Tester4Streams.class.getResource("readme.txt").getPath());
        try( Stream< String > lines = Files.lines( path, StandardCharsets.UTF_8 ) ) {
            lines.onClose( () -> System.out.println("Done!") ).forEach( System.out::println );
        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        /*
         Stream的方法 onClose 返回一个等价的有额外句柄的Stream，当Stream的 close() 方法被调用的时候这个句柄会被执行。
         Stream API、Lambda表达式还有接口默认方法和静态方法支持的方法引用，是Java 8对软件开发的现代范式的响应。
         */
    }
}
