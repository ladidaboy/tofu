package wit.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * <pre>
 * 给定一个m*n的整数矩阵作为地图，短阵数值为地形高度。
 * 小华选择地图中的任意一点作为起点，尝试往上、下、左、右四个相邻格子移动。
 * 移动时有如下约束:
 *   1、只能上坡或者下坡，不能走到高度相同的点
 *   2、不允许连续上坡或者连续下坡，需要交替进行
 *   3、每个位置只能经过一次，不能重复行走
 * 请给出小华在本地图内，能连续移动的最大次数？</pre>
 * <pre>
 * >> 输入描述 <<
 * 第一行两个数字 m n
 * 后续数据为矩阵地图内容, 矩阵边长范围[1,8], 地形高度范围[0,100000]
 * << 输出描述 >>
 * 一个整数，代表小华在本地图内，能连续移动的最大次数</pre>
 */
public class Dfs2 {
    public static int         row;
    public static int         col;
    public static int[][]     matrix;
    public static int[][]     visited;
    public static int[]       directions = {-1, 0, 1, 0, -1};
    public static List<int[]> maxSteps   = new ArrayList<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        row = in.nextInt();
        col = in.nextInt();
        matrix = new int[row][col];
        visited = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = in.nextInt();
            }
        }

        Stack<int[]> steps = new Stack<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                steps.clear();
                dfs(i, j, steps, true);
                steps.clear();
                dfs(i, j, steps, false);
            }
        }
        System.out.println("Max step count: " + (maxSteps.size() - 1));
        System.out.println(maxSteps.stream().map(s -> String.valueOf(matrix[s[0]][s[1]])).collect(Collectors.joining(" -> ")));
    }

    public static void dfs(int x, int y, Stack<int[]> steps, boolean flag) {
        visited[x][y] = 1;
        steps.push(new int[] {x, y});
        if (steps.size() > maxSteps.size()) {
            maxSteps.clear();
            maxSteps.addAll(steps);
        }
        int i = 1;
        while (true) {
            if (i >= 5) {
                break;
            } else {
                int xx = x + directions[i - 1];
                int yy = y + directions[i];
                if (xx < 0 || yy < 0 || xx >= row || yy >= col ||
                        /**/visited[xx][yy] == 1 ||
                        /**/matrix[xx][yy] == matrix[x][y] ||
                        /**/(flag && matrix[xx][yy] > matrix[x][y]) ||
                        /**/(!flag && matrix[xx][yy] < matrix[x][y])) {
                    i += 1;
                    continue;
                }
                dfs(xx, yy, steps, !flag);
            }
            i += 1;
        }
        visited[x][y] = 0;
        steps.pop();
    }
}
/*
#### 示例 1 ####
输入：
2 2
1 2
4 3
输出：
3
说明：3 > 4 > 1 > 2

#### 示例 2 ####
输入：
3 3
1 2 4
3 5 7
6 8 9
输出：
4
说明：6 > 3 > 5 > 2 > 4
 */