package cn.hl.ox.clone;

import cn.hl.ax.clone.CloneBean;
import cn.hl.ax.clone.ReflectionUtils;
import cn.hl.ax.log.LogUtils;
import cn.hl.ox.clone.bean.Alpaca;
import cn.hl.ox.clone.bean.Animal;
import cn.hl.ox.clone.bean.BP;
import cn.hl.ox.clone.bean.BS;
import cn.hl.ox.clone.bean.DataTypeOfAdvanced;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        try {
            test0();
        } catch (Exception e) {
            e.printStackTrace();
        }

        test1();

        test2();

        try {
            test3();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            test4();
        } catch (Exception e) {
            e.printStackTrace();
        }

        test5();
    }

    // test CloneBean.newInstance.deepCopy
    private static void test0() throws Exception {
        LogUtils.printHeadlineEx("test0: 测试对象自身的深度拷贝(普通的Animal / 自实现对transient对象拷贝的Alpaca)", 128);

        Alpaca first = new Alpaca("#1st");
        Alpaca second = new Alpaca("#2nd");
        first.setNext(second);
        second.setNext(null);

        CloneBean third = first.deepCopy();
        System.out.println(first);
        System.out.println(third);

        LogUtils.printSplitWave(68);

        Animal forth = new Animal("#4th");
        Animal fifth = new Animal("#5th");
        forth.setNext(fifth);
        fifth.setNext(null);

        Animal sixth = forth.deepCopy();
        System.out.println(forth);
        System.out.println(sixth);
    }

    // test CloneBean.selfClone
    private static void test1() {
        LogUtils.printHeadlineEx("test1: CloneBean.selfClone", 128);
        BS org = new BS(), out = null;
        // ---------------------------------------------------------------------
        org.setsId("33333");
        org.setName("Half");
        org.setBirthday(new Date());
        org.setAge(24);
        org.setSex(true);
        org.setHeight(1.82d);
        org.setpId("1234");
        System.out.println("origin1 : " + org.toString());
        // ---------------------------------------------------------------------
        try {
            out = CloneBean.selfClone(org, false);
            System.out.println("simple  : " + out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------------------------------------------
        org.setsId("66666");
        org.setName("Jack");
        org.setBirthday(new Date(System.currentTimeMillis() - 3423423432L));
        org.setAge(61);
        org.setHeight(1.76d);
        org.setpId("9876");
        System.out.println("origin2 : " + org.toString());
        // ---------------------------------------------------------------------
        try {
            out = CloneBean.selfClone(org, true);
            System.out.println("deeply  : " + out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------------------------------------------
        org.setsId("99999");
        org.setName("Toad");
        org.setBirthday(new Date(System.currentTimeMillis() - 52362363624L));
        org.setAge(45);
        org.setSex(false);
        org.setHeight(1.9d);
        org.setpId("5467");
        System.out.println("origin3 : " + org.toString());
    }

    // test CloneBean.newInstance.selfClone / CloneBean.newInstance.deepCopy
    private static void test2() {
        LogUtils.printHeadlineEx("test2: CloneBean.newInstance.selfClone / CloneBean.newInstance.deepCopy", 128);
        DataTypeOfAdvanced org = new DataTypeOfAdvanced(), out1 = null, out2 = null;
        List<String> list = new ArrayList<>();
        list.add("list1");
        list.add("list2");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String[] codes = new String[] {"CODE1", "CODE2"};
        // ---------------------------------------------------------------------
        org.setId("ID123");
        org.setParent("FATHER");
        org.setByteValue((byte) 5);
        org.setCharValue('c');
        org.setShortValue((short) 2);
        org.setIntValue(5);
        org.setLongValue(9l);
        org.setFloatValue(8.8f);
        org.setDoubleValue(3.14);
        org.setBooleanValue(true);
        org.setListValue(list);
        org.setMapValue(map);
        org.setCodes(codes);
        // ---------------------------------------------------------------------
        try {
            out1 = org.selfClone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------------------------------------------
        try {
            out2 = org.deepCopy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------------------------------------------
        org.getCodes()[0] = "ORGN";
        org.getListValue().add("NewLIST");
        org.getMapValue().put("KKKK", "VALUE");

        out1.getCodes()[0] = "SIMPLE";
        out1.getListValue().add("SIMPLE");
        out1.getMapValue().put("OUT1", "SIMPLE");

        out2.getCodes()[0] = "DEEPLY";
        out2.getListValue().add("DEEPLY");
        out2.getMapValue().put("OUT2", "DEEPLY");
        // ---------------------------------------------------------------------
        System.out.println("origin : " + org.info());
        System.out.println("simple : " + out1.info());
        System.out.println("deeply : " + out2.info());
    }

    // test ReflectionUtils
    private static void test3() throws Exception {
        LogUtils.printHeadlineEx("test3: ReflectionUtils", 128);
        BS s = new BS();
        System.out.println("convertToMethodName : " + ReflectionUtils.convertToMethodName(s, "sId", true));
        System.out.println("getDeclaredField : " + ReflectionUtils.getDeclaredField(s, "pId"));
        System.out.println("getDeclaredMethod : " + ReflectionUtils.getDeclaredMethod(s, "getpId"));
        ReflectionUtils.setAttributeValue(s, "height", 1.87);
        System.out.println("invokeMethod : " + ReflectionUtils.invokeMethod(s, "getHeight", new Class[0], new Integer[0]));
        ReflectionUtils.setFieldValue(s, "name", "Jerry");
        System.out.println("getFieldValue : " + ReflectionUtils.getFieldValue(s, "name"));
    }

    // test CloneBean.cloneSameField
    private static void test4() throws Exception {
        LogUtils.printHeadlineEx("test4: CloneBean.cloneSameField", 128);
        BP p = new BP();
        p.setpId("HaH");
        BS s = (BS) CloneBean.cloneSameField(p, BS.class);
        s.setsId("WoW");
        System.out.println(p);
        System.out.println(s);
    }

    private static void test5() {
        LogUtils.printHeadlineEx("test5: ReflectionUtils", 128);
        int i = 2;
        BS bs = new BS();
        System.out.println("BS is a BasicData type?       > " + ReflectionUtils.isBasicDataType(bs));
        System.out.println("int is a BasicData type?      > " + ReflectionUtils.isBasicDataType(i));
        System.out.println("int is a Primitive type?      > " + ReflectionUtils.isPrimitiveType(int.class));
        System.out.println("Integer is a Primitive type?  > " + ReflectionUtils.isPrimitiveType(Integer.class));
    }
}
