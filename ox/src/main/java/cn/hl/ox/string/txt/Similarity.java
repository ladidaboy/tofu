package cn.hl.ox.string.txt;

public class Similarity {
    /**
     * 比较两个字符串的相似度
     * @param word1
     * @param word2
     * @return
     */
    public static String longestCommonSubstring(String word1, String word2) {
        char[] mychar1 = word1.toCharArray();
        char[] mychar2 = word2.toCharArray();
        int m = mychar1.length;
        int n = mychar2.length;
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (mychar1[i - 1] == mychar2[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
                }
            }
        }

        // Display
        Tester.display(matrix, m, n, false);

        char[] result = new char[matrix[m][n]];
        int currentIndex = result.length - 1;
        while (matrix[m][n] != 0) {
            if (matrix[m][n] == matrix[m][n - 1]) {
                n--;
            } else if (matrix[m][n] == matrix[m - 1][n]) {
                m--;
            } else {
                result[currentIndex] = mychar1[m - 1];
                currentIndex--;
                n--;
                m--;
            }
        }
        return new String(result);
    }
}