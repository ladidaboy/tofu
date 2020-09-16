package cn.hl.ax.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author hyman
 * @date 2019-08-28 13:25:25
 */
public class ArrayUtils {
    /**
     * 数组去重
     */
    public static String[] makeUnique(String[] arr) {
        Set<String> set = new HashSet<>(Arrays.asList(arr));
        return set.toArray(new String[0]);
    }

    /**
     * 交叉比较两数组
     * @param arr1 数组1
     * @param arr2 数组2
     * @param isIntersectOrDiffer true: Intersect交集; false: Differ差集
     * @return 集合
     */
    public static String[] crossCollection(String[] arr1, String[] arr2, boolean isIntersectOrDiffer) {
        if (DataUtils.isInvalid(arr1) || DataUtils.isInvalid(arr2)) {
            return new String[0];
        }
        String[] arr = DataUtils.add2Array(makeUnique(arr1), makeUnique(arr2));
        Map<String, Boolean> single = new HashMap<>();
        LinkedList<String> list = new LinkedList<>();
        for (String txt : arr) {
            single.put(txt, !single.containsKey(txt));
        }
        for (Entry<String, Boolean> e : single.entrySet()) {
            if (isIntersectOrDiffer && !e.getValue()) {
                list.add(e.getKey());
            } else if (!isIntersectOrDiffer && e.getValue()) {
                list.add(e.getKey());
            }
        }
        return list.toArray(new String[] {});
    }

    /**
     * 两数组交集
     */
    public static String[] intersect(String[] arr1, String[] arr2) {
        return crossCollection(arr1, arr2, true);
    }

    /**
     * 两数组差集
     */
    public static String[] differ(String[] arr1, String[] arr2) {
        return crossCollection(arr1, arr2, false);
    }

    /**
     * 两数组并集
     * <br>利用HashSet不重复的特性，也可以修改交集
     */
    public static String[] union(String[] arr1, String[] arr2) {
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(arr1));
        set.addAll(Arrays.asList(arr2));
        return set.toArray(new String[] {});
    }

    public static String maxIntersect(String max, String min) {
        if (max.length() < min.length()) {
            String ttt = max;
            max = min;
            min = ttt;
        }
        String sub = min;
        int begin = 0, end = min.length(), i = 1, counter = 0;
        while (!max.contains(sub)) {
            if (end == min.length()) {
                begin = 0;
                end = (min.length()) - (i++);
            } else {
                begin++;
                end++;
            }
            sub = min.substring(begin, end);
            counter++;
        }
        System.out.println("maxIntersect -> O(m #" + max.length() + " × n #" + min.length() + ") = " + counter);
        return sub;
    }
}
