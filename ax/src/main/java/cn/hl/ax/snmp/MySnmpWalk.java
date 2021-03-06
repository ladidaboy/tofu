package cn.hl.ax.snmp;

import cn.hl.ax.log.LogUtils;
import cn.hl.ax.snmp.mib.Mib2Library;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Hyman
 * @date 2019-08-05 13:20:48
 */
public class MySnmpWalk {
    /**
     * == Check snmp walk finish == <br>
     * 1) responsePDU == null <br>
     * 2) responsePDU.getErrorStatus() != 0 <br>
     * 3) responsePDU.get(0).getOid() == null <br>
     * 4) responsePDU.get(0).getOid().size() < targetOID.size() <br>
     * 5) targetOID.leftMostCompare(targetOID.size(),responsePDU.get(0).getOid())!=0 <br>
     * 6) Null.isExceptionSyntax(responsePDU.get(0).getVariable().getSyntax()) <br>
     * 7) responsePDU.get(0).getOid().compareTo(targetOID) <= 0 <br>
     *
     * @param targetOID   目标OID
     * @param responsePDU 响应PDU
     * @return true: Finished, false:NotFinished
     */
    private static boolean checkWalkFinished(OID targetOID, PDU responsePDU) {
        String message = "[FINISHED] ";
        if (responsePDU == null) {
            // 1
            message += "[ responsePDU == null ] ";
            LogUtils.printError(message);
            return true;
        }
        boolean finished = false;
        VariableBinding vb = responsePDU.get(0);
        if (responsePDU.getErrorStatus() != 0) {
            // 2
            message += "[ responsePDU.getErrorStatus() != 0 ] ";
            message += responsePDU.getErrorStatusText();
            finished = true;
        } else if (vb.getOid() == null) {
            // 3
            message += "[ vb.getOid() == null ]";
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            // 4
            message += "[ vb.getOid().size() < targetOID.size() ]";
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            // 5
            message += "[ targetOID.leftMostCompare() != 0 ]";
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            // 6
            message += "[ Null.isExceptionSyntax(vb.getVariable().getSyntax()) ]";
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            // 7
            message += "[ Variable received is not lexicographic successor of requested one:";
            message += vb.toString() + " <= " + targetOID + " ]";
            finished = true;
        }

        if (finished) {
            LogUtils.printError(message);
        }

        return finished;
    }

    /**
     * Create default CommunityTarget
     *
     * @param ip        地址
     * @param port      端口
     * @param community 共同体
     * @return CommunityTarget
     */
    private static CommunityTarget createDefault(String ip, int port, String community) {
        CommunityTarget target = new CommunityTarget();
        target.setAddress(GenericAddress.parse("udp:" + ip + "/" + port));
        target.setCommunity(new OctetString(community));
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(3000);
        target.setRetries(2);
        return target;
    }

    private static void clear(TransportMapping transport, Snmp snmp) {
        if (snmp != null) {
            try {
                snmp.close();
            } catch (IOException ex1) {
                snmp = null;
            }
        }
        if (transport != null) {
            try {
                transport.close();
            } catch (IOException ex2) {
                transport = null;
            }
        }
    }

    /**
     * 异步采集数据
     *
     * @param ip        地址
     * @param port      端口
     * @param community 共同体
     * @param oid       OID信息
     */
    public static void walkAsyn(String ip, int port, String community, OID oid) {
        final CommunityTarget target = createDefault(ip, port, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        try {
            LogUtils.printLabel("----====>>>> SNMP Asynchronous Walk START <<<<====----");

            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            final PDU request = new PDU();
            final CountDownLatch latch = new CountDownLatch(1);
            request.add(new VariableBinding(oid));

            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);

                    try {
                        PDU response = event.getResponse();
                        boolean finished = checkWalkFinished(oid, response);
                        if (finished) {
                            LogUtils.printLabel("---->>>> SNMP Walked OIDs value successfully! <<<<----");
                            latch.countDown();
                        } else {
                            VariableBinding vb = response.get(0);
                            // Received Walk response value
                            LogUtils.printLabel(Mib2Library.formatVB(vb));
                            request.setRequestID(new Integer32(0));
                            request.set(0, vb);
                            ((Snmp) event.getSource()).getNext(request, target, null, this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }
                }
            };

            snmp.getNext(request, target, null, listener);
            LogUtils.printError("PDU 已发送,等到异步处理结果...");
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            LogUtils.printLabel("latch.await =: " + wait);
            snmp.close();
        } catch (Exception e) {
            LogUtils.printError("SNMP Asynchronous Walk Exception:", e);
        } finally {
            clear(transport, snmp);
            LogUtils.printLabel("----====>>>> SNMP Asynchronous Walk STOP! <<<<====----");
        }
    }

    /**
     * 同步采集数据
     *
     * @param ip        地址
     * @param port      端口
     * @param community 共同体
     * @param oid       OID信息
     */
    public static void walkSync(String ip, int port, String community, OID oid) {
        CommunityTarget target = createDefault(ip, port, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        try {
            LogUtils.printLabel("----====>>>> SNMP *Synchronous Walk START <<<<====----");

            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            PDU request = new PDU();
            request.add(new VariableBinding(oid));
            LogUtils.printError("PDU 已发送,等到同步处理结果...");
            boolean finished = false;
            while (!finished) {
                ResponseEvent event = snmp.getNext(request, target);
                PDU response = event.getResponse();
                // check finish
                finished = checkWalkFinished(oid, response);
                if (finished) {
                    LogUtils.printLabel("---->>>> SNMP Walked OIDs value successfully! <<<<----");
                    snmp.close();
                } else {
                    VariableBinding vb = response.get(0);
                    // Received Walk response value
                    LogUtils.printLabel(Mib2Library.formatVB(vb));
                    // Set up the variable binding for the next entry.
                    request.setRequestID(new Integer32(0));
                    request.set(0, vb);
                }
            }
        } catch (Exception e) {
            LogUtils.printError("SNMP *Synchronous Walk Exception:", e);
        } finally {
            clear(transport, snmp);
            LogUtils.printLabel("----====>>>> SNMP *Synchronous Walk STOP! <<<<====----");
        }
    }

    public static void main(String[] args) {
        int port = 161;
        String ip = "192.254.81.145";
        String community = "C#EDGE123";
        // 异步采集数据
        walkAsyn(ip, port, community, Mib2Library.System.getOID());
        // 同步采集数据
        walkSync(ip, port, community, Mib2Library.Host.getOID());
    }
}
