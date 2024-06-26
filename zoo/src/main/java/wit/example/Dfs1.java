package wit.example;

import java.util.Scanner;

/**
 * <pre>
 * 小华按照地图去寻宝，地图上被划分成 m 行和 n 列的方格，横纵坐标范围分别是 [0, n-1] 和 [0, m-1]。
 * 在横坐标和纵坐标的各个数位之和不大于 k 的方格中存在黄金（每个方格中仅存在一克黄金），但横坐标和纵坐标之和大于 k 的方格存在危险不可进入。
 * 小华从入口 (0,0) 进入，任何时候只能向左，右，上，下四个方向移动一格。
 * 请问小华最多能获得多少克黄金？</pre>
 * <pre>
 * >> 输入描述 <<
 * 输入三个字数:    m,        n,        k
 * 坐标取值范围: 0<=m<=50; 0<=n<=50; 0<=k<=100
 * << 输出描述 >>
 * 输出小华最多能获得多少克黄金</pre>
 */
public class Dfs1 {
    public static int[][] visited;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int m = in.nextInt();
        int n = in.nextInt();
        int k = in.nextInt();

        visited = new int[m][n];
        int result = dfs(0, 0, m, n, k);
        System.out.println(result);

    }

    public static int dfs(int x, int y, int m, int n, int k) {
        if (x < 0 || y < 0 || x >= m || y >= n) {
            return 0;
        }
        if (visited[x][y] == 1) {
            return 0;
        }

        int total = 0;
        int xx = x;
        int yy = y;
        while (xx > 0) {
            total += xx % 10;
            xx /= 10;
        }
        while (yy > 0) {
            total += yy % 10;
            yy /= 10;
        }
        if (total > k) {
            return 0;
        }

        visited[x][y] = 1;
        int result = 1;
        if (x + 1 <= m) {
            result += dfs(x + 1, y, m, n, k);
        }
        if (x - 1 >= 0) {
            result += dfs(x - 1, y, m, n, k);
        }
        if (y + 1 <= n) {
            result += dfs(x, y + 1, m, n, k);
        }
        if (y - 1 >= 0) {
            result += dfs(x, y - 1, m, n, k);
        }

        return result;
    }
}
/*
#### 示例 1 ####
输入：
40 40 18
输出：
1484

#### 示例 2 ####
输入：
5 4 7
输出：
20
 */