package cn.hl.ox.character;

public class Tester {
    public static void main(String[] args) {
        // m：人数，n：蛋糕数，n：m-1个人多分的蛋糕数，k：当前人多分的蛋糕数
        int m = 3, n = 10, nn = n - m, c = 0;
        for (int k = 0; k < nn; k++) {
            // 给下个人分
            c += share(m - 1, nn - k, k);
        }
        System.out.println(c);
    }

    public static int share(int m, int n, int k) {
        if (n <= 0) {
            return 0;
        }
        if (m <= 0) {
            return 0;
        }
        if (m == 1) {
            if (n >= k && n <= k + 3) {
                return 1;
            }
            return 0;
        }
        // 给下个人分
        int nc = 0;
        while (k <= 3) {
            nc += share(m - 1, n - k, k++);
        }
        return nc;
    }
}