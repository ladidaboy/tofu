package cn.hl.ax.snmp;

import cn.hl.ax.log.LogUtils;
import cn.hl.ax.snmp.mib.Mib;
import cn.hl.ax.snmp.mib.Mib2Library;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hyman
 * @date 2019-08-01 15:10:16
 */
public class MySnmpUtils {
    /**
     * 检查对象是否为空
     *
     * @param obj 待测试对象
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(Object obj) {
        return obj == null;
    }

    /**
     * 检查文本是否为空
     *
     * @param txt 待测试文本
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(String txt) {
        return txt == null || "".equals(txt.trim());
    }

    /**
     * 检查数组是否为空
     *
     * @param arr 待测试数组
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * String左对齐
     */
    private static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] chars = new char[len];
        System.arraycopy(src.toCharArray(), 0, chars, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * String右对齐
     */
    private static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] chars = new char[len];
        System.arraycopy(src.toCharArray(), 0, chars, diff, src.length());
        for (int i = 0; i < diff; i++) {
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * 打印TableEvent对象
     *
     * @param events TableEvents
     */
    private static void printTableEvents(List<TableEvent> events, OID[] oids) {
        if (!checkEmpty(events)) {
            LogUtils.printLine("----------------========>>>> Print [ Table Events ] <<<<========----------------");
            int rows = events.size(), cols = oids.length;
            String[][] data = new String[rows + 1][cols];
            int[] colLenMax = new int[cols];
            // 表头
            for (int c = 0; c < cols; c++) {
                Mib mib = Mib2Library.getMib(oids[c]);
                if (mib == null) {
                    data[0][c] = oids[c].format();
                } else {
                    data[0][c] = mib.getName();// + "(" + mib.getDataType() + ")";
                }
                colLenMax[c] = Math.max(colLenMax[c], data[0][c].length());
            }
            // 计算
            for (int r = 0; r < rows; r++) {
                VariableBinding[] vbs = events.get(r).getColumns();
                for (int c = 0; c < vbs.length; c++) {
                    if (vbs[c] != null) {
                        String[] names = Mib2Library.fmtVB(vbs[c]);
                        data[r + 1][c] = names[2];
                        if (names[2] != null) {
                            colLenMax[c] = Math.max(colLenMax[c], names[2].length());
                        }
                    } else {
                        data[r + 1][c] = "";
                    }
                }
            }
            // 打印
            String line = "+", template = "|";
            for (int len : colLenMax) {
                line = padLeft(line, line.length() + len + 2, '-') + "+";
                template += " \033[34;0m%" + len + "s\033[0m |";
            }
            for (int r = 0; r <= rows; r++) {
                if (r < 2) {
                    System.out.println(line);
                }
                System.out.println(String.format(template, data[r]));
            }
            LogUtils.printCornerTitleEx("Found " + rows + " TableEvents", line.length());
        }
    }

    /**
     * 打印ResponseEvent对象
     *
     * @param response PDU.Response
     */
    private static void printPduResponse(PDU response) {
        if (response != null) {
            LogUtils.printLine("----------------========>>>> Print { PDU.Response } <<<<========----------------");
            for (VariableBinding vb : response.getVariableBindings()) {
                LogUtils.printLabel(Mib2Library.formatVB(vb));
            }
        }
    }

    private static OID[] translateMib(Mib[] mibs) {
        OID[] oids = new OID[mibs.length];
        int index = 0;
        for (Mib mib : mibs) {
            oids[index++] = mib.getOID();
        }
        return oids;
    }

    /**
     * 翻译OID
     *
     * @param columns OIDs
     */
    private static String translateOid(OID... columns) {
        return "[" + Arrays.stream(columns).map(oid -> {
            Mib mib = Mib2Library.getMib(oid);
            String name = oid.toString();
            if (mib != null) {
                name = name.replace(mib.getOidText(), mib.getName());
            }
            return "'" + name + "'";
        }).collect(Collectors.joining(",")) + "]";
    }

    /**
     * 打印错误日志
     *
     * @param template 模版
     * @param messages 参数
     */
    private static void printError(String template, Object... messages) {
        Exception exp = null;
        if (!checkEmpty(messages)) {
            int size = messages.length;
            if (messages[size - 1] instanceof Exception) {
                exp = (Exception) messages[size - 1];
            }
            template = String.format(template, messages);
        }
        LogUtils.printError(template, exp);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 采集目标设备的OID信息
     *
     * @param ip        网络地址
     * @param port      地址端口
     * @param community 共同体
     * @param columns   OID信息
     * @return TableEvent信息
     */
    public static List<TableEvent> collect(String ip, Integer port, String community, OID[] columns, Object... debug) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(columns)) {
            return null;
        }

        String cols = translateOid(columns);
        List<TableEvent> list;
        TransportMapping transport = null;
        Snmp snmp = null;
        try {
            //>> 设定采取的协议，设定传输协议为UDP
            transport = new DefaultUdpTransportMapping();
            //>> 创建SNMP对象，用于发送请求PDU
            snmp = new Snmp(transport);
            snmp.listen();

            //>> 定义远程主机的地址
            String address = "udp:" + ip + "/" + port;
            //>> 设定CommunityTarget
            CommunityTarget target = new CommunityTarget();
            //设定远程主机的地址
            target.setAddress(GenericAddress.parse(address));
            //设置snmp共同体
            target.setCommunity(new OctetString(community));
            //设置使用的snmp版本
            target.setVersion(SnmpConstants.version2c);
            //设置超时的时间(millisecond)
            target.setTimeout(5000);
            //设置超时重试次数
            target.setRetries(2);

            PDUFactory factory = new DefaultPDUFactory(PDU.GETBULK);
            TableUtils tableUtils = new TableUtils(snmp, factory);
            list = tableUtils.getTable(target, columns, null, null);
            if (list.size() == 0 || (list.size() == 1 && list.get(0).getColumns() == null)) {
                TableEvent ee = list.size() == 0 ? null : list.get(0);
                String result = "Data not found";
                if (ee != null) {
                    result = ee.getErrorMessage() + "(Status:" + ee.getStatus() + ")";
                }
                throw new Exception(result);
            }
        } catch (Exception e) {
            list = null;
            printError("MySnmpUtils.collect('%s', %d, '%s', %s)", ip, port, community, cols, e);
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException ee) {
                printError("MySnmpUtils.collect('%s', %d, '%s', %s) [transport/snmp].close", ip, port, community, cols, ee);
            }
        }

        if (debug.length > 0) {
            printTableEvents(list, columns);
        }
        return list;
    }

    public static List<TableEvent> collect(String ip, Integer port, String community, Mib[] columns, Object... debug) {
        return collect(ip, port, community, translateMib(columns), debug);
    }

    /**
     * 采集目标设备的OID信息
     *
     * @param ip        网络地址
     * @param port      地址端口
     * @param community 共同体
     * @param columns   OID信息
     * @return response信息
     */
    public static PDU snmpGet(String ip, Integer port, String community, OID[] columns, Object... debug) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(columns)) {
            return null;
        }

        String cols = translateOid(columns);
        TransportMapping transport = null;
        PDU response = null;
        try {
            //>> 设定CommunityTarget
            CommunityTarget myTarget = new CommunityTarget();

            //>> 定义远程主机的地址
            UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(ip), port);
            //设定远程主机的地址
            myTarget.setAddress(udpAddress);
            //设置snmp共同体
            myTarget.setCommunity(new OctetString(community));
            //设置使用的snmp版本
            myTarget.setVersion(SnmpConstants.version2c);
            //设置超时的时间
            myTarget.setTimeout(5 * 1000);
            //设置超时重试次数
            myTarget.setRetries(2);

            //>> 设定采取的协议，设定传输协议为UDP
            transport = new DefaultUdpTransportMapping();
            //>> 调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
            transport.listen();
            //>> 创建SNMP对象，用于发送请求PDU
            Snmp protocol = new Snmp(transport);

            //>> 创建请求pdu,获取mib
            PDU request = new PDU();
            //>> 调用的add方法绑定要查询的OID
            for (OID oid : columns) {
                request.add(new VariableBinding(oid));
            }
            //>> 调用setType()方法来确定该pdu的类型
            request.setType(PDU.GETNEXT);
            //>> 调用 send(PDU pdu,Target target)发送pdu，返回一个ResponseEvent对象
            ResponseEvent responseEvent = protocol.send(request, myTarget);
            //>> 通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
            response = responseEvent.getResponse();

            if (!checkEmpty(debug)) {
                printPduResponse(response);
            }
        } catch (IOException e) {
            printError("MySnmpUtils.snmpGet('%s', %d, '%s', %s)", ip, port, community, cols, e);
        } finally {
            //调用close()方法释放该进程
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException ee) {
                    printError("MySnmpUtils.snmpGet('%s', %d, '%s', %s) transport.close", ip, port, community, cols, ee);
                }
            }
        }
        return response;
    }

    public static PDU snmpGet(String ip, Integer port, String community, Mib[] columns, Object... debug) {
        return snmpGet(ip, port, community, translateMib(columns), debug);
    }

    /**
     * 设置目标设备的OID信息
     *
     * @param ip        网络地址
     * @param port      地址端口
     * @param community 共同体
     * @param key       OID信息
     * @param val       OID数值
     * @return response信息
     */
    public static boolean snmpSet(String ip, Integer port, String community, OID key, String val, Object... debug) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(key)) {
            return false;
        }

        String cols = translateOid(key);
        TransportMapping transport = null;
        try {
            CommunityTarget target = new CommunityTarget();
            UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(ip), port);
            target.setCommunity(new OctetString(community));
            target.setVersion(SnmpConstants.version2c);
            target.setAddress(udpAddress);
            target.setTimeout(2 * 1000);
            target.setRetries(2);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(key, new OctetString(val)));
            pdu.setType(PDU.SET);

            transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            snmp.listen();

            ResponseEvent event = snmp.send(pdu, target);
            PDU response = event.getResponse();
            if (checkEmpty(response)) {
                throw new Exception("Not found response");
            }
            if (!checkEmpty(debug)) {
                String result = response.getErrorStatusText() + "(Status:" + response.getErrorStatus() + ")";
                printError("MySnmpUtils.snmpSet('%s', %d, '%s', %s, '%s') -> %s", ip, port, community, cols, val, result);
            }
            return response.getErrorStatus() == 0;
        } catch (Exception e) {
            printError("MySnmpUtils.snmpSet('%s', %d, '%s', %s, '%s')", ip, port, community, cols, val, e);
            return false;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException ee) {
                    printError("MySnmpUtils.snmpSet('%s', %d, '%s', %s, '%s') transport.close", ip, port, community, cols, val, ee);
                }
            }
        }
    }

    public static boolean snmpSet(String ip, Integer port, String community, Mib key, String val, Object... debug) {
        return snmpSet(ip, port, community, key.getOID(), val, debug);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 获取CPU利用率
     */
    public static List<TableEvent> collectCPU(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取CPU利用率");
        return collect(ip, port, community, new OID[] {new OID("1.3.6.1.2.1.25.3.3.1.2")}, true);
    }

    /**
     * 获取存储信息
     */
    public static List<TableEvent> collectMemoryAndDisk(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取存储信息");
        OID[] oids = {new OID("1.3.6.1.2.1.25.2.3.1.2"), new OID("1.3.6.1.2.1.25.2.3.1.3"), new OID("1.3.6.1.2.1.25.2.3.1.4"), new OID(
                "1.3.6.1.2.1.25.2.3.1.5"), new OID("1.3.6.1.2.1.25.2.3.1.6"),};
        return collect(ip, port, community, oids, true);
    }

    /**
     * 获取服务器进程集合
     */
    public static List<TableEvent> collectProcess(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取服务器进程集合");
        OID[] oids = {new OID("1.3.6.1.2.1.25.4.2.1.1"), new OID("1.3.6.1.2.1.25.4.2.1.2"), new OID("1.3.6.1.2.1.25.4.2.1.4"), new OID(
                "1.3.6.1.2.1.25.4.2.1.6"), new OID("1.3.6.1.2.1.25.5.1.1.1"), new OID("1.3.6.1.2.1.25.5.1.1.2"),};
        return collect(ip, port, community, oids, true);
    }

    /**
     * 获取服务器系统服务集合
     */
    public static List<TableEvent> collectService(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取服务器系统服务集合");
        OID[] oids = {new OID("1.3.6.1.4.1.77.1.2.3.1.1")};
        //中文乱码，需要转为utf-8编码
        return collect(ip, port, community, oids, true);
    }

    /**
     * 获取服务器接口集合
     */
    public static List<TableEvent> collectInterface(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取服务器接口集合");
        OID[] ifOids = {new OID("1.3.6.1.2.1.2.2.1.1"), new OID("1.3.6.1.2.1.2.2.1.2"), new OID("1.3.6.1.2.1.2.2.1.3"), new OID(
                "1.3.6.1.2.1.2.2.1.5"), new OID("1.3.6.1.2.1.2.2.1.6"), new OID("1.3.6.1.2.1.2.2.1.7"), new OID("1.3.6.1.2.1.2.2.1.8"),
                new OID("1.3.6.1.2.1.2.2.1.10"), new OID("1.3.6.1.2.1.2.2.1.16"),};
        List<TableEvent> ifList = collect(ip, port, community, ifOids, true);
        if (ifList == null) {
            ifList = new ArrayList<>();
        }

        OID[] ipOids = {new OID("1.3.6.1.2.1.4.20.1.1"), new OID("1.3.6.1.2.1.4.20.1.2"), new OID("1.3.6.1.2.1.4.20.1.3"),};
        List<TableEvent> ipList = collect(ip, port, community, ipOids, true);
        if (!checkEmpty(ipList)) {
            ifList.addAll(ipList);
        }

        return ifList;
    }

    /**
     * 获取服务器端口集合
     */
    public static List<TableEvent> collectPort(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取服务器端口集合");
        OID[] tcpOids = {new OID("1.3.6.1.2.1.6.13.1.3"), new OID("1.3.6.1.2.1.6.13.1.1")};
        List<TableEvent> tcpList = collect(ip, port, community, tcpOids, true);
        if (tcpList == null) {
            tcpList = new ArrayList<>();
        }

        OID[] udpOids = {new OID("1.3.6.1.2.1.7.5.1.2")};
        List<TableEvent> udpList = collect(ip, port, community, udpOids, true);
        if (!checkEmpty(udpList)) {
            tcpList.addAll(udpList);
        }

        return tcpList;
    }

    /**
     * 获取服务器安装软件集合
     */
    public static List<TableEvent> collectSoft(String ip, int port, String community) {
        LogUtils.printLabel("[DEMO] 获取服务器安装软件集合");
        OID[] oids = {new OID("1.3.6.1.2.1.25.6.3.1.2"), new OID("1.3.6.1.2.1.25.6.3.1.4"), new OID("1.3.6.1.2.1.25.6.3.1.5")};
        return collect(ip, port, community, oids, true);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        // 107.155.19.82  192.254.81.147  C#Edge123
        String ip = "104.166.168.3";
        int port = 161;
        String community = "C#EDGE123";

        //collectCPU(ip, port, community);
        //collectMemoryAndDisk(ip, port, community);
        //collectProcess(ip, port, community);
        //collectService(ip, port, community);
        //collectInterface(ip, port, community);
        //collectPort(ip, port, community);
        //collectSoft(ip, port, community);

        snmpSet(ip, port, community, Mib2Library.SysName, "Test.System.Name", true);
        snmpGet(ip, port, community, new Mib[] {Mib2Library.SysName, Mib2Library.SysDescr}, true);

        Mib[] mibs = {
                /*!*/Mib2Library.IfIndex,
                /*!*/Mib2Library.IfName,
                /*!*/Mib2Library.IfAlias,
                /*!*/Mib2Library.IfAdminStatus,
                /*!*/Mib2Library.IfOperStatus,
                /*!*/Mib2Library.IfHCInOctets,
                /*!*/Mib2Library.IfHCOutOctets,
                ///*!*/Mib2Library.IpAdEntAddr,
                ///*!*/Mib2Library.IpAdEntNetMask,
                ///*!*/Mib2Library.IpRouteDest,
                ///*!*/Mib2Library.IpRouteIfIndex,
        };
        collect(ip, port, community, mibs, true);
    }
}
