package cn.hl.ox.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * @author hyman
 * @date 2019-08-01 15:20:08
 */
public class LearnSnmp2 {

    private Snmp snmp = null;
    private int  version;

    public LearnSnmp2(int version) {
        try {
            this.version = version;
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            if (version == SnmpConstants.version3) {
                // 设置安全模式
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
            }
            // 开始监听消息
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(boolean syn, final Boolean bro, PDU pdu, String addr, String community) throws Exception {
        // 生成目标地址对象
        Address targetAddress = GenericAddress.parse(addr);
        Target target = null;
        if (version == SnmpConstants.version3) {
            // 添加用户
            UsmUser user = new UsmUser(new OctetString("MD5DES"), //
                    AuthMD5.ID, new OctetString("MD5DESUserAuthPassword"), //
                    PrivDES.ID, new OctetString("MD5DESUserPrivPassword"));
            snmp.getUSM().addUser(user.getSecurityName(), user);
            target = new UserTarget();
            // 设置安全级别
            ((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
            ((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
            target.setVersion(SnmpConstants.version3);
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                ((CommunityTarget) target).setCommunity(new OctetString(community));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString(community));
            }

        }
        // 目标对象相关设置
        target.setAddress(targetAddress);
        target.setTimeout(5000);
        target.setRetries(2);

        if (syn) {
            // [异步]
            // 设置监听对象
            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    if (bro.equals(false)) {
                        ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    }
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println("Asynchronous(异步) message(消息) from(来自) " + event.getPeerAddress());
                    System.out.println("request (发送的请求): " + request);
                    System.out.println("response(返回的响应): " + response);
                }

            };
            // 发送报文
            snmp.send(pdu, target, null, listener);
        } else {
            // [同步]
            // 发送报文 并且接受响应
            ResponseEvent event = snmp.send(pdu, target);
            // 处理响应
            System.out.println("Synchronize(同步) message(消息) from(来自) " + event.getPeerAddress());
            System.out.println("request (发送的请求): " + event.getRequest());
            System.out.println("response(返回的响应): " + event.getResponse());
        }
    }

    public static void main(String[] args) {
        //Snmp的三个版本号
        //int ver3 = SnmpConstants.version3;
        int ver2c = SnmpConstants.version2c;
        //int ver1 = SnmpConstants.version1;
        LearnSnmp2 manager = new LearnSnmp2(ver2c);

        // 构造报文
        PDU request = new PDU();
        //PDU pdu = new ScopedPDU();

        // 设置要获取的对象ID
        request.add(new VariableBinding(SnmpConstants.sysContact));

        // 设置报文类型
        request.setType(PDU.GET);
        //((ScopedPDU) pdu).setContextName(new OctetString("priv"));

        try {
            // 发送消息 其中最后一个是想要发送的目标地址
            manager.sendMessage(true, true, request, "udp:127.0.0.1/161", "public");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* IP: 23.236.118.192  PORT: 161  COMMUNITY: C#EDGE123 */
    /*
     输出结果：
     Synchronize(同步) message(消息) from(来自) 192.168.1.233/161
     request(发送的请求):GET[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = Null]]
     response(返回的响应):RESPONSE[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = WIN-667H6TS3U37]]
     */
}
