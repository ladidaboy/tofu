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
import java.util.List;

/**
 * @author hyman
 * @date 2019-08-01 15:10:16
 * @version $ Id: SNMPUtils.java, v 0.1  hyman Exp $
 */
public class MySnmpUtils {
    /**
     * 检查对象是否为空
     * @param obj 待测试对象
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(Object obj) {
        return obj == null;
    }

    /**
     * 检查文本是否为空
     * @param txt 待测试文本
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(String txt) {
        return txt == null || txt.trim().equals("");
    }

    /**
     * 检查数组是否为空
     * @param arr 待测试数组
     * @return true:空, false:非空
     */
    private static boolean checkEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * 将16进制的时间转换成标准的时间格式
     * @param hexString 16进制时间
     * @return 标准时间
     */
    private static String hexToDateTime(String hexString) {
        if (checkEmpty(hexString)) {
            return "";
        }
        String dateTime = "";
        try {
            byte[] values = OctetString.fromHexString(hexString).getValue();
            int year, month, day, hour, minute;

            year = values[0] * 256 + 256 + values[1];
            month = values[2];
            day = values[3];
            hour = values[4];
            minute = values[5];

            char[] formatStr = new char[22];
            int index = 3;
            int temp = year;
            for (; index >= 0; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[4] = '-';
            index = 6;
            temp = month;
            for (; index >= 5; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[7] = '-';
            index = 9;
            temp = day;
            for (; index >= 8; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[10] = ' ';
            index = 12;
            temp = hour;
            for (; index >= 11; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[13] = ':';
            index = 15;
            temp = minute;
            for (; index >= 14; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            dateTime = new String(formatStr, 0, formatStr.length).substring(0, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * String左对齐
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * String右对齐
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * 打印TableEvent对象
     * @param events TableEvents
     */
    public static void printTableEvents(List<TableEvent> events, OID[] oids) {
        if (!checkEmpty(events)) {
            LogUtils.printLabel("--------========>>>> Print TableEvent`s <<<<========--------");
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
                template += " %" + len + "s |";
            }
            LogUtils.printLabel(line);
            LogUtils.printLabel(String.format(template, data[0]));
            LogUtils.printLabel(line);
            for (int r = 1; r < rows; r++) {
                LogUtils.printLabel(String.format(template, data[r]));
            }
            LogUtils.printLabel(line);
        }
    }

    /**
     * 打印ResponseEvent对象
     * @param response PDU.Response
     */
    public static void printPduResponse(PDU response) {
        if (response != null) {
            LogUtils.printLabel("--------========>>>> Print PDU.Response <<<<========--------");
            for (VariableBinding vb : response.getVariableBindings()) {
                LogUtils.printLabel(Mib2Library.formatVB(vb));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 采集目标设备的OID信息
     * @param ip 网络地址
     * @param port 地址端口
     * @param community 共同体
     * @param columns OID信息
     * @return TableEvent信息
     */
    public static List<TableEvent> collect(String ip, Integer port, String community, OID[] columns) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(columns)) {
            return null;
        }

        List<TableEvent> list = null;
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
            target.setAddress(GenericAddress.parse(address)); //设定远程主机的地址
            target.setCommunity(new OctetString(community)); //设置snmp共同体
            target.setVersion(SnmpConstants.version2c); //设置使用的snmp版本
            target.setTimeout(5000); //设置超时的时间(millisecond)
            target.setRetries(2); //设置超时重试次数

            PDUFactory factory = new DefaultPDUFactory(PDU.GETBULK);
            TableUtils tableUtils = new TableUtils(snmp, factory);
            list = tableUtils.getTable(target, columns, null, null);
            if (list.size() == 0 || (list.size() == 1 && list.get(0).getColumns() == null)) {
                TableEvent ee = list.size() == 0 ? null : list.get(0);
                String result = "NULL";
                String oids = "[";
                for (OID oid : columns) {
                    Mib mib = Mib2Library.getMib(oid);
                    String name = oid.toString();
                    if (mib != null) {
                        name = name.replace(mib.getOidText(), mib.getName());
                    }
                    oids += "'" + name + "', ";
                }
                oids = oids.substring(0, oids.length() - 2) + "]";
                if (ee != null) {
                    result = ee.getErrorMessage() + "(Status:" + ee.getStatus() + ")";
                }
                String msg = String.format("SnmpUtils.collect('%s', %d, '%s', %s) -> %s", ip, port, community, oids, result);
                System.err.println(msg);
                list = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        printTableEvents(list, columns);
        return list;
    }

    /**
     * 采集目标设备的OID信息
     * @param ip 网络地址
     * @param port 地址端口
     * @param community 共同体
     * @param columns OID信息
     * @return response信息
     */
    public static PDU snmpGet(String ip, Integer port, String community, OID[] columns) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(columns)) {
            return null;
        }

        TransportMapping transport = null;
        PDU response = null;
        try {
            //>> 设定CommunityTarget
            CommunityTarget myTarget = new CommunityTarget();

            //>> 定义远程主机的地址
            UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(ip), port);
            myTarget.setAddress(udpAddress); //设定远程主机的地址
            myTarget.setCommunity(new OctetString(community)); //设置snmp共同体
            myTarget.setVersion(SnmpConstants.version2c); //设置使用的snmp版本
            myTarget.setTimeout(5 * 1000); //设置超时的时间
            myTarget.setRetries(2); //设置超时重试次数

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //调用close()方法释放该进程
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException e) {
                    transport = null;
                }
            }
        }
        return response;
    }

    /**
     * 设置目标设备的OID信息
     * @param ip 网络地址
     * @param port 地址端口
     * @param community 共同体
     * @param key OID信息
     * @param val OID数值
     * @return response信息
     */
    public static boolean snmpSet(String ip, Integer port, String community, OID key, String val) {
        if (checkEmpty(ip) || checkEmpty(port) || checkEmpty(community) || checkEmpty(key)) {
            return false;
        }

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
            LogUtils.printLabel("SnmpSet Result: " + response.getErrorStatusText() + "(" + response.getErrorStatus() + ")");
            return response.getErrorStatus() == 0;
        } catch (Exception ex) {
            LogUtils.printError("SNMP Get Error!", ex);
            return false;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException iox) {
                    transport = null;
                }
            }
        }
    }
    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 获取CPU利用率方法
     */
    public static List<TableEvent> collectCPU(String ip, int port, String community) {
        List<TableEvent> list = collect(ip, port, community, new OID[] {new OID("1.3.6.1.2.1.25.3.3.1.2")});
        /*if (!checkEmpty(list)) {
            int percentage = 0;
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values != null) {
                    percentage += Integer.parseInt(values[0].getVariable().toString());
                }
            }
            LogUtils.printLabel("CPU利用率为：" + percentage / list.size() + "%");
        }*/
        return list;
    }

    /**
     * 获取内存信息方法
     */
    public static List<TableEvent> collectMemory(String ip, int port, String community) {
        OID[] oids = {//
                new OID("1.3.6.1.2.1.25.2.3.1.2"), //type 存储单元类型
                new OID("1.3.6.1.2.1.25.2.3.1.3"), //descr
                new OID("1.3.6.1.2.1.25.2.3.1.4"), //unit 存储单元大小
                new OID("1.3.6.1.2.1.25.2.3.1.5"), //size 总存储单元数
                new OID("1.3.6.1.2.1.25.2.3.1.6"), //used 使用存储单元数
        };
        String PHYSICAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.2";//物理存储
        String VIRTUAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.3"; //虚拟存储
        List<TableEvent> list = collect(ip, port, community, oids);
        /*if (!checkEmpty(list)) {
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                int unit = Integer.parseInt(values[2].getVariable().toString()); //unit 存储单元大小
                int totalSize = Integer.parseInt(values[3].getVariable().toString()); //size 总存储单元数
                int usedSize = Integer.parseInt(values[4].getVariable().toString()); //used  使用存储单元数
                String oid = values[0].getVariable().toString();
                if (PHYSICAL_MEMORY_OID.equals(oid)) {
                    LogUtils.printLabel("物理内存大小(PHYSICAL_MEMORY)：" + (long) totalSize * unit / (1024 * 1024 * 1024) + "G \t内存使用率为：" + (long) usedSize * 100 / totalSize + "%");
                } else if (VIRTUAL_MEMORY_OID.equals(oid)) {
                    LogUtils.printLabel("虚拟内存大小(VIRTUAL_MEMORY) ：" + (long) totalSize * unit / (1024 * 1024 * 1024) + "G \t内存使用率为：" + (long) usedSize * 100 / totalSize + "%");
                }
            }
        }*/
        return list;
    }

    /**
     * 获取磁盘相关信息
     */
    public static List<TableEvent> collectDisk(String ip, int port, String community) {
        String DISK_OID = "1.3.6.1.2.1.25.2.1.4";
        OID[] oids = {//
                new OID("1.3.6.1.2.1.25.2.3.1.2"), //type 存储单元类型
                new OID("1.3.6.1.2.1.25.2.3.1.3"), //descr
                new OID("1.3.6.1.2.1.25.2.3.1.4"), //unit 存储单元大小
                new OID("1.3.6.1.2.1.25.2.3.1.5"), //size 总存储单元数
                new OID("1.3.6.1.2.1.25.2.3.1.6"), //used 使用存储单元数
        };
        List<TableEvent> list = collect(ip, port, community, oids);
        /*if (!checkEmpty(list)) {
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values == null || !DISK_OID.equals(values[0].getVariable().toString())) {
                    continue;
                }
                int unit = Integer.parseInt(values[2].getVariable().toString()); //unit 存储单元大小
                int totalSize = Integer.parseInt(values[3].getVariable().toString()); //size 总存储单元数
                int usedSize = Integer.parseInt(values[4].getVariable().toString()); //used  使用存储单元数
                LogUtils.printLabel(values[1].getVariable().toString() + "   磁盘大小：" + (long) totalSize * unit / (1024 * 1024 * 1024) + "G   磁盘使用率为：" + (long) usedSize * 100 / totalSize + "%");
            }
        }*/
        return list;
    }

    /**
     * 服务器进程集合信息
     */
    public static List<TableEvent> collectProcess(String ip, int port, String community) {
        OID[] oids = {//
                new OID("1.3.6.1.2.1.25.4.2.1.1"), //index
                new OID("1.3.6.1.2.1.25.4.2.1.2"), //name
                new OID("1.3.6.1.2.1.25.4.2.1.4"), //run path
                new OID("1.3.6.1.2.1.25.4.2.1.6"), //type
                new OID("1.3.6.1.2.1.25.5.1.1.1"), //cpu
                new OID("1.3.6.1.2.1.25.5.1.1.2"), //memory
        };
        List<TableEvent> list = collect(ip, port, community, oids);
        /*if (!checkEmpty(list)) {
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                String sp = "  ";
                String[] name = Mib2Library.fmtVB(values[1]);
                String[] cpu = Mib2Library.fmtVB(values[4]);
                String[] memory = Mib2Library.fmtVB(values[5]);
                String[] path = Mib2Library.fmtVB(values[2]);
                LogUtils.printLabel(name[0] + "(name)->" + name[1] + sp + cpu[0] + "(cpu)->" + cpu[1] + sp + memory[0] + "(memory)->" + memory[1] + sp + path[0] + "(path)->" + path[1]);
            }
        }*/
        return list;
    }

    /**
     * 服务器系统服务集合
     */
    public static List<TableEvent> collectService(String ip, int port, String community) {
        OID[] oids = {new OID("1.3.6.1.4.1.77.1.2.3.1.1")};
        List<TableEvent> list = collect(ip, port, community, oids);
        /*if (!checkEmpty(list)) {
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                String name = values[0].getVariable().toString();//name
                LogUtils.printLabel("名称--->" + name);//中文乱码，需要转为utf-8编码
            }
        }*/
        return list;
    }

    /**
     * 服务器接口集合
     */
    public static List<TableEvent> collectInterface(String ip, int port, String community) {
        OID[] ifOIDs = {//
                new OID("1.3.6.1.2.1.2.2.1.1"),  //Index
                new OID("1.3.6.1.2.1.2.2.1.2"),  //descr
                new OID("1.3.6.1.2.1.2.2.1.3"),  //type
                new OID("1.3.6.1.2.1.2.2.1.5"),  //speed
                new OID("1.3.6.1.2.1.2.2.1.6"),  //mac
                new OID("1.3.6.1.2.1.2.2.1.7"),  //adminStatus
                new OID("1.3.6.1.2.1.2.2.1.8"),  //operStatus
                //
                new OID("1.3.6.1.2.1.2.2.1.10"), //inOctets
                new OID("1.3.6.1.2.1.2.2.1.16"), //outOctets
                //new OID("1.3.6.1.2.1.2.2.1.14"), //inError
                //new OID("1.3.6.1.2.1.2.2.1.20"), //outError
                //new OID("1.3.6.1.2.1.2.2.1.13"), //inDiscard
                //new OID("1.3.6.1.2.1.2.2.1.19"), //outDiscard
                //new OID("1.3.6.1.2.1.2.2.1.11"), //inUcastPkts
                //new OID("1.3.6.1.2.1.2.2.1.17"), //outUcastPkts
                //new OID("1.3.6.1.2.1.2.2.1.12"), //inNUcastPkts
                //new OID("1.3.6.1.2.1.2.2.1.18"), //outNUcastPkts
        };
        List<TableEvent> ifList = collect(ip, port, community, ifOIDs);
        /*if (!checkEmpty(ifList)) {
            for (TableEvent event : ifList) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                LogUtils.printLabel("Interface -> index=" + Mib2Library.fmtVB(values[0])[1]//
                        + ", descr=" + Mib2Library.fmtVB(values[1])[1] //
                        + ", type=" + Mib2Library.fmtVB(values[2])[1] //
                        + ", speed=" + Mib2Library.fmtVB(values[3])[1] //
                        + ", mac=" + Mib2Library.fmtVB(values[4])[1] //
                        + ", adminStatus=" + Mib2Library.fmtVB(values[5])[1] //
                        + ", operStatus=" + Mib2Library.fmtVB(values[6])[1]);
            }
        }*/

        OID[] ipOIDs = {//
                new OID("1.3.6.1.2.1.4.20.1.1"), //ipAdEntAddr
                new OID("1.3.6.1.2.1.4.20.1.2"), //ipAdEntIfIndex
                new OID("1.3.6.1.2.1.4.20.1.3"), //ipAdEntNetMask
        };
        List<TableEvent> ipList = collect(ip, port, community, ipOIDs);
        /*if (!checkEmpty(ipList)) {
            for (TableEvent event : ipList) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                LogUtils.printLabel("IP -> ipAdEntAddr=" + Mib2Library.fmtVB(values[0])[1] //
                        + ", ipAdEntIfIndex=" + Mib2Library.fmtVB(values[1])[1] //
                        + ", ipAdEntNetMask=" + Mib2Library.fmtVB(values[2])[1]);
            }
        }*/
        return ifList;
    }

    /**
     * 服务器端口集合
     */
    public static List<TableEvent> collectPort(String ip, int port, String community) {
        OID[] tcpOIDs = {//
                new OID("1.3.6.1.2.1.6.13.1.3"), //port
                new OID("1.3.6.1.2.1.6.13.1.1"), //status
        };
        List<TableEvent> tcpList = collect(ip, port, community, tcpOIDs);
        /*if (!checkEmpty(tcpList)) {
            for (TableEvent event : tcpList) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                int status = Integer.parseInt(values[0].getVariable().toString());
                LogUtils.printLabel("status--->" + status + "   TCP_port--->" + values[1].getVariable().toString());
            }
        }*/

        OID[] udpOIDs = {new OID("1.3.6.1.2.1.7.5.1.2")};
        List<TableEvent> udpList = collect(ip, port, community, udpOIDs);
        /*if (!checkEmpty(udpList)) {
            for (TableEvent event : udpList) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                String name = values[0].getVariable().toString(); //name
                LogUtils.printLabel("UDP_port--->" + name);
            }
        }*/
        return tcpList;
    }

    /**
     * 服务器安装软件集合
     */
    public static List<TableEvent> collectSoft(String ip, int port, String community) {
        OID[] oids = {//
                new OID("1.3.6.1.2.1.25.6.3.1.2"), //software
                new OID("1.3.6.1.2.1.25.6.3.1.4"), //type
                new OID("1.3.6.1.2.1.25.6.3.1.5"), //install date
        };
        List<TableEvent> list = collect(ip, port, community, oids);
        /*if (!checkEmpty(list)) {
            for (TableEvent event : list) {
                VariableBinding[] values = event.getColumns();
                if (values == null) {
                    continue;
                }
                String software = values[0].getVariable().toString(); //software
                String type = values[1].getVariable().toString(); //type
                String date = values[2].getVariable().toString(); //date
                LogUtils.printLabel("软件名称--->" + software + "  type--->" + type + "  安装时间--->" + hexToDateTime(date.replace("'", "")));
            }
        }*/
        return list;
    }

    // ------------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        // 107.155.19.82
        String ip = "109.244.46.18";
        int port = 161;
        // public C#Edge123
        String community = "C#EDGE123";
        //
        //collectCPU(ip, port, community);
        //collectMemory(ip, port, community);
        //collectDisk(ip, port, community);
        //collectProcess(ip, port, community);
        //collectService(ip, port, community);
        //collectInterface(ip, port, community);
        //collectPort(ip, port, community);
        //collectSoft(ip, port, community);

        //OID oid = Mib2Library.SysName.getOID();
        //snmpSet(ip, port, community, oid, "Test.System.Name");
        //PDU retGet = snmpGet(ip, port, community, new OID[] {oid});
        //printPduResponse(retGet);

        OID[] oids = { //
                Mib2Library.IfIndex.getOID(), //
                Mib2Library.IfName.getOID(), //
                Mib2Library.IfAlias.getOID(),//
                Mib2Library.IfHCInOctets.getOID(), //
                //Mib2Library.IpAdEntAddr.getOID(), //
                //Mib2Library.IpAdEntNetMask.getOID(), //
                //Mib2Library.IpRouteDest.getOID(), //
                //Mib2Library.IpRouteIfIndex.getOID(), //
        };
        PDU result = snmpGet(ip, port, community, oids);
        printPduResponse(result);
        List<TableEvent> list = collect(ip, port, community, oids);
        LogUtils.printLabel("collect : " + list.size());
    }
}
