package cn.hl.kit.ox.data;

import cn.hl.kit.ax.data.AryTransfer;
import cn.hl.kit.ox.BuddhaBless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author hyman
 * @date 2020-01-07 10:21:49
 * @version $ Id: Tester4Iterator.java, v 0.1  hyman Exp $
 */
public class Tester4Iterator {
    private static final String KEY = "XX";

    private static void test4Iterate4Map(Map<String, String> map) {
        //第一种：普遍使用，二次取值
        System.out.println("== 通过Map.keySet遍历key和value：");
        for (String key : map.keySet()) {
            System.out.println("K= " + key + ", V= " + map.get(key));
        }

        //第二种:通过Iterator迭代器遍历循环Map.entrySet().iterator();
        System.out.println("== 通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            System.out.println("K= " + entry.getKey() + ", V= " + entry.getValue());
        }

        //第三种：笔者推荐，尤其是容量大时(相对来说 比2好一点 效率高)
        System.out.println("== 通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("K= " + entry.getKey() + ", V= " + entry.getValue());
        }

        //第四种
        System.out.println("== 通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("V= " + v);
        }
    }

    private static void test4Delete4List(List<String> list) {
        BuddhaBless.printHeadline("test4Delete4List", list);
        // [ERROR] 示例一
        /*for (String str : list) {
            if (str.equals(KEY)) {
                list.remove(str);
            }
        }
        System.out.println(list);*/

        // [ERROR] 示例二：这种情况不会报错，因为最后一个元素未被遍历到就程序跳出了循环遍历
        for (String str : list) {
            if (str.equals(KEY)) {
                list.remove(str);
            }
        }
        System.out.println(list);

        // 方法一：for循环遍历
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if (KEY.equals(item)) {
                //逻辑判断，剔除满足条件的元素
                list.remove(item);
                //元素删除后，后面的元素位置向前挪一位,位置索引i需要自减一，否则访问不到后面的元素
                i--;
            }
        }
        System.out.println(list);

        // 方法二：使用集合的迭代器
        Iterator<String> it = list.iterator();
        for (; it.hasNext(); ) {
            if (KEY.equals(it.next())) {
                //使用迭代器的删除方法,不要使用集合对象的删除方法remove
                it.remove();
            }
        }
        System.out.println(list);

        /*
        原因说明：
        1. 如果使用增强for循环进行遍历，那么实际上使用的是集合的迭代器进行遍历，这时如果使用的是集合对象自己的删除方法，
           迭代器不知道元素集合发生了变化，进行获取下一个元素的时候就会爆出错误：ConcurrentModificationException，
           但这里有一点需要注意，如果遍历过程中第一个被删除的元素处在倒数第二个被遍历的位置时，程序不会报错，正常结束。
           实际的结果是：集合中最后一个元素之前，会调用迭代器的hasNext()方法，该方法发现当前位置索引等于集合元素数量，
           判断遍历结束，跳出循环，实则最后一个元素未被遍历到。
        2. 第一种遍历删除方法，是自己控制并调整元素位置索引，只要位置索引控制正确，就可以正常删除元素，否则会出现IndexOutOfBoundsException
        3. 第二种遍历删除直接使用迭代器进行遍历，而且删除时使用的迭代器的删除元素方法，这个删除方法里面会对位置索引进行调整，
           使得迭代器在继续遍历查找下一个元素时，不会抛出异常错误。
         */
    }

    public static void main(String[] args) {
        Random rd = new Random();

        //
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String key = AryTransfer.to26Ary(rd.nextInt(900000000) + 100000000);
            String val = AryTransfer.to26Ary(rd.nextInt(900000000) + 100000000);
            map.put(key, val);
        }
        test4Iterate4Map(map);

        //
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(AryTransfer.to26Ary(rd.nextInt(260)));
        }
        list.add(KEY);
        list.add(KEY);
        test4Delete4List(list);
    }
}
