package wit.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 现需要在某城市进行 5G 网络建设，已经选取 N 个地点设置 5G 基站，编号固定为 1 到 N，接下来需要各个基站之间使用光纤进行连接以确保基站能互联互通，
 * 不同基站之间架设光纤的成本各不相同，且有些节点之间已经存在光纤相连，请你设计算法，计算出能联通这些基站的最小成本是多少。<br>
 * 注意，基站的联通具有传递性，即基站 A 与基站 B 架设了光纤，基站 B 与基站 C 也架设了光纤，则基站 A 与基站 C 视为可以互相联通。
 * <pre>
 * >> 输入描述 <<
 * 第一行输入表示基站的个数 N，其中 0 < N <= 20
 * 第二行输入表示具备光纤直连条件的基站对的数目 M，其中 0 < M < N * (N - 1) / 2
 * 第三行开始连续输入 M 行数据，格式为 X Y Z P，
 *   其中 X Y 表示基站的编号，0 < X <= N， 0 < Y <= N 且 X 不等于 Y，
 *   Z 表示在 X Y 之间架设光纤的成本，其中 0 < Z < 100，
 *   P 表示是否已存在光纤连接，0 表示未连接， 1 表示已连接。
 *
 * << 输出描述 >>
 * 如果给定条件，可以建设成功互联互通的 5G 网络，则输出最小的建设成本，
 * 如果给定条件，无法建设成功互联互通的 5G 网络，则输出 -1
 *
 * == <b>解题思路</b> ==
 *   1.可以用并查集来解决这种图联通类型的问题。
 *   2.并查集属于数据结构的一种，是高等数据结构最基础的一部分，主要分为普通并查集、种类并查集以及带权并查集。
 *     它是一种用于管理元素所属集合的数据结构，这里的集合我们可以理解为一颗树，每个元素都是树上的有一个分叉，
 *     顺着分叉我们可以找到这棵树的根，也就是这个集合里所有元素的“祖先”。
 *   3.主要操作：
 *     - 查找（find）：用于查找每棵树的根，也就是这个集合所有元素的祖先。
 *       这样就可以判断两个元素是否属于一个集合，就是看两个元素的祖先是否相同。
 *     - 合并（merge）：用于合并两颗树，将两个集合合并。
 *     - 求大小（size）：可以统计每个集合元素的个数。
 * </pre>
 * 可以看下这篇文章，有很多样例的介绍：https://blog.csdn.net/iGxy0929/article/details/128684399
 */
public class UnionFind {
    //https://leetcode.cn/problems/number-of-provinces/solutions/1975727/li-kou-547-bing-cha-ji-si-lu-jiang-jie-b-u9qh/
    int[] root;
    int[] rank;
    int   count;

    UnionFind(int size) {
        root = new int[size + 1];
        rank = new int[size + 1];
        count = 0;
        for (int i = 0; i < size + 1; i++) {
            root[i] = i;
            rank[i] = 1;
        }
    }

    int find(int x) {
        if (x == root[x]) {
            return x;
        }
        return root[x] = find(root[x]);
    }

    void union(int x, int y) {
        root[find(x)] = find(y);
        count += 1;
    }

    int getCount() {
        return count;
    }

    ////

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        in.nextLine();
        UnionFind finder = new UnionFind(n);
        List<int[]> networks = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] nums = Arrays.stream(in.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (nums[3] == 1) {
                if (finder.find(nums[0]) != finder.find(nums[1])) {
                    finder.union(nums[0], nums[1]);
                }
            } else {
                networks.add(new int[] {nums[0], nums[1], nums[2]});
            }
        }
        networks.sort(Comparator.comparingInt(o -> o[2]));

        int i = 0, costs = 0;
        while (i < networks.size()) {
            if (finder.find(networks.get(i)[0]) != finder.find(networks.get(i)[1])) {
                finder.union(networks.get(i)[0], networks.get(i)[1]);
                costs += networks.get(i)[2];
                if (finder.getCount() == n - 1) {
                    break;
                }
            }
            i++;
        }

        if (finder.getCount() != n - 1) {
            costs = -1;
        }
        System.out.println(costs);
        System.exit(0);
    }
}
/*
#### 示例 1 ####
输入
3
3
1 2 3 0
1 3 1 0
2 3 5 0
输出
4
说明
只需要在 1,2 以及 2,3 基站之间铺设光纤，其成本为 3+1=4

#### 示例 2 ####
输入
3
1
1 2 5 0
输出
-1
说明
3 基站无法与其他基站连接，输出-1

#### 示例 3 ####
输入
3
3
1 2 3 0
1 3 1 0
2 3 5 1
输出
1
说明
2,3 基站已有光纤相连，只有要再 1,3 站点 2 向铺设，其成本为 1
 */