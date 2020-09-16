package cn.hl.ox.string.txt;

/**
 * 编辑距离（EditDistance）定义字符串的相似度
 * <P>
 * <br>编辑距离就是用来计算从原串（s）转换到目标串(t)所需要的最少的插入，删除和替换的数目，
 * 在NLP中应用比较广泛，如一些评测方法中就用到了（wer,mWer等），同时也常用来计算你对原文本所作的改动数。
 * <br>编辑距离的算法是首先由俄国科学家Levenshtein提出的，故又叫Levenshtein Distance。
 * <br>Levenshtein Distance算法可以看作动态规划。它的思路就是从两个字符串的左边开始比较，
 * 记录已经比较过的子串相似度(实际上叫做距离),然后进一步得到下一个字符位置时的相似度。 
 * <br>用下面的例子: GUMBO和GAMBOL。
 * <br>当算到矩阵D[3,3]位置时，也就是当比较到GUM和GAM时，要从已经比较过的3对子串GU-GAM，
 *     GUM-GA和GU-GA之中选一个差别最小的来当它的值。所以要从左上到右下构造矩阵。
 * <p>
 * 编辑距离的伪算法：
 * <pre>
 * 整数 Levenshtein距离(字符 str1[1..lenStr1], 字符 str2[1..lenStr2])
 *     宣告 int d[0..lenStr1, 0..lenStr2]
 *     宣告 int i, j, cost
 *
 *     对于 i 等于 由 0 至 lenStr1
 *         d[i, 0] := i
 *     对于 j 等于 由 0 至 lenStr2
 *         d[0, j] := j
 *     对于 i 等于 由 1 至 lenStr1
 *     对于 j 等于 由 1 至 lenStr2
 *     若 str1[i] = str2[j] 则 cost := 0
 *     否则 cost := 1
 *     d[i, j] := 最小值(
 *         d[i-1, j ] + 1,           // 删除
 *         d[i , j-1] + 1,           // 插入
 *         d[i-1, j-1] + cost        // 替换
 *     )
 *     返回 d[lenStr1, lenStr2]
 * </pre>
 * 
 * @author Halfman
 *
 */
public class EditDistance {
    /**
     * 计算两个字符串之间的编辑距离
     * @param word1
     * @param word2
     * @return
     */
    public static int process(String word1, String word2) {
        int distance = 0;
        char[] mychar1 = word1.toCharArray();
        char[] mychar2 = word2.toCharArray();
        int len1 = mychar1.length;
        int len2 = mychar2.length;
        int cost;
        int boundaryX = len1 + 1;
        int boundaryY = len2 + 1;
        int[][] dis = new int[boundaryX][boundaryY];
        for (int i = 0; i <= len1; i++) dis[i][0] = i;
        for (int j = 0; j <= len2; j++) dis[0][j] = j;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (mychar1[i - 1] == mychar2[j - 1]) cost = 0;
                else cost = 1;

                dis[i][j] = getMin(dis[i - 1][j] + 1, dis[i][j - 1] + 1, dis[i - 1][j - 1] + cost);
            }
        }
        distance = dis[len1][len2];

        // Display
        Tester.display(dis, boundaryX, boundaryY, true);

        return distance;
    }

    // 返回三个int的最小数
    private static int getMin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}