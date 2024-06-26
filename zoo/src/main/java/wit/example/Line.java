package wit.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 下图中，每个方块代表一个像素，每个像素用其行号和列号表示。为简化处理，多段线的走向只能是水平、竖直、斜向45度。<br>
 * <img width="500" src="Line.png"/><br>
 * 上图中的多段线可以用下面的坐标串表示: (2,8)、(3,7)、(3,6)、(3,5)、(4,4)、(5,3)、(6,2)、(7,3)、(8,4)、(7,5).<br>
 * 但可以发现，这种表示不是最简的，其实只需要存储6个蓝色的关键点即可，它们是线段的起点、拐点、终点，而剩下4个点是几余的。<br>
 * 即可以简化为: (2,8)、(3,7)、(3,5)、(6.2)、(8,4)、(7,5).<br>
 * 现在，请根据输入的包含有几余数据的多段线坐标列表，输出其最简化的结果。<br>
 * <pre>
 * >> 输入描述 <<
 * 2 8 3 7 3 6 3 5 4 4 5 3 6 2 7 3 8 4 7 5
 * 1、所有数字以空格分隔，每两个数字一组，第一个数字是行号，第二个数字是列号;
 * 2、行号和列号范围为[0,64)，用例输入保证不会越界，考生不必检查;
 * 3、输入数据至少包含两个坐标点。
 * << 输出描述 >>
 * 2 8 3 7 3 5 6 2 8 4 7 5
 * 压缩后的最简化坐标列表，和输入数据的格式相同
 * 备注: 输出的坐标相对顺序不能变化</pre>
 * == <b>解题思路</b> ==
 * <ol>
 * <li>其实这道题考察的是基本的初中数学知识。</li>
 * <li>假设我们只有三个点，我们怎样判断这三个点是否有【起点、拐点、终点】呢？三个点a,b,c组成线段a-b和线段b-c，两个线段的斜率不一致，那么这三个点肯定就不存在拐点，也就可以消掉b这个点。</li>
 * <li>具体我们实现的时候可以不使用斜率，而可以使用向量乘积这个特性。<br><img width="500" src="Line1.png"/></li>
 * </ol>
 */
public class Line {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] input = Arrays.stream(in.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        List<Integer> result = new ArrayList<>();
        int len = input.length;

        result.add(input[0]);
        result.add(input[1]);
        for (int i = 2; i < len - 2; i += 2) {
            if (!checkStraightLine(input, i)) {
                result.add(input[i]);
                result.add(input[i + 1]);
            }
        }
        if (len >= 4) {
            result.add(input[len - 2]);
            result.add(input[len - 1]);
        }

        System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    public static boolean checkStraightLine(int[] points, int anchor) {
        int x1 = points[anchor + 0] - points[anchor - 2];
        int y1 = points[anchor + 1] - points[anchor - 1];
        int x2 = points[anchor + 2] - points[anchor + 0];
        int y2 = points[anchor + 3] - points[anchor + 1];
        return x1 * y2 == x2 * y1;
    }
}
/*
#### 示例 1 ####
>> 输入
2 8 3 7 3 6 3 5 4 4 5 3 6 2 7 3 8 4 7 5
<< 输出
2 8 3 7 3 5 6 2 8 4 7 5
~~ 说明
如上图所示，6个蓝色像素的坐标依次是 (2,8)、(3,7)、(3,5)、(6,2)、(8,4)、(7,5)。
 */