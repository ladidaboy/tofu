package cn.hl.ox.annotation;

import cn.hl.ax.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author hyman
 * @date 2021-01-05 14:45:58
 */
public class MyClassScanner {
    private static final String S_FILE = "file", S_JAR = "jar", S_CLASS = ".class", CHARSET = "UTF-8";
    private static final char C_DOT = '.', C_SP = '/';

    /**
     * 从指定包中获取Class信息
     *
     * @param packageName 包名称
     */
    public static List<Class> scan(String packageName) throws IOException {
        return scan(packageName, true);
    }

    /**
     * 从指定包中获取Class信息
     *
     * @param packageName 包名称
     * @param recursive   循环迭代
     */
    public static List<Class> scan(String packageName, boolean recursive) throws IOException {
        return scan(packageName, recursive, null);
    }

    /**
     * 从指定包中获取Class信息
     *
     * @param packageName 包名称
     * @param recursive   循环迭代
     * @param filter      用户断言(过滤Class)
     */
    public static List<Class> scan(String packageName, boolean recursive, Predicate<Class> filter) throws IOException {
        List<Class> hits = new ArrayList<>();
        String packagePath = packageName.replace(C_DOT, C_SP);
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packagePath);
        while (dirs.hasMoreElements()) {
            URL packageUrl = dirs.nextElement();
            String protocol = packageUrl.getProtocol();
            if (S_FILE.equals(protocol)) {
                String filepath = URLDecoder.decode(packageUrl.getFile(), CHARSET);
                findClassesInPackage(hits, packageName, new File(filepath), recursive, filter);
            } else if (S_JAR.equals(protocol)) {
                findClassesInJar(hits, packagePath, packageUrl, recursive, filter);
            }
        }

        return hits;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param hits        命中的Java类列表
     * @param packageName 包名称
     * @param packageRoot 包路径
     * @param recursive   循环迭代
     * @param filter      用户断言(过滤Class)
     */
    private static void findClassesInPackage(List<Class> hits, String packageName, File packageRoot, boolean recursive,
                                             Predicate<Class> filter) {
        if (!packageRoot.exists() || !packageRoot.isDirectory()) {
            System.err.println("There are no files in the package{" + packageName + "}!");
            return;
        }
        // 自定义过滤规则: 1# 如果可以循环(包含子目录);  2# 以.class结尾的文件(编译好的java类文件);
        File[] javaFiles = packageRoot.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(S_CLASS)));
        if (javaFiles == null || javaFiles.length == 0) {
            System.err.println("There are no valid files in the package{" + packageName + "}!");
            return;
        }
        //
        for (File file : javaFiles) {
            // 如果是目录,则继续扫描
            String nextClass = packageName + C_DOT + file.getName();
            if (file.isDirectory()) {
                findClassesInPackage(hits, nextClass, file, recursive, filter);
            } else {
                nextClass = nextClass.substring(0, nextClass.length() - 6);
                addAndFilterClass(hits, nextClass, filter);
            }
        }
    }

    /**
     * 以文件的形式来获取Jar下的所有Class
     *
     * @param hits        命中的Java类列表
     * @param packagePath 包地址
     * @param jarUrl      包地址
     * @param recursive   循环迭代
     * @param filter      用户断言(过滤Class)
     */
    private static void findClassesInJar(List<Class> hits, String packagePath, URL jarUrl, boolean recursive, Predicate<Class> filter)
            throws IOException {
        int packageLevel = getDirLevel(packagePath), fileLevel;
        JarFile jarFile = ((JarURLConnection) jarUrl.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体,可以是目录和一些jar包里的其他文件(如META-INF等文件)
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            name = name.charAt(0) == C_SP ? name.substring(1) : name;
            fileLevel = getDirLevel(name);
            boolean packageMatched = name.startsWith(packagePath);
            boolean subDirMatched = fileLevel > packageLevel;
            boolean recursiveMatched = !subDirMatched || recursive;
            boolean classMatched = name.endsWith(S_CLASS);
            if (packageMatched && recursiveMatched && classMatched) {
                // 去掉后面的".class" 获取真正的类名
                String classname = name.substring(0, name.length() - 6).replace(C_SP, C_DOT);
                addAndFilterClass(hits, classname, filter);
            }
        }
    }

    private static void addAndFilterClass(List<Class> hits, String classname, Predicate<Class> filter) {
        try {
            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(classname);
            if (clazz.isInterface()) {
                return;
            }
            if (filter == null || filter.test(clazz)) {
                hits.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Load class{" + classname + "} error!");
        }
    }

    private static int getDirLevel(String path) {
        String[] ss = path.split("[" + C_SP + "]");
        int level = ss.length;
        boolean noFix = path.endsWith(String.valueOf(C_SP));
        noFix |= !ss[level - 1].contains(String.valueOf(C_DOT));
        return level - (noFix ? 0 : 1);
    }

    public static void main(String[] args) throws IOException {
        List<Class> hits1 = scan("cn.hl.ox.annotation", true, c -> c.getName().contains("Test"));
        hits1.forEach(System.out::println);

        LogUtils.printSplitLine(64);

        List<Class> hits2 = scan("cn.hutool.cache", true);
        hits2.forEach(System.out::println);
    }
}
