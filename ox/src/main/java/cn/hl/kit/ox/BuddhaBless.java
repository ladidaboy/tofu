package cn.hl.kit.ox;

import cn.hl.kit.ax.data.DataUtils;

public class BuddhaBless {
    private static final int MIN_LEN = 48;

    public static void printHeadline(String tag, Object... msg) {
        printHeadlineEx(tag, 0, msg);
    }

    public static void printHeadlineEx(String tag, int len, Object... msg) {
        tag = tag == null ? "^" : tag.trim();
        tag = " " + tag + " ";
        len = Math.max(len, MIN_LEN) - 16;
        int lmin = tag.length(), lmax = (int) (Math.ceil(lmin / 2.0) * 2), smax = Math.max(len, lmax);
        tag = DataUtils.leftPad(tag, lmax + (smax - lmax) / 2, "/");
        tag = DataUtils.rightPad(tag, smax, "\\");
        tag = "____////" + tag + "\\\\\\\\____";
        try {
            Thread.sleep(5);
            System.err.println(tag);
            Thread.sleep(5);
            if (msg.length > 0) {
                for (Object mm : msg) {
                    System.out.println(mm);
                }
            }
            Thread.sleep(5);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void printCornerTitle(String tag, Object... msg) {
        printCornerTitleEx(tag, 0, msg);
    }

    public static void printCornerTitleEx(String tag, int len, Object... msg) {
        tag = tag == null ? "!" : tag.trim();
        len = Math.max(len, MIN_LEN);
        int lmin = tag.length(), lmax = Math.max(lmin + 18, len);
        tag = DataUtils.rightPad("\\" + tag + "/", lmin + 10, "^");
        tag = DataUtils.leftPad(tag, lmax, "^");
        try {
            Thread.sleep(5);
            if (msg.length > 0) {
                for (Object mm : msg) {
                    System.out.println(mm);
                }
            }
            Thread.sleep(5);
            System.err.println(tag);
            Thread.sleep(5);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void printSplitLine() {
        printSplitLine(0);
    }

    public static void printSplitLine(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = DataUtils.rightPadEx("", len, '-');
        String ll = "( S P L I T ~ L I N E )-";
        //ll = "( Ⓢ ⓟ Ⓛ Ⓘ Ⓣ - Ⓛ Ⓘ Ⓝ Ⓔ )-";
        //ll = "( ⓢ Ⓟ ⓛ ⓘ ⓣ - ⓛ ⓘ ⓝ ⓔ )-";
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            ll = "( E R R O R ~ L I N E )-";
            //ll = "( Ⓔ Ⓡ Ⓡ Ⓞ Ⓡ - Ⓛ Ⓘ Ⓝ Ⓔ )-";
            //ll = "( ⓔ ⓡ ⓡ ⓞ ⓡ - ⓛ ⓘ ⓝ ⓔ )-";
        }
        System.err.println(ss + ll + ss);
    }

    public static void printSplitWave() {
        printSplitWave(0);
    }

    public static void printSplitWave(int len) {
        len = Math.max(len, MIN_LEN) / 2 - 12;
        String ss = DataUtils.rightPadEx("", len, '~');
        String ll = "( S P L I T - L I N E )~";
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            ll = "( E R R O R - L I N E )~";
        }
        System.err.println(ss + ll + ss);
    }

    public static void printLine(int len) {
        System.out.println(DataUtils.rightPad("", len, '='));
    }

    public static void printWave(int len) {
        System.out.println(DataUtils.rightPad("", len, '~'));
    }

    public static void pray2theBuddha() {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n/^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\");
        sb.append("\r\n|                                             |");
        sb.append("\r\n|                   _ooOoo_                   |");
        sb.append("\r\n|                  o8888888o                  |");
        sb.append("\r\n|                  88\" . \"88                  |");
        sb.append("\r\n|                  (| -_- |)                  |");
        sb.append("\r\n|                  O\\  =  /O                  |");
        sb.append("\r\n|               ____/`---'\\____               |");
        sb.append("\r\n|             .'  \\\\|     |//  `.             |");
        sb.append("\r\n|            /  \\\\|||  :  |||//  \\            |");
        sb.append("\r\n|           /  _||||| -:- |||||-  \\           |");
        sb.append("\r\n|           |   | \\\\\\  -  /// |   |           |");
        sb.append("\r\n|           | \\_|  ''\\---/''  |   |           |");
        sb.append("\r\n|           \\  .-\\__  `-`  ___/-. /           |");
        sb.append("\r\n|         ___`. .'  /--.--\\  `. . __          |");
        sb.append("\r\n|      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".       |");
        sb.append("\r\n|     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |     |");
        sb.append("\r\n|     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /     |");
        sb.append("\r\n|======`-.____`-.___\\_____/___.-`____.-'======|");
        sb.append("\r\n|                   `=---='                   |");
        sb.append("\r\n|---------------------------------------------|");
        sb.append("\r\n||===>>>>  Buddha bless, Never BUG!   <<<<===||");
        sb.append("\r\n+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+");
        sb.append("\r\n: -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
        sb.append("\r\n+ 佛曰:");
        sb.append("\r\n: 　　写字楼里写字间，写字间里程序员；");
        sb.append("\r\n: 　　程序人员写程序，又拿程序换酒钱。");
        sb.append("\r\n: 　　酒醒只在网上坐，酒醉还来网下眠；");
        sb.append("\r\n: 　　酒醉酒醒日复日，网上网下年复年。");
        sb.append("\r\n: 　　但愿老死电脑间，不愿鞠躬老板前；");
        sb.append("\r\n: 　　奔驰宝马贵者趣，公交自行程序员。");
        sb.append("\r\n: 　　别人笑我忒疯癫，我笑自己命太贱；");
        sb.append("\r\n: 　　不见满街漂亮妹，哪个归得程序员？");
        sb.append("\r\n:");
        System.out.println(sb);

    }

    public static void main(String[] args) {
        pray2theBuddha();
        printHeadline("HEADLINE", "Used to display customer information.");
        System.out.println(".\n.");
        printSplitLine();
        System.out.println(".\n.");
        printSplitWave();
        System.out.println(".\n.");
        printCornerTitle("CornerTitle", "Something interesting ^.^");
    }
}
