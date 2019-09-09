package cn.hl.ox.snmp.trap;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * 本类用于发送Trap信息
 * @author Hyman
 */
public class SnmpTrapSender {

    private Snmp                         snmp          = null;
    private Address                      targetAddress = null;
    private TransportMapping<UdpAddress> transport     = null;
    /** UsmUser的username */
    private String                       username1     = "user1";
    /** 认证协议的密码, 如MD5 */
    private String                       authPassword  = "password1";
    /** 加密协议密码,如:DES,AES */
    private String                       privPassword  = "password2";

    public static void main(String[] args) {
        SnmpTrapSender poc = new SnmpTrapSender();
        try {
            poc.init();
            poc.sendV1Trap();
            poc.sendV2cTrap();
            poc.sendV3TrapNoAuthNoPriv();
            poc.sendV3Auth();
            poc.sendV3();
            poc.sendV3_();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        //目标主机的IP地址 和 端口号
        targetAddress = GenericAddress.parse("udp:127.0.0.1/161");
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    /**
     * Snmp V1 测试发送Trap
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV1Trap() throws IOException {
        PDUv1 pdu = new PDUv1();
        VariableBinding vb = new VariableBinding();
        vb.setOid(SnmpConstants.sysName);
        vb.setVariable(new OctetString("Snmp Trap V1 Test"));
        pdu.add(vb);
        pdu.setType(PDU.V1TRAP);

        // set target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        // retry times when commuication error
        target.setRetries(2);
        // timeout
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version1);
        // send pdu, return response
        ResponseEvent response = snmp.send(pdu, target);
        printResponseEvent(response);

        return response;
    }

    /**
     * Snmp V2c 测试发送Trap
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV2cTrap() throws IOException {
        PDU pdu = new PDU();
        VariableBinding vb = new VariableBinding();
        vb.setOid(SnmpConstants.sysName);
        vb.setVariable(new OctetString("Snmp Trap V2 Test"));
        pdu.add(vb);
        pdu.setType(PDU.TRAP);

        // set target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);

        // retry times when commuication error
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        // send pdu, return response
        ResponseEvent response = snmp.send(pdu, target);
        printResponseEvent(response);

        return response;
    }

    /**
     * SnmpV3 不带认证加密协议.
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3TrapNoAuthNoPriv() throws IOException {
        SNMP4JSettings.setExtensibilityEnabled(true);
        SecurityProtocols.getInstance().addDefaultProtocols();

        UserTarget target = new UserTarget();
        target.setVersion(SnmpConstants.version3);

        try {
            transport = new DefaultUdpTransportMapping();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        byte[] enginId = "TEO_ID".getBytes();
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(enginId), 500);
        SecurityModels secModels = SecurityModels.getInstance();
        if (snmp.getUSM() == null) {
            secModels.addSecurityModel(usm);
        }

        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);

        target.setAddress(targetAddress);

        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(PDU.NOTIFICATION);
        VariableBinding vb = new VariableBinding();
        vb.setOid(SnmpConstants.sysName);
        vb.setVariable(new OctetString("Snmp Trap V3 Test sendV3TrapNoAuthNoPriv"));
        pdu.add(vb);

        snmp.setLocalEngine(enginId, 500, 1);
        ResponseEvent response = snmp.send(pdu, target);
        printResponseEvent(response);

        return response;
    }

    /**
     * 目前不可以被接收
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3Auth() throws IOException {
        SNMP4JSettings.setExtensibilityEnabled(true);
        SecurityProtocols.getInstance().addDefaultProtocols();

        UserTarget target = new UserTarget();
        target.setSecurityName(new OctetString(username1));
        target.setVersion(SnmpConstants.version3);

        try {
            transport = new DefaultUdpTransportMapping();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        byte[] enginId = "TEO_ID".getBytes();
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(enginId), 500);
        SecurityModels secModels = SecurityModels.getInstance();
        synchronized (secModels) {
            if (snmp.getUSM() == null) {
                secModels.addSecurityModel(usm);
            }
            snmp.getUSM().addUser(new OctetString(username1), new OctetString(enginId), new UsmUser(new OctetString(username1), AuthMD5.ID, new OctetString(authPassword), Priv3DES.ID, new OctetString(privPassword)));
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);

            target.setAddress(targetAddress);

            ScopedPDU pdu = new ScopedPDU();
            pdu.setType(PDU.NOTIFICATION);
            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString("Snmp Trap V3 Test sendV3Auth"));
            pdu.add(v);

            snmp.setLocalEngine(enginId, 500, 1);
            ResponseEvent response = snmp.send(pdu, target);
            //System.out.println(send.getError());
            printResponseEvent(response);

            return response;
        }
    }

    /**
     * 测试SnmpV3  带认证协议，加密协议
     * @return
     * @throws IOException
     */
    public ResponseEvent sendV3() throws IOException {
        OctetString userName = new OctetString(username1);
        OctetString authPass = new OctetString(authPassword);
        OctetString privPass = new OctetString("privPassword");

        TransportMapping<?> transport;
        transport = new DefaultUdpTransportMapping();

        Snmp snmp = new Snmp(transport);
        //MPv3.setEnterpriseID(35904);
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 500);
        SecurityModels.getInstance().addSecurityModel(usm);

        UserTarget target = new UserTarget();

        byte[] enginId = "TEO_ID".getBytes();

        SecurityModels secModels = SecurityModels.getInstance();
        synchronized (secModels) {
            if (snmp.getUSM() == null) {
                secModels.addSecurityModel(usm);
            }
            /*snmp.getUSM().addUser(
                    new OctetString(username),
                    new OctetString(enginId),
                    new UsmUser(new OctetString(username), AuthMD5.ID,
                            new OctetString(authPassword), Priv3DES.ID,
                            new OctetString(privPassword)));*/
            // add user to the USM
            snmp.getUSM().addUser(userName, new UsmUser(userName, AuthMD5.ID, authPass, PrivDES.ID, privPass));

            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(3000);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
            target.setSecurityName(userName);

            ScopedPDU pdu = new ScopedPDU();
            pdu.setType(PDU.NOTIFICATION);
            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString("Snmp Trap V3 Test sendV3Auth----------"));
            pdu.add(v);

            snmp.setLocalEngine(enginId, 500, 1);
            ResponseEvent response = snmp.send(pdu, target);
            //System.out.println(send.getError());
            printResponseEvent(response);

            return response;
        }

    }

    public ResponseEvent sendV3_() throws IOException {
        snmp.getUSM().addUser(new OctetString("MD5DES"), new UsmUser(new OctetString("MD5DES"), AuthMD5.ID, new OctetString("MD5DESUserAuthPassword"), PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
        // create the target
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("MD5DES"));

        // create the PDU
        PDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(new OID("1.3.6")));
        pdu.setType(PDU.GETNEXT);

        // send the PDU
        ResponseEvent response = snmp.send(pdu, target);
        printResponseEvent(response);

        return response;
    }

    private void printResponseEvent(ResponseEvent response) {
        if (response == null) {
            return;
        }
        // extract the response PDU (could be null if timed out)
        PDU responsePDU = response.getResponse();
        // extract the address used by the agent to send the response:
        Address peerAddress = response.getPeerAddress();
        System.out.println(peerAddress);

        Vector<? extends VariableBinding> vbs = responsePDU.getVariableBindings();
        for (VariableBinding vb : vbs) {
            int[] oid = vb.getOid().getValue();
            System.out.print("|" + oid[oid.length - 1] + " \t" + vb.getVariable() + " \t");
        }
    }
}
