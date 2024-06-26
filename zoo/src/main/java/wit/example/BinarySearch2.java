package wit.example;

import java.util.Arrays;
import java.util.Random;

/**
 * 某公司部门需要派遣员工去国外做项目。
 * 现在，代号为 x 的国家和代号为 y 的国家分别需要 cntX 名和 cntY 名员工。
 * 部门每个员工有一个员工号 (1,2,3,......)，工号连续，从1开始。
 * <pre>部长派遣员工的规则:
 * 规则1: 从1,k中选择员工派遣出去
 * 规则2: 编号为`x的倍数`的员工不能去x国，编号为`y的倍数`的员工不能去y国
 * </pre>
 * <b>问题</b><br>
 * 找到最小的k，使得可以将编号在[1,k]中的员工分配给 x 国和 y 国，且满足 x 国和 y 国的需求
 */
class BinarySearch2 {

    public static void main(String[] args) {
        int x = 3, y = 5, cx = 7, cy = 8;

        int left = 1, right = Integer.MAX_VALUE, mid, need;
        while (left < right) {
            mid = (left + right) / 2;
            need =
                    /* x */Math.max(0, cx - mid / y + mid / (x * y)) +
                    /* y */Math.max(0, cy - mid / x + mid / (x * y));
            if (mid - mid / x - mid / y + mid / (x * y) >= need) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        System.out.println(left);
    }
}
