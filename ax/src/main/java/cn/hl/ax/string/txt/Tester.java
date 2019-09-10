package cn.hl.ax.string.txt;

import cn.hl.ax.data.DataUtils;

public class Tester {
    public static void display(int[][] dis, int rows, int cols, boolean flag) {
        System.err.print(flag ? "-->EditDistance<--" : "-->Similarity<----");
        for (int idx = 6; idx <= cols; idx++) {
            System.err.print("--+");
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
        System.out.print("\r\n \\|");
        for (int col = 0; col < cols; col++) {
            System.out.print(String.format("%1$2d|", col));
        }
        System.out.println();
        for (int col = 0; col <= cols; col++) {
            System.out.print("--+");
        }
        System.out.println();
        for (int row = 0; row < rows; row++) {
            System.out.print(String.format("%1$2d|", row));
            for (int col = 0; col < cols; col++) {
                System.out.print(String.format("%1$2d|", dis[row][col]));
            }
            System.out.println();
        }
        System.out.println();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        String word2 = "我我我我我我违法3 放332 放费23 放\r\n请欺负求粉2我我我我我我我我我我";
        String word1 = "我的我付钱费2的我的我发请的我的钱费2的我的\t放前放去2求粉3放我的我的的我的发前2我的我的我放前2前的我我的发前2我的我的我的";
        word1 = DataUtils.removeSign(word1);
        word2 = DataUtils.removeSign(word2);

        // EditDistance 字符距离值
        int dis = EditDistance.process(word1, word2);
        // Similarity 字符相似度
        String longestCommonSubstr = Similarity.longestCommonSubstring(word1, word2);
        int sim = longestCommonSubstr.length();

        double diff = 100.0;
        int max = Math.max(word1.length(), word2.length());
        System.out.println("-- Result ------------------------------------------");
        System.out.println("word1: " + word1);
        System.out.println("word2: " + word2);
        System.out.println("Max Length: " + max);
        // 输出结果
        System.out.println("-- EditDistance ------------------------------------");
        System.out.println("Difference Length: " + dis);
        diff = 100.0 * (max - dis) / max;
        System.out.println(diff + " ≈ " + String.format("%1$.2f%%", diff));

        System.out.println("-- Similarity --------------------------------------");
        System.out.println("Similarity Length: " + sim);
        diff = 100.0 * sim / max;
        System.out.println(diff + " ≈ " + String.format("%1$.2f%%", diff));
        System.out.println("-- LongestCommonSubstr -----------------------------");
        System.out.println(longestCommonSubstr);
    }
}