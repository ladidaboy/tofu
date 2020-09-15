package cn.hl.kit.ox.data;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author hyman
 * @date 2019-08-26 16:33:51
 * @version $ Id: Tester4ArrayUtils.java, v 0.1  hyman Exp $
 */
public class Tester4ArrayUtils {
    /*
    数组判断函数：
    1.判断是否存在，返回boolean：
    ArrayUtils.contains(T[] array, T objectToFind)
    2.判断是否为空，返回boolean：
    ArrayUtils.isEmpty(T[] array)
    3.判断数组是否相同，返回boolean：
    ArrayUtils.isEquals(T array1, T array2)
    4.判断数组是否相同长度，并且长度不为0，返回boolean：
    ArrayUtils.isSameLength(T[] array1, T[] array2)

    数组增加函数：
    1.添加指定元素到数组中，返回Array：
    ArrayUtils.add(T[] array, T element)
    2.添加指定元素到数组的index位置中，返回Array：
    ArrayUtils.add(T[] array，Int index， T element)
    3.合并两个数组，返回Array：
    ArrayUtils.addAll(T[] array1, T[] array2)
    4.复制数组，返回数组：
    ArrayUtils.clone(T[] array)

    数组移除函数：
    1.移除指定位置的元素，返回Array：
    ArrayUtils.removeElement(T[] array, int element)
    2.移除指定元素，返回Array：
    ArrayUtils.removeElement(T[] array, T element)

    数组查找函数：
    1.查找数组中是否存在，并返回其第一个位置，返回int，-1代表不存在：
    ArrayUtils.indexOf(T[] array, T objectToFind)
    2.查找数组中是否存在，并返回其最后一个位置，返回int，-1表示不存在：
    ArrayUtils.lastIndexOf(T[] array, T objectToFind)
    3.查找指定位置间的子数组，返回子数组Array：
    ArrayUtils.subarray(T[] array, int startIndexInclusive, int endIndexExclusive)

    其他常用函数：
    1.获取数值长度，返回Int：
    ArrayUtils.getLength(T[] array)
    2.数组翻转，该数组本身发生变化，无返回：
    ArrayUtils.reverse(T[] array)
    3.数组转换为Map类型，返回Map：
    ArrayUtils.toMap(T[] array)
    4.数组转换为String，返回String；当数组为null时，返回stringIfNull，可不填，返回“{}”：
    ArrayUtils.toString(T[] array, String stringIfNull)
     */

    public static void main(String[] args) {
        String[] array = {"Ab", "C", "dE", "FGH", "ij", "kL", "MNO", "pq", "rST", "UVW", "X", "yZ"};
        String[] newArray = ArrayUtils.remove(array, 5);
        System.out.println(ArrayUtils.toString(array));
        System.out.println(ArrayUtils.toString(newArray));
    }
}
