package wit.example;

import java.util.Scanner;

/**
 * <img width="500" src="Singer.jpg"/>
 */
public class Singer {
    private static int minTest(int[] heights) {
        int n = heights.length;
        int[] ldp = new int[n];
        int[] rdp = new int[n];
        for (int i = 0; i < n; i++) {
            ldp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (heights[i] > heights[j]) {
                    ldp[i] = Math.max(ldp[i], ldp[j] + 1);
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            rdp[i] = 1;
            for (int j = n - 1; j > i; j--) {
                if (heights[i] > heights[j]) {
                    rdp[i] = Math.max(rdp[i], rdp[j] + 1);
                }
            }
        }

        // 找到一个同学使得左侧和右侧最长递增子序列长度之和最大
        int res = 0;
        for (int i = 0; i < n; i++) {
            res = Math.max(res, rdp[i] + ldp[i] - 1);
        }
        return n - res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = sc.nextInt();
        }

        int res = minTest(heights);
        System.out.println(res);
    }
}
/*
#### 示例 1 ####
输入：
8
186 186 150 200 160 130 197 200
输出：
4
说明：
由于不允许改变队列元素的先后顺序，所以最终剩下的队列应该为186 200 160 130或150 200 160 130
 */