package cn.hl.ox.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author hyman
 * @date 2019-08-01 16:56:39
 * @version $ Id: LearnSnmp.java, v 0.1  hyman Exp $
 */
public class LearnSnmp {
    /*
     一、核心对象SNMP的初始化。
       源码中有四种初始化方法及四个构造函数，其实都大同小异：参数少的就必须后续添加，参数多的必须提前初始化。
     二、核心对象Target
       我们先看下Target对象下的继承关系。其中主要用到的子对象是CommunityTarget和UserTarget，CommunityTarget用于SNMPv1和SNMPv2c这两个版本，而UserTarget用于SNMPV3版本。
     三、核心对象PDU
       跟Target一样，针对snmp的不同版本是使用不同的子类去实现。PDUv1用于SNMPv1和SNMPv2c这两个版本，而ScopedPDU用于SNMPV3版本。
     */

    public static  Snmp   snmp      = null;
    private static String community = null;
    private static String ipAddress = null;
    private static int    port      = 161;

    /**
     * 第一步：要初始snmp并开启监听。其中有点不同的是，为了支持snmpv3版本的处理需要增加用户并设置安全名称和加密算法。
     * （关于那些静态变量的值，最好放到配置文件中显得灵活点。）
     * <br>
     * 另外再说明下：snmp是基于udp协议发送报文的，且snmp端口默认为161。
     */
    public static void initSnmp() throws IOException {
        //1、初始化多线程消息转发类
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        //其中要增加三种处理模型。如果snmp初始化使用的是Snmp(TransportMapping<? extends Address> transportMapping) ,就不需要增加
        messageDispatcher.addMessageProcessingModel(new MPv1());
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        //当要支持snmpV3版本时，需要配置user
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(), localEngineID, 0);
        UsmUser user = new UsmUser(new OctetString("SNMPV3"), //
                AuthSHA.ID, new OctetString("authPassword"),  //
                PrivAES128.ID, new OctetString("privPassword"));
        usm.addUser(user.getSecurityName(), user);
        messageDispatcher.addMessageProcessingModel(new MPv3(usm));

        //2、创建transportMapping
        //UdpAddress updAddr = (UdpAddress) GenericAddress.parse("udp:" + ipAddress + "/" + port);
        //TransportMapping<?> transportMapping = new DefaultUdpTransportMapping(updAddr);

        //3、正式创建snmp
        //snmp = new Snmp(messageDispatcher, transportMapping);
        snmp = new Snmp(new DefaultUdpTransportMapping());
        //开启监听
        snmp.listen();
    }

    /**
     * 第二步： 根据snmp版本创建Target对象，其中针对snmpV3版本需要设置安全级别和安全名称，
     * 其中安全名称是创建snmp指定user设置的new OctetString("SNMPV3")，针对snmpv1和snmpv2c需要设置团体名。
     * 另外必须设置ipAddress，且对应的主机要配置snmp否则获取不到值。
     * @param version SNMP版本
     * @return Target
     */
    private static Target createTarget(int version) throws UnknownHostException {
        Target target = null;
        if (!(version == SnmpConstants.version3 || version == SnmpConstants.version2c || version == SnmpConstants.version1)) {
            System.err.println("参数version异常");
            return target;
        }
        if (version == SnmpConstants.version3) {
            target = new UserTarget();
            //snmpV3需要设置安全级别和安全名称，其中安全名称是创建snmp指定user设置的new OctetString("SNMPV3")
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(new OctetString("SNMPV3"));
        } else {
            //snmpV1和snmpV2需要指定团体名名称
            target = new CommunityTarget();
            ((CommunityTarget) target).setCommunity(new OctetString(community));
            if (version == SnmpConstants.version2c) {
                target.setSecurityModel(SecurityModel.SECURITY_MODEL_SNMPv2c);
            }
        }
        target.setVersion(version);
        //必须指定，没有设置就会报错。
        UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(ipAddress), port);
        target.setAddress(udpAddress);
        target.setTimeout(3000);
        target.setRetries(5);
        return target;
    }

    /**
     * 第三步：创建报文。其中要注意的是pdu可以设置类型，如果想要用snmpget方法，就设置PDU.GET。
     * @param version SNMP版本
     * @param type PDU类型
     * @param oid OID信息
     * @return
     */
    private static PDU createPDU(int version, int type, String oid) {
        PDU pdu = null;
        if (version == SnmpConstants.version3) {
            pdu = new ScopedPDU();
        } else if (version == SnmpConstants.version1) {
            pdu = new PDUv1();
        } else {
            pdu = new PDU();
        }
        pdu.setType(type);
        //可以添加多个变量oid
        pdu.add(new VariableBinding(new OID(oid)));
        return pdu;
    }

    /**
     * 最后一步发送报文也是最重要的一步，需要前面三步的支撑才能进行。
     * @param oid OID信息
     */
    public static void snmpGet(String oid) {
        try {
            //1、初始化snmp,并开启监听
            initSnmp();
            //2、创建目标对象
            // SnmpConstants.DEFAULT_COMMAND_RESPONDER_PORT
            Target target = createTarget(SnmpConstants.version2c);
            //3、创建报文
            PDU pdu = createPDU(SnmpConstants.version2c, PDU.GET, oid);
            //4、发送报文，并获取返回结果
            System.out.println("-------> 发送PDU <-------");
            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();
            System.out.println("返回结果：" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        community = "C#EDGE123";//"public";//
        ipAddress = "23.236.118.192";//"127.0.0.1";//
        snmpGet(SnmpConstants.sysContact.toString());
    }
}
