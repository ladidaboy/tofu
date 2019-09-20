package cn.hl.ox.permutations;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 排列算法 |n A| = m!/(m-n)! |m
 */
public class FullSort {
    // 将NUM设置为待排列数组的长度即实现全排列
    private static int NUM = 3;
    private static int CNT = 0;

    private static FileWriter writer;

    private static void print(List<String> datas) {
        try {
            if (writer == null) {
                writer = new FileWriter("out.txt", true);
            }
            for (String obj : datas) {
                writer.write(obj);
            }
            writer.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BigDecimal factorial(BigDecimal input) {
        BigDecimal one = new BigDecimal(1);// BigDecimal类型的1
        BigDecimal two = new BigDecimal(2);// BigDecimal类型的2
        BigDecimal result = one;// 结果集，初值取1
        while (input.compareTo(one) > 0) {// 参数大于1，进入循环
            result = result.multiply(input.multiply(input.subtract(one)));// 实现result*(n*(n-1))
            input = input.subtract(two);// n-2后继续
        }
        return result;
    }

    /**
     * 递归算法：将数据分为两部分，递归将数据从左侧移右侧实现全排列
     *
     * @param source
     * @param target
     */
    private static void sort(List<String> source, List<String> target) {
        if (target.size() == NUM) {
            print(target);
            CNT++;
            return;
        }
        for (int i = 0; i < source.size(); i++) {
            List<String> _source_ = new ArrayList<String>(source);
            List<String> _target_ = new ArrayList<String>(target);
            _target_.add(_source_.get(i));
            _source_.remove(i);
            sort(_source_, _target_);
        }
    }

    public static void main(String[] args) throws IOException {
        String[] source = new String[] {"58", "2", "712", "330", "610", "3", "6", "36", "63", "66", "9", "86", "89", "@"};
        sort(Arrays.asList(source), new ArrayList<String>());
        System.out.println("A(m,n): " + factorial(new BigDecimal(source.length)).divide(factorial(new BigDecimal(source.length - NUM))));
        System.out.println("COUNTS: " + CNT);
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

}
