package cn.hl.ox;

import cn.hl.ax.data.DataUtils;

public class BuddhaBless {
    public static void printSplitLine() {
        printSplitLine(0);
    }

    public static void printSplitLine(int len) {
        len = Math.max(len, 64) / 2 - 12;
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
        sb.append("\r\n:..............................................");
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
    }
}
