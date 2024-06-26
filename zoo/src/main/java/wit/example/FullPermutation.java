package wit.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 思路及解法
 * 注意到下一个排列总是比当前排列要大，除非该排列已经是最大的排列。
 * 我们希望找到一种方法，能够找到一个大于当前序列的新序列，且变大的幅度尽可能小。具体地：
 * <ol>
 * <li>我们需要将一个左边的「较小数」与一个右边的「较大数」交换，以能够让当前排列变大，从而得到下一个排列。</li>
 * <li>同时我们要让这个「较小数」尽量靠右，而「较大数」尽可能小。<br>
 *     当交换完成后，「较大数」右边的数需要按照升序重新排列。<br>
 *     这样可以在保证新排列大于原来排列的情况下，使变大的幅度尽可能小。 </li>
 * </ol>
 * <i>以排列 [4,5,2,6,3,1] 为例：</i>
 * <ul>
 * <li>我们能找到的符合条件的一对「较小数」与「较大数」的组合为 2 与 3，满足「较小数」尽量靠右，而「较大数」尽可能小。</li>
 * <li>当我们完成交换后排列变为 [4,5,3,6,2,1]，此时我们可以重排「较小数」右边的序列，序列变为 [4,5,3,1,2,6]。</li>
 * </ul>
 * 具体地，我们这样描述该算法，对于长度为 n 的排列 a：
 * <ol>
 * <li>首先从后向前查找第一个顺序对 (i,i+1)，满足 a[i]&lt;a[i+1]，这样「较小数」即为 a[i]，此时 [i+1,n) 必然是下降序列。</li>
 * <li>如果找到了顺序对，那么在区间 [i+1,n) 中从后向前查找第一个元素 j 满足 a[i]&lt;a[j]，这样「较大数」即为 a[j]。</li>
 * <li>交换 a[i] 与 a[j]，此时可以证明区间 [i+1,n) 必为降序。我们可以直接使用双指针反转区间 [i+1,n) 使其变为升序，而无需对该区间进行排序。</li>
 * </ol>
 */
public class FullPermutation {

    public static void main(String[] args) {
        String input = "HELLO";
        List<String> output = new ArrayList<>();

        char[] arr = input.toCharArray();
        Arrays.sort(arr);
        do {
            output.add(new String(arr));
        } while (nextPermutation(arr));

        System.out.println(output.size());
        output.forEach(System.out::println);
    }

    public static boolean nextPermutation(char[] arr) {
        int i = arr.length - 2;
        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }
        if (i < 0) {
            return false;
        }
        int j = arr.length - 1;
        while (j >= 0 && arr[i] >= arr[j]) {
            j--;
        }
        swap(arr, i, j);
        reverse(arr, i + 1);
        return true;
    }

    public static void swap(char[] arr, int i, int j) {
        char t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void reverse(char[] arr, int start) {
        int left = start, right = arr.length - 1;
        while (left < right) {
            swap(arr, left, right);
            left++;
            right--;
        }
    }
}
