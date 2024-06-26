package wit.example;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * 疫情期间需要大家保证一定的社交距离，公司组织开交流会议。<br>
 * 座位一排共 N 个座位，编号分别为[0,N-1]，要求员工一个接着一个进入会议室，并且可以在任何时候离开会议室。<br>
 * 满足：<ul>
 * <li>每当一个员工进入时，需要坐到最大社交距离 (最大化自己和其他人的距离的座位)</li>
 * <li>如果有多个这样的座位，则坐到索引最小的那个座位。</li>
 * </ul>
 * <pre>
 * >> 输入描述 <<
 * 会议室座位总数seatNum。(1 <= seatNum <= 500)
 * 员工的进出顺序 seatOrLeave 数组，元素值为 1，表示进场；元素值为负数，表示出场（特殊：位置 0 的员工不会离开）。
 * 例如 -4 表示坐在位置 4 的员工离开（保证有员工坐在该座位上）
 * << 输出描述 >>
 * 最后进来员工，他会坐在第几个位置，如果位置已满，则输出-1。
 * </pre>
 */
public class Seat {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int seatNum = sc.nextInt();
        sc.nextLine();
        String seatOrLeaveLine = sc.nextLine();
        String[] c = seatOrLeaveLine.substring(1, seatOrLeaveLine.length() - 1).split(", ");
        int[] seatOrLeave = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            seatOrLeave[i] = Integer.parseInt(c[i]);
        }

        Seat socialDistance = new Seat();
        int ans = socialDistance.conferenceSeatDistance(seatNum, seatOrLeave);
        System.out.println(ans);
    }

    /**
     * 计算最后进来的人，坐的位置
     *
     * @param seatNum     会议室座位总数
     * @param seatOrLeave 员工的进出顺序
     * @return 最后进来的人，坐的位置
     */
    public int conferenceSeatDistance(int seatNum, int[] seatOrLeave) {
        TreeSet<Integer> seated = new TreeSet<>();
        for (int i = 0; i < seatOrLeave.length; i++) {
            int next = -1;
            if (seatOrLeave[i] > 0) {
                if (seated.size() == 0) {
                    // 第一步
                    next = 0;
                } else if (seated.size() == 1) {
                    // 第二步
                    next = seatNum - 1;
                } else if (seated.size() < seatNum) {
                    // 第三步之后
                    int[] cs = new int[seated.size()];
                    int idx = 0;
                    for (Integer integer : seated) {
                        cs[idx++] = integer;
                    }
                    int max = 0, step;
                    for (int j = 0; j < cs.length - 1; j++) {
                        step = (cs[j + 1] - cs[j]) / 2;
                        if (step > max) {
                            next = cs[j] + step;
                            max = step;
                        }
                    }
                } else {
                    // 没法坐了
                    return -1;
                }
                seated.add(next);
            } else {
                seated.remove(-seatOrLeave[i]);
            }
            // 没有步骤了
            if (i == seatOrLeave.length - 1) {
                return next;
            }
        }
        return 0;
    }
}
/*
#### 示例 1 ####
输入
10
[1, 1, 1, 1, -4, 1, 1]
输出
5
说明
seat -> 0, 空在任何位置都行，但是要给他安排索引最小的位置，也就是座位 0
seat -> 9, 要和旁边的人距离最远，也就是座位 9
seat -> 4, 要和旁边的人距离最远，应该坐到中间，也就是座位 4
seat -> 2, 员工最后坐在 2 号座位上
leave(4),  4 号座位的员工离开
seat -> 5, 员工最后坐在 5 号座位上
 */