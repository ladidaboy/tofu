package cn.hl.kit.ax;

import java.util.Properties;

public class SystemProperty {
    private static boolean even = false;

    private static void pln(String msg, String val) {
        //msg = DataUtility.rightPadEx(msg, 32, ' ');
        msg += "\t " + val;
        if (even) {
            System.out.println("➢ " + msg);
        } else {
            System.out.println("➣ " + msg);
        }
        even = !even;
    }

    public static void main(String[] args) {
        Properties props = System.getProperties(); // 系统属性
        pln("Java的运行环境版本　　　", props.getProperty("java.version"));
        pln("Java的运行环境供应商　　", props.getProperty("java.vendor"));
        pln("Java供应商的URL地址　　", props.getProperty("java.vendor.url"));
        pln("Java的安装路径　　　　　", props.getProperty("java.home"));
        pln("Java的虚拟机规范版本　　", props.getProperty("java.vm.specification.version"));
        pln("Java的虚拟机规范供应商　", props.getProperty("java.vm.specification.vendor"));
        pln("Java的虚拟机规范名称　　", props.getProperty("java.vm.specification.name"));
        pln("Java的虚拟机实现版本　　", props.getProperty("java.vm.version"));
        pln("Java的虚拟机实现供应商　", props.getProperty("java.vm.vendor"));
        pln("Java的虚拟机实现名称　　", props.getProperty("java.vm.name"));
        pln("Java运行时环境规范版本　", props.getProperty("java.specification.version"));
        pln("Java运行时环境规范供应商", props.getProperty("java.specification.vender"));
        pln("Java运行时环境规范名称　", props.getProperty("java.specification.name"));
        pln("Java的类格式版本号　　　", props.getProperty("java.class.version"));
        pln("Java的类路径　　　　　　", props.getProperty("java.class.path"));
        pln("加载库时搜索的路径列表　", props.getProperty("java.library.path"));
        pln("默认的临时文件路径　　　", props.getProperty("java.io.tmpdir"));
        pln("一个或多个扩展目录的路径", props.getProperty("java.ext.dirs"));
        pln("操作系统的名称　　　　　", props.getProperty("os.name"));
        pln("操作系统的构架　　　　　", props.getProperty("os.arch"));
        pln("操作系统的版本　　　　　", props.getProperty("os.version"));
        pln("文件分隔符　　　　　　　", props.getProperty("file.separator"));
        pln("路径分隔符　　　　　　　", props.getProperty("path.separator"));//在unix系统中是":"
        pln("行分隔符　　　　　　　　", "char(" + (int) props.getProperty("line.separator").charAt(0) + ")");//在unix系统中是"/n"
        pln("用户的账户名称　　　　　", props.getProperty("user.name"));
        pln("用户的主目录　　　　　　", props.getProperty("user.home"));
        pln("用户的当前工作目录　　　", props.getProperty("user.dir"));
    }
}
