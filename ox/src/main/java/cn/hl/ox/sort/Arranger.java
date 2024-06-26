package cn.hl.ox.sort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Arranger {
    public static void main(String[] args) throws IOException {
        test1();
    }

    private static void test1() {
        String[] numbers = {"1", "2", "3", "4"};
        Arranger arranger = new Arranger(numbers);

        int idx = 0;
        for (List<String> result : arranger.getResults()) {
            System.out.println(String.format("%02d", idx++) + "." + result);
        }
    }

    private static void test2() throws IOException {
        String[] numbers = {"黄色", "水红", "粉色", "红色", "米色"};
        Arranger arranger = new Arranger(numbers);

        int idx = 0;
        for (List<String> result : arranger.getResults()) {
            File file = new File("p" + (idx++) + ".txt");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(String.format("" +
                    /*01*/"青色 灰色 黄色 蓝色\n" +
                    /*02*/"紫兰 紫色 浅蓝 水红\n" +
                    /*03*/"青色 橙色 粉色 灰色\n" +//
                    /*04*/"红色 酒红 浅蓝 黄色\n" +
                    /*05*/"%s 米色 紫色 青色\n" +
                    /*06*/"%s 紫兰 紫色 红色\n" +
                    /*07*/"橙色 水红 浅蓝 绿色\n" +
                    /*08*/"%s 米色 水红 酒红\n" +
                    /*09*/"米色 绿色 酒红 橙色\n" +
                    /*10*/"蓝色 红色 粉色 浅蓝\n" +
                    /*11*/"%s 紫色 灰色 紫兰\n" +
                    /*12*/"绿色 橙色 粉色 蓝色\n" +
                    /*13*/"%s 青色 灰色 蓝色\n" +
                    /*14*/"紫兰 绿色 黄色 酒红\n" +//
                    /**/"\n\n.", result.toArray()).getBytes());
        }
    }

    // 保存在内部的原始元素数组的引用
    private final String[] rawElements;

    // 返回结果
    private final List<List<String>> results;

    /**
     * 构造函数
     *
     * @param raws 原始元素数组
     */
    public Arranger(String[] raws) {
        rawElements = raws;
        results = new ArrayList<>();
        doArrange(new ArrayList<>());
    }

    /**
     * 使用递归进行全排列，结果放在results中
     *
     * @param initialList 初始链表
     */
    private void doArrange(List<String> initialList) {
        List<String> innerList = new ArrayList<>(initialList);

        if (rawElements.length == initialList.size()) {
            results.add(innerList);
        }

        for (String rawElement : rawElements) {
            if (innerList.contains(rawElement)) {
                continue;
            }

            innerList.add(rawElement);
            doArrange(innerList);
            innerList.remove(innerList.size() - 1);
        }
    }

    /**
     * 获得结果链表的引用
     */
    public List<List<String>> getResults() {
        return results;
    }
}
