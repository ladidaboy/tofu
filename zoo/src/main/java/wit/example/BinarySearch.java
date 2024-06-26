package wit.example;

import java.util.Arrays;
import java.util.Random;

/**
 * 二分查找（折半查找）<br>
 * 10个人，分两组，保证能力尽量平均
 */
class BinarySearch {
    public static boolean[] is_visited;

    public static void main(String[] args) {
        Random r = new Random();
        int[] nums = new int[10];
        for (int i = 0; i < 10; i++) {
            // 能力值
            nums[i] = r.nextInt(1000);
        }
        int sum = Arrays.stream(nums).sum();
        System.out.println(Arrays.toString(nums));
        System.out.println("能力总和：" + sum);

        is_visited = new boolean[10];
        for (int min = 0; min <= sum; min++) {
            int target = (sum - min);
            if (target % 2 == 0) {
                if (dfs(target / 2, nums, 0)) {
                    System.out.println("能力差值：" + min);
                    break;
                }
            }
        }
    }

    private static boolean dfs(int target, int[] arr, int index) {
        int length = arr.length;
        if (index > length / 2 || target < 0) {
            return false;
        }
        if (index == length / 2 && target == 0) {
            System.out.println(Arrays.toString(is_visited));
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (is_visited[i]) {
                continue;
            }
            is_visited[i] = true;
            if (dfs(target - arr[i], arr, index + 1)) {
                return true;
            }
            is_visited[i] = false;
        }
        return false;
    }
}
