package cn.hl.ox.data;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author hyman
 * @date 2019-08-26 16:34:04
 */
public class Tester4SystemUtils {
    /*
    1.获取Java Home目录，返回File
    SystemUtils.getJavaHome()
    2.获取Java IO临时目录文件，返回File
    SystemUtils.getJavaIoTmpDir()
    3.获取Java版本，返回Float
    SystemUtils.getJavaVersion()
    相对应的还有getJavaVersionAsFloat()、getJavaVersionAsInt()
    4.获取用户目录，返回File
    SystemUtils.getUserDir()
    5.获取获取用户主目录，返回File
    SystemUtils.getUserHome()
    6.判断当前Java版本是否满足要求，返回boolean。
    SystemUtils.isJavaVersionAtLeast(float|int requiredVersion)
     */

    public static void main(String[] args) {
        System.out.println(SystemUtils.IS_OS_MAC);
        System.out.println(SystemUtils.getJavaHome());
        System.out.println(SystemUtils.getJavaIoTmpDir());
        System.out.println(SystemUtils.getUserDir());
        System.out.println(SystemUtils.getUserHome());
    }
}
