package wit.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * <pre>输入 m 和 n 两个数，m 和 n 表示一个 m*n 的棋盘。
 * 输入棋盘内的数据。
 * 棋盘中存在数字和"."两种字符，如果是数字表示该位置是一匹马，如果是"."表示该位置为空的，棋盘内的数字表示为该马能走的最大步数。
 * 例如棋盘内某个位置一个数字为 k，表示该马只能移动 1~k 步的距离。
 * 棋盘内的马移动类似于中国象棋中的马移动，先在水平或者垂直方向上移动一格，然后再将其移动到对角线位置。
 * 棋盘内的马可以移动到同一个位置，同一个位置可以有多匹马。
 * 请问能否将棋盘上所有的马移动到同一个位置，若可以请输出移动的最小步数，若不可以输出 0。</pre>
 * <pre>
 * >> 输入描述 <<
 * 输入m 和 n 两个数，m 和 n 表示一个 m*n 的棋盘。
 * 输入棋盘内的数据。
 * << 输出描述 >>
 * 能否将棋盘上所有的马移动到同一个位置，若可以请输出移动的最小步数，若不可以输出 0。
 * </pre>
 * == 解题思路 ==<ol>
 * <li>棋盘类的跳跃问题，BFS或者DFS的经典问题。</li>
 * <li>题目要求我们求出多匹马移动到同一个位置的最小步数，我们可以拆解下这个问题。<br>
 * 我们先假定只有一匹马，那么我们用简单的BFS/DFS就可以求出这匹马到棋盘上每个位置所需要的步数。</li>
 * <li>那么对于多匹马，我们为每一匹马都计算出这样一个结果。并对结果进行对应位置的判断以及累加即可得出结果。</li>
 * </ol>
 */
public class Bfs1 {
    public static int     m;
    public static int     n;
    public static int[][] matrix;
    public static int[][] result;
    public static int[][] directions = {
            /**/{2, 1}, {2, -1},
            /**/{-2, 1}, {-2, -1},
            /**/{1, 2}, {1, -2},
            /**/{-1, 2}, {-1, -2}};

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        m = in.nextInt();
        n = in.nextInt();
        in.nextLine();
        result = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0;
            }
        }
        matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            String[] inputs = in.nextLine().split(" ");
            for (int j = 0; j < n; j++) {
                if (!inputs[j].equals(".")) {
                    clearMatrix();
                    bfs(i, j, Integer.parseInt(inputs[j]));
                    updateResult();
                }

            }
        }

        int output = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (result[i][j] != -1) {
                    output = Math.min(output, result[i][j]);
                }
            }
        }
        if (output == Integer.MAX_VALUE) {
            System.out.println(0);
        } else {
            System.out.println(output);
        }

    }

    public static void clearMatrix() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = -1;
            }
        }
    }

    public static void updateResult() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == -1 || result[i][j] == -1) {
                    result[i][j] = -1;
                } else {
                    result[i][j] += matrix[i][j];
                }
            }
        }
    }

    public static void bfs(int i, int j, int step) {
        matrix[i][j] = 0;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] {i, j});
        int index = 1;
        while (true) {
            if (queue.isEmpty() || index > step) {
                break;
            } else {
                int size = queue.size();
                for (int k = 0; k < size; k++) {
                    int[] current = queue.poll();
                    for (int l = 0; l < 8; l++) {
                        int ni = current[0] + directions[l][0];
                        int nj = current[1] + directions[l][1];
                        if (0 <= ni && ni < m && 0 <= nj && nj < n && matrix[ni][nj] == -1) {
                            matrix[ni][nj] = index;
                            queue.add(new int[] {ni, nj});
                        }
                    }

                }
            }
            index += 1;
        }
    }
}
/*
## 示例1:
输入
3 2
. .
2 .
. .
输出
0

## 示例2:
输入
3 5
4 7 . 4 8
4 7 4 4 .
7 . . . .
输出
17
 */