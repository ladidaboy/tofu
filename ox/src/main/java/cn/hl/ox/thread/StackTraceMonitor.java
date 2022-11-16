package cn.hl.ox.thread;

import cn.hl.ax.snmp.MySnmpUtils;

import java.util.Map;

/**
 * @author hyman
 * @date 2020-03-19 15:51:49
 */
public class StackTraceMonitor {
    public static void main(String[] args) {
        String ip = "128.14.158.127";
        int port = 161;
        String community = "C#EDGE123";
        MySnmpUtils.collectInterface(ip, port, community);



        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            for (StackTraceElement ste : entry.getValue()) {
                System.out.println(ste);
            }
        }
    }
}
