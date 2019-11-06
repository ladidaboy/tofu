package cn.hl.ox.enume;

import cn.hl.ax.enume.EnumUtils;
import cn.hl.ox.BuddhaBless;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author hyman
 * @date 2019-08-22 09:44:34
 * @version $ Id: Tester.java, v 0.1  hyman Exp $
 */
public class Tester {
    public static void main(String[] args) {
        // 遍历Enum
        for (Week e : Week.values()) {
            System.out.println("➢ " + e.toString() + " IsRest: " + e.isRest());
        }
        // EnumSet的使用
        EnumSet<Week> weekSet = EnumSet.allOf(Week.class);
        for (Week day : weekSet) {
            System.out.println("➣ " + day);
        }
        // EnumMap的使用
        EnumMap<Week, String> weekMap = new EnumMap(Week.class);
        weekMap.put(Week.MON, "星期一");
        weekMap.put(Week.TUE, "星期二");
        // ... ...
        for (Iterator<Entry<Week, String>> iter = weekMap.entrySet().iterator(); iter.hasNext(); ) {
            Entry<Week, String> entry = iter.next();
            System.out.println("➢ " + entry.getKey().name() + ":" + entry.getValue());
        }
        BuddhaBless.printSplitLine();

        Week test = EnumUtils.fromOrdinal(Week.class, (int) (Math.random() * 8));
        switch (test) {
            case MON:
                System.out.println("➣ 今天是星期一");
                break;
            case TUE:
                System.out.println("➣ 今天是星期二");
                break;
            case WED:
                System.out.println("➣ 今天是星期三");
                break;
            case THU:
                System.out.println("➣ 今天是星期四");
                break;
            case FRI:
                System.out.println("➣ 今天是星期五");
                break;
            case SAT:
                System.out.println("➣ 今天是星期六");
                break;
            case SUN:
                System.out.println("➣ 今天是星期日");
                break;
            default:
                System.out.println(test);
                break;
        }
        //compareTo(E o)
        switch (test.compareTo(Week.MON)) {
            case -1:
                System.out.println("TUE 在 MON 之前");
                break;
            case 1:
                System.out.println("TUE 在 MON 之后");
                break;
            default:
                System.out.println("TUE 与 MON 在同一位置");
                break;
        }
        BuddhaBless.printSplitWave();

        Week week = EnumUtils.fromName(Week.class, "SAT");
        // getDeclaringClass()
        System.out.println("➢ getDeclaringClass(): " + week.getDeclaringClass().getName());
        // name() 和 toString()
        System.out.println("➣ name(): " + week.name());
        System.out.println("➣ toString(): " + week.toString());
        // ordinal() 返回值是从 0 开始
        System.out.println("➢ ordinal(): " + week.ordinal());
        BuddhaBless.printSplitLine();

        MyEnum myenum = EnumUtils.fromField(MyEnum.class, "tag", "WoWo");
        System.out.println("EnumUtils.fromField(MyEnum, tag, WoWo)-> " + myenum);
        myenum = EnumUtils.fromField(MyEnum.class, "no", 3);
        System.out.println("EnumUtils.fromField(MyEnum, no, 3)-> " + myenum);
        System.out.println("EnumUtils.isSame(MyEnum, wow)-> " + EnumUtils.isSame(myenum, "wow"));

        boolean flag = org.apache.commons.lang3.EnumUtils.isValidEnum(MyEnum.class, "WOW");
        System.out.println("commons->EnumUtils.isValidEnum(MyEnum, WOW) := " + flag);
    }
}
