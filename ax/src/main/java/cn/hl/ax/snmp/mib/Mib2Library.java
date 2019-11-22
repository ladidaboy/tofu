package cn.hl.ax.snmp.mib;

import cn.hl.ax.snmp.formatter.IfStatusVF;
import cn.hl.ax.snmp.formatter.IfTypeVF;
import cn.hl.ax.snmp.formatter.IpForwardingVF;
import cn.hl.ax.snmp.formatter.IpNetToMediaTypeVF;
import cn.hl.ax.snmp.formatter.IpRouteTypeVF;
import cn.hl.ax.snmp.formatter.TcpConnStateVF;
import cn.hl.ax.snmp.formatter.TcpRtoAlgorithmVF;
import cn.hl.ax.snmp.formatter.VariableFormatter;
import cn.hl.ax.snmp.mib.Mib.AccessMode;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyman
 * @date 2019-08-05 16:18:44
 * @version $ Id: Mib2Library.java, v 0.1  hyman Exp $
 */
public class Mib2Library {
    private static int                            maxL = 0;
    private static List<Mib>                      mibs = new ArrayList<>();
    private static Map<String, VariableFormatter> maps = new HashMap<>();

    public static Mib put(String name, int[] oid, String dataType, AccessMode accessMode, String description) {
        Mib mib = new Mib(name, oid, dataType, accessMode.getValue(), description);
        maxL = Math.max(maxL, name.length() + 4);
        mibs.add(mib);
        return mib;
    }

    public static Mib getMib(OID oid) {
        if (oid == null) {
            return null;
        }
        int oidValue[] = oid.getValue();
        String oidText = oid.toString();
        for (int i = mibs.size() - 1; i >= 0; i--) {
            int diff = arrayContains(oidValue, mibs.get(i).getOid());
            if (diff >= 0) {
                return mibs.get(i);
            }
        }
        return null;
    }

    private static int arrayContains(int[] big, int[] small) {
        if (big == null || big.length == 0 || small == null || small.length == 0 || big.length < small.length) {
            return -1;
        }
        for (int i = 0; i < small.length; i++) {
            if (big[i] != small[i]) {
                return -1;
            }
        }
        return big.length - small.length;
    }

    public static String[] fmtVB(VariableBinding vb) {
        String[] mibInfos = new String[4];// {OID.name, Variable(format), Variable, OID.Syntax}
        Mib mib = getMib(vb.getOid());
        String oidText = vb.getOid().toString(), oidValue;
        if (mib == null) {
            mibInfos[0] = vb.getOid().toString();
            mibInfos[1] = vb.getVariable().toString();
            mibInfos[2] = vb.getVariable().toString();
            mibInfos[3] = "S#" + vb.getOid().getSyntax();
        } else {
            VariableFormatter formatter = maps.get(mib.getName());
            oidValue = formatter == null ? vb.getVariable().toString() : formatter.format(vb);
            mibInfos[0] = oidText.replace(mib.getOidText(), mib.getName());
            mibInfos[1] = mib.getDataType() + ": " + oidValue;
            mibInfos[2] = oidValue;
            mibInfos[3] = mib.getDataType();
        }
        return mibInfos;
    }

    public static String formatVB(VariableBinding vb) {
        String mibInfo;
        Mib mib = getMib(vb.getOid());
        String oidText = vb.getOid().toString(), oidValue;
        if (mib == null) {
            mibInfo = "mib-2::" + vb.getOid() + " = " + vb.getVariable();
        } else {
            oidText = oidText.replace(mib.getOidText(), mib.getName());
            for (int i = oidText.length(); i <= maxL; i++) {
                oidText += " ";
            }
            VariableFormatter formatter = maps.get(mib.getName());
            oidValue = formatter == null ? vb.getVariable().toString() : formatter.format(vb);
            mibInfo = "mib-2::" + oidText + " = " + mib.getDataType() + ": " + oidValue;
        }
        return mibInfo;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    /*
     {iso(1), identified-organization(3), dod(6), internet(1), mgmt(2), mib-2(1)} (247 child OIDs)
     1.3.6.1.2.1.0   - reserved
     1.3.6.1.2.1.1   - SNMP MIB-2 System
     1.3.6.1.2.1.2   - SNMP MIB-2 Interfaces
     1.3.6.1.2.1.3   - at
     1.3.6.1.2.1.4   - ip
     1.3.6.1.2.1.5   - icmp
     1.3.6.1.2.1.6   - tcp
     1.3.6.1.2.1.7   - udp
     1.3.6.1.2.1.8   - egp
     1.3.6.1.2.1.9   - cmot
     1.3.6.1.2.1.10  - transmission
     1.3.6.1.2.1.11  - snmp
     1.3.6.1.2.1.12  - genericIf
     1.3.6.1.2.1.13  - appletalk
     1.3.6.1.2.1.14  - OSPF Version 2 MIB
     1.3.6.1.2.1.15  - BGPv4
     1.3.6.1.2.1.16  - Managed Objects for Bridges
     1.3.6.1.2.1.17  - Bridge Mib
     1.3.6.1.2.1.18  - decnetP4
     1.3.6.1.2.1.19  - char
     1.3.6.1.2.1.20  - snmpParties
     1.3.6.1.2.1.21  - snmpSecrets
     1.3.6.1.2.1.22  - rptrHealth
     1.3.6.1.2.1.23  - rip2
     1.3.6.1.2.1.24  - ident
     1.3.6.1.2.1.25  - HOST-RESOURCES-MIB, from RFC 1514
     1.3.6.1.2.1.26  - Ethernet MAU mib
     1.3.6.1.2.1.27  - Application MIB module
     1.3.6.1.2.1.28  - MTA MIB module
     1.3.6.1.2.1.29  - X.500 Directory MIB module
     1.3.6.1.2.1.30  - ianaifType
     1.3.6.1.2.1.31  - ifMib
     1.3.6.1.2.1.32  - dns
     1.3.6.1.2.1.33  - upsMIB(33)
     1.3.6.1.2.1.34  - snanauMIB(34)
     1.3.6.1.2.1.35  - etherMIB(35)
     1.3.6.1.2.1.36  - sipMIB(36)
     1.3.6.1.2.1.37  - atmMIB(37)
     1.3.6.1.2.1.38  - mdmMIB(38)
     1.3.6.1.2.1.39  - rdbmsMIB(39)
     1.3.6.1.2.1.40  - flowMIB(40)
     1.3.6.1.2.1.41  - snaDLC(41)
     1.3.6.1.2.1.42  - dot5SrMIB(42)
     1.3.6.1.2.1.43  - Printer-MIB(43)
     1.3.6.1.2.1.44  - mipMIB(44)
     1.3.6.1.2.1.45  - dot12MIB(45)
     1.3.6.1.2.1.46  - dlswMIB(46)
     1.3.6.1.2.1.47  - entity-mib(47)
     1.3.6.1.2.1.48  - ipMIB(48)
     1.3.6.1.2.1.49  - tcpMIB(49)
     1.3.6.1.2.1.50  - udpMIB(50)
     1.3.6.1.2.1.51  - rsvp(51)
     1.3.6.1.2.1.52  - intSrv(52)
     1.3.6.1.2.1.53  - vgRptrMIB(53)
     1.3.6.1.2.1.54  - sysApplMIB(54)
     1.3.6.1.2.1.55  - ipv6MIB(55)
     1.3.6.1.2.1.56  - ipv6IcmpMIB(56)
     1.3.6.1.2.1.57  - marsMIB(57)
     1.3.6.1.2.1.58  - perfHistTCMIB(58)
     1.3.6.1.2.1.59  - atmAccountingInformationMIB(59)
     1.3.6.1.2.1.60  - accountingControlMIB(60)
     1.3.6.1.2.1.61  - iANATn3270eTCMIB(61)
     1.3.6.1.2.1.62  - applicationMib(62)
     1.3.6.1.2.1.63  - schedMIB(63)
     1.3.6.1.2.1.64  - scriptMIB(64)
     1.3.6.1.2.1.65  - wwwMIB(65)
     1.3.6.1.2.1.66  - dsMIB(66)
     1.3.6.1.2.1.67  - radiusMIB(67)
     1.3.6.1.2.1.68  - vrrpMIB(68)
     1.3.6.1.2.1.69  - docsDev(69)
     1.3.6.1.2.1.70  - etherChipsetMIB(70)
     1.3.6.1.2.1.71  - nhrpMIB(71)
     1.3.6.1.2.1.72  - ianaAddressFamily(72)
     1.3.6.1.2.1.73  - ianalanguagemib(73)
     1.3.6.1.2.1.74  - agentxMIB(74)
     1.3.6.1.2.1.75  - fcFeMIB(75)
     1.3.6.1.2.1.76  - inetAddressMIB(76)
     1.3.6.1.2.1.77  - ifInvertedStackMIB(77)
     1.3.6.1.2.1.78  - hcnumTC(78)
     1.3.6.1.2.1.79  - ptopoMIB(79)
     1.3.6.1.2.1.80  - pingMIB(80)
     1.3.6.1.2.1.81  - traceRouteMIB(81)
     1.3.6.1.2.1.82  - lookupMIB(82)
     1.3.6.1.2.1.83  - ipMRouteStdMIB(83)
     1.3.6.1.2.1.84  - ianaipRouteProtocolMIB(84)
     1.3.6.1.2.1.85  - igmpStdMIB(85)
     1.3.6.1.2.1.86  - frAtmIwfMIB(86)
     1.3.6.1.2.1.87  - rtpMIB(87)
     1.3.6.1.2.1.88  - dismanEventMIB(88)
     1.3.6.1.2.1.89  - copsClientMIB(89)
     1.3.6.1.2.1.90  - dismanExpressionMIB(90)
     1.3.6.1.2.1.91  - mldMIB(91)
     1.3.6.1.2.1.92  - notificationLogMIB(92)
     1.3.6.1.2.1.93  - pintMib(93)
     1.3.6.1.2.1.94  - circuitIfMIB(94)
     1.3.6.1.2.1.95  - frsldMIB(95)
     1.3.6.1.2.1.96  - diffServDSCPTC(96)
     1.3.6.1.2.1.97  - diffServMib(97)
     1.3.6.1.2.1.98  - gsmpMIB(98)
     1.3.6.1.2.1.99  - entitySensorMIB(99)
     1.3.6.1.2.1.100 - transportAddressMIB(100)
     1.3.6.1.2.1.101 _ mallocMIB(101)
     1.3.6.1.2.1.102 _ ianamallocMIB(102)
     1.3.6.1.2.1.103 _ ipv6FlowLabelMIB(103)
     1.3.6.1.2.1.104 _ sctpMIB(104)
     1.3.6.1.2.1.105 _ powerEthernetMIB(105)
     1.3.6.1.2.1.106 _ ianaCharsetMIB(106)
     1.3.6.1.2.1.107 _ hcPerfHistTCMIB(107)
     1.3.6.1.2.1.108 _ diffServConfigMib(108)
     1.3.6.1.2.1.109 _ ianaPrinterMIB(109)
     1.3.6.1.2.1.110 _ ianafinisherMIB(110)
     1.3.6.1.2.1.111 _ finisherMIB(111)
     1.3.6.1.2.1.112 _ rohcMIB(112)
     1.3.6.1.2.1.113 _ rohcUncmprMIB(113)
     1.3.6.1.2.1.114 _ rohcRtpMIB(114)
     1.3.6.1.2.1.115 _ tripTC(115)
     1.3.6.1.2.1.116 _ tripMIB(116)
     1.3.6.1.2.1.117 _ arcMibModule(117)
     1.3.6.1.2.1.118 _ alarmMIB(118)
     1.3.6.1.2.1.119 _ ianaItuAlarmNumbers(119)
     1.3.6.1.2.1.120 _ ituAlarmTc(120)
     1.3.6.1.2.1.121 _ ituAlarmMIB(121)
     1.3.6.1.2.1.122 _ teMIB(122)
     1.3.6.1.2.1.123 _ natMIB(123)
     1.3.6.1.2.1.124 _ pmMib(124)
     1.3.6.1.2.1.125 _ docsSubMgt(125)
     1.3.6.1.2.1.126 _ docsBpi2MIB(126)
     1.3.6.1.2.1.127 _ docsIetfQosMIB(127)
     1.3.6.1.2.1.128 - ianaIppmMetricsRegistry(128)
     1.3.6.1.2.1.129 - vpnTcMIB(129)
     1.3.6.1.2.1.130 - entityStateTc(130)
     1.3.6.1.2.1.131 - entityStateMIB(131)
     1.3.6.1.2.1.132 - docsDevNotifMIB(132)
     1.3.6.1.2.1.133 - mip6MIB(133)
     1.3.6.1.2.1.134 - rstpMIB(134)
     1.3.6.1.2.1.135 - t11FcNameServerMIB(135)
     1.3.6.1.2.1.136 - t11TcMIB(136)
     1.3.6.1.2.1.137 - t11FcFabricAddrMgrMIB(137)
     1.3.6.1.2.1.138 - isisMIB(138)
     1.3.6.1.2.1.139 - scsiMIB(139)
     1.3.6.1.2.1.140 - pktcIetfMtaMib(140)
     1.3.6.1.2.1.141 - ipsAuthMibModule(141)
     1.3.6.1.2.1.142 - iscsiMibModule(142)
     1.3.6.1.2.1.143 - t11FcFspfMIB(143)
     1.3.6.1.2.1.144 - t11FcRouteMIB(144)
     1.3.6.1.2.1.145 - radiusDynAuthClientMIB(145)
     1.3.6.1.2.1.146 - radiusDynAuthServerMIB(146)
     1.3.6.1.2.1.147 - t11FcVirtualFabricMIB(147)
     1.3.6.1.2.1.148 - sipTC(148)
     1.3.6.1.2.1.149 - sipCommonMIB(149)
     1.3.6.1.2.1.150 - sipUAMIB(150)
     1.3.6.1.2.1.151 - sipServerMIB(151)
     1.3.6.1.2.1.152 - ianaGmpls(152)
     1.3.6.1.2.1.153 - spdMIB(153)
     1.3.6.1.2.1.154 - ianaMauMIB(154)
     1.3.6.1.2.1.155 - dot3EponMIB(155)
     1.3.6.1.2.1.156 - tcpEStatsMIB(156)
     1.3.6.1.2.1.157 - pimStdMIB(157)
     1.3.6.1.2.1.158 - dot3OamMIB(158)
     1.3.6.1.2.1.159 - t11FabricLockMIB(159)
     1.3.6.1.2.1.160 - t11ZoneServerMIB(160)
     1.3.6.1.2.1.161 - t11FcRscnMIB(161)
     1.3.6.1.2.1.162 - t11FcFabricConfigServerMIB(162)
     1.3.6.1.2.1.163 - isnsMIB(163)
     1.3.6.1.2.1.164 - uriTcMIB(164)
     1.3.6.1.2.1.165 - langTagTcMIB(165)
     1.3.6.1.2.1.166 - ifCapStackMIB(166)
     1.3.6.1.2.1.167 - efmCuMIB(167)
     1.3.6.1.2.1.168 - ipMcastMIB(168)
     1.3.6.1.2.1.169 - pktcIetfSigMib(169)
     1.3.6.1.2.1.170 - udpliteMIB(170)
     1.3.6.1.2.1.171 - midcomMIB(171)
     1.3.6.1.2.1.172 - pimBsrMIB(172)
     1.3.6.1.2.1.173 - syslogTCMIB(173)
     1.3.6.1.2.1.174 - ianaPwe3MIB(174)
     1.3.6.1.2.1.175 - t11FcTcMIB(175)
     1.3.6.1.2.1.176 - t11FcSpAuthenticationMIB(176)
     1.3.6.1.2.1.177 - t11FcSpZoningMIB(177)
     1.3.6.1.2.1.178 - t11FcSpPolicyMIB(178)
     1.3.6.1.2.1.179 - t11FcSpSaMIB(179)
     1.3.6.1.2.1.180 - pwEnetStdMIB(180)
     1.3.6.1.2.1.181 - pwMplsStdMIB(181)
     1.3.6.1.2.1.182 - pktcIetfEventMib(182)
     1.3.6.1.2.1.183 - pwATMMIB(183)
     1.3.6.1.2.1.184 - nemoMIB(184)
     1.3.6.1.2.1.185 - mgmdStdMIB(185)
     1.3.6.1.2.1.186 - pwTDMMIB(186)
     1.3.6.1.2.1.187 - forcesMib(187)
     1.3.6.1.2.1.188 - pwTcStdMIB(188)
     1.3.6.1.2.1.189 - snmpSshtmMIB(189)
     1.3.6.1.2.1.190 - snmpTsmMIB(190)
     1.3.6.1.2.1.191 - ospfv3MIB(191)
     1.3.6.1.2.1.192 - syslogMsgMib(192)
     1.3.6.1.2.1.193 - ipfixMIB(193)
     1.3.6.1.2.1.194 - ipfixSelectorMIB(194)
     1.3.6.1.2.1.195 - capwapDot11MIB(195)
     1.3.6.1.2.1.196 - capwapBaseMIB(196)
     1.3.6.1.2.1.197 - ntpSnmpMIB(197)
     1.3.6.1.2.1.198 - snmpTlstmMIB(198)
     1.3.6.1.2.1.199 - vacmAaaMIB(199)
     1.3.6.1.2.1.200 - pwCepStdMIB(200)
     1.3.6.1.2.1.201 - floatTcMIB(201)
     1.3.6.1.2.1.202 - mplsFrrGeneralMIB(202)
     1.3.6.1.2.1.203 - mplsFrrOne2OneMIB(203)
     1.3.6.1.2.1.204 - mplsFrrFacilityMIB(204)
     1.3.6.1.2.1.205 - pmip6TCMIB(205)
     1.3.6.1.2.1.206 - pmip6MIB(206)
     1.3.6.1.2.1.207 - vrrpv3MIB(207)
     1.3.6.1.2.1.208 - g9981MIB(208)
     1.3.6.1.2.1.209 - g9982MIB(209)
     1.3.6.1.2.1.210 - g9983MIB(210)
     1.3.6.1.2.1.211 - gBondMIB(211)
     1.3.6.1.2.1.212 - psampMIB(212)
     1.3.6.1.2.1.213 - nhdpMIB(213)
     1.3.6.1.2.1.214 - rbridgeMIB(214)
     1.3.6.1.2.1.215 - ianaGBondTcMIB(215)
     1.3.6.1.2.1.216 - ianaEntityMIB(216)
     1.3.6.1.2.1.217 - uuidTCMIB(217)
     1.3.6.1.2.1.218 - rpkiRtrMIB(218)
     1.3.6.1.2.1.219 - manetOlsrv2MIB(219)
     1.3.6.1.2.1.220 - lispMIB(220)
     1.3.6.1.2.1.221 - ianaolsrv2LinkMetricType(221)
     1.3.6.1.2.1.222 - bfdMIB(222)
     1.3.6.1.2.1.223 - bfdTCStdMib(223)
     1.3.6.1.2.1.224 - fcipMIB(224)
     1.3.6.1.2.1.225 - ianaSmfMIB(225)
     1.3.6.1.2.1.226 - lowpanMIB(226)
     1.3.6.1.2.1.227 - pcePcepMIB(227)
     1.3.6.1.2.1.228 - ianaPowerStateSet(228)
     1.3.6.1.2.1.229 - energyObjectMib(229)
     1.3.6.1.2.1.230 - powerAttributesMIB(230)
     1.3.6.1.2.1.231 - energyObjectContextMIB(231)
     1.3.6.1.2.1.232 - ianaEnergyRelationMIB(232)
     1.3.6.1.2.1.233 - batteryMIB(233)
     1.3.6.1.2.1.234 - natv2MIB(234)
     1.3.6.1.2.1.235 - snmpUsmHmacSha2MIB(235)
     1.3.6.1.2.1.236 - vmMIB(236)
     1.3.6.1.2.1.237 - ianaStorageMediaTypeMIB(237)
     1.3.6.1.2.1.238 - trillOamMib(238)
     1.3.6.1.2.1.239 - swmMIB(239)
     1.3.6.1.2.1.240 - dsliteMIB(240)
     1.3.6.1.2.1.241 - ptpbaseMIB(241)
     1.3.6.1.2.1.242 - mapMIB(242)
     1.3.6.1.2.1.243 - mvpnMIB(243)
     1.3.6.1.2.1.244 - l2L3VpnMcastTCMIB(244)
     1.3.6.1.2.1.245 - l2L3VpnMcastMIB(245)
     1.3.6.1.2.1.264 - g9982MIB(264)

     {iso(1), identified-organization(3), dod(6), internet(1), private(4), IANA(1)}
     https://www.alvestrand.no/objectid/1.3.6.1.4.1.html
     1.3.6.1.4.1.2 - IBM
     1.3.6.1.4.1.9 - Cisco
     1.3.6.1.4.1.11 - Hewlett-Packard Company
     1.3.6.1.4.1.23 - Novell
     1.3.6.1.4.1.42 - Sun
     1.3.6.1.4.1.43 - 3COM Enterprise MIBs
     1.3.6.1.4.1.232 - Compaq
     1.3.6.1.4.1.311 - Microsoft
     1.3.6.1.4.1.343 - Intel Corporation
     1.3.6.1.4.1.674 - Dell Computers
     1.3.6.1.4.1.2011 - Huawei-3Com
     1.3.6.1.4.1.2021 - RedHat Linux
     1.3.6.1.4.1.2312 - RedHat Software
     */
    /**系统组 (.1.3.6.1.2.1.1)*/
    public static final Mib System                       = put("system", new int[] {1, 3, 6, 1, 2, 1, 1}, "OctetString", AccessMode.All, "[系统组]");
    public static final Mib SysDescr                     = put("sysDescr", System.child(1), "OctetString", AccessMode.Read, "关于该设备或实体的描述，如设备类型、硬件特性、操作系统信息等");
    public static final Mib SysObjectID                  = put("sysObjectID", System.child(2), "ObjectIdentifier", AccessMode.Read, "设备厂商的授权标识符");
    public static final Mib SysUpTime                    = put("sysUpTime", System.child(3), "TimeTicks", AccessMode.Read, "从系统(代理)的网络管理部分最后一次重新初始化以来，经过的时间量");
    public static final Mib SysContact                   = put("sysContact", System.child(4), "OctetString", AccessMode.All, "记录其他提供该设备支持的机构和(或)联系人的信息");
    public static final Mib SysName                      = put("sysName", System.child(5), "OctetString", AccessMode.All, "设备的名字，可能是官方的主机名或者是分配的管理名字");
    public static final Mib SysLocation                  = put("sysLocation", System.child(6), "OctetString", AccessMode.All, "该设备安装的物理位置");
    public static final Mib SysSevices                   = put("sysSevices", System.child(7), "Integer", AccessMode.Read, "该设备提供的服务(根据sysServices 的值也可以判定该设备是工作在OSI的第几层, 解释：本机返回72)");
    public static final Mib SysOrLastChange              = put("sysOrLastChange", System.child(8), "TimeStamp", AccessMode.Read, "任何sysORID对象的实例最近一次变化时，sysUpTime对象的值, 解释：本机返回(0) 0:00:00.00");
    public static final Mib SysORTable                   = put("sysORTable", System.child(9), "Sequence.sysOREntry", AccessMode.Read, "一个SNMPv2中作为代理角色的动态配置对象资源的表");
    public static final Mib SysOREntry                   = put("sysOREntry", SysORTable.child(1), "Sequence", AccessMode.Read, "指定可配置对象上的信息");
    public static final Mib SysORIndex                   = put("sysORIndex", SysOREntry.child(1), "ObjectIdentifier", AccessMode.Read, "用做sysORTable的索引");
    public static final Mib SysORID                      = put("sysORID", SysOREntry.child(2), "OctetString", AccessMode.Read, "该表项的OID，类似sysObjectID对象");
    public static final Mib SysORDescr                   = put("sysORDescr", SysOREntry.child(3), "OctetString", AccessMode.Read, "对象资源的描述，类似sysDescr对象");
    public static final Mib SysORUpTime                  = put("sysORUpTime", SysOREntry.child(4), "TimeStamp", AccessMode.Read, "包含该实例(该行)最后更新或实例化时，sysUpTime对象的值");
    /**接口组 (.1.3.6.1.2.1.2)*/
    public static final Mib Interfaces                   = put("interfaces", new int[] {1, 3, 6, 1, 2, 1, 2}, "OctetString", AccessMode.All, "[接口组]");
    public static final Mib IfNumber                     = put("ifNumber", Interfaces.child(1), "Integer", AccessMode.Read, "本地系统中包含的网络接口总数, 解释：本机返回2");
    public static final Mib IfTable                      = put("ifTable", Interfaces.child(2), "Sequence.ifEntry", AccessMode.None, "该表的接口表项的一行");
    public static final Mib IfEntry                      = put("ifEntry", IfTable.child(1), "Sequence", AccessMode.None, "一个指定的接口表项，包含所有该对象下定义的对象");
    public static final Mib IfIndex                      = put("ifIndex", IfEntry.child(1), "Integer", AccessMode.Read, "一个MIB引用定义，指向一个用于访问该网络接口的特定介");
    public static final Mib IfDescr                      = put("ifDescr", IfEntry.child(2), "OctetString", AccessMode.Read, "该接口的一个字符串描述，包括操作系统角度获得的接口名，可能包括以下值：eth0、ppp0和lo0, 解释：本机返回 1:MS TCP Loopback interface; 2:Broadcom NetLink (TM)Gigabit Ethernet-网络适配器");
    public static final Mib IfType                       = put("ifType", IfEntry.child(3), "OctetString", AccessMode.Read, "接口的类型。表6-3列出了特定的类型");
    public static final Mib IfMtu                        = put("ifMtu", IfEntry.child(4), "Integer", AccessMode.Read, "接口的最大传输单元。用来表示接口上可以发送或接受的最大帧");
    public static final Mib IfSpeed                      = put("ifSpeed", IfEntry.child(5), "Gauge", AccessMode.Read, "接口速率(容量), 解释：本机返回 1:10000000; 2:100000000");
    public static final Mib IfPhysAddress                = put("ifPhysAddress", IfEntry.child(6), "PhysAddress", AccessMode.Read, "接口的数据链路地址");
    public static final Mib IfAdminStatus                = put("ifAdminStatus", IfEntry.child(7), "Integer", AccessMode.All, "接口的管理状态，该状态是ifOperStatus中列出的已定义状态之一");
    public static final Mib IfOperStatus                 = put("ifOperStatus", IfEntry.child(8), "Integer", AccessMode.Read, "接口当前的操作状态。已定义的状态包括up(1)、down(2)和testing(3)");
    public static final Mib IfLastChange                 = put("ifLastChange", IfEntry.child(9), "TimeTicks", AccessMode.Read, "接口最后更新成当前操作状态的时刻");
    public static final Mib IfInOctets                   = put("ifInOctets", IfEntry.child(10), "Counter", AccessMode.Read, "从该接口上接受到的字节数，包括任何数据链路组帧的字节");
    public static final Mib IfInUcastPkts                = put("ifInUcastPkts", IfEntry.child(11), "Counter", AccessMode.Read, "通过上层协议传递到子网的单播报文数");
    public static final Mib IfInNUcastPkts               = put("ifInNUcastPkts", IfEntry.child(12), "Counter", AccessMode.Read, "传递给上层网络协议的非单播报文数");
    public static final Mib IfInDiscards                 = put("ifInDiscards", IfEntry.child(13), "Counter", AccessMode.Read, "被丢弃(尽管没有错误)的输入报文数，并且这些报文不会被传递给上层网络协议");
    public static final Mib IfInErrors                   = put("ifInErrors", IfEntry.child(14), "Counter", AccessMode.Read, "流入的错误报文数，由于错误使得这些报文不会被传递给上层网络协议");
    public static final Mib IfInUnknownProtos            = put("ifInUnknownProtos", IfEntry.child(15), "Counter", AccessMode.Read, "由于未知或不支持的网络协议而丢弃的输入报文的数量");
    public static final Mib IfOutOctets                  = put("ifOutOctets", IfEntry.child(16), "Counter", AccessMode.Read, "该接口上发送的字节数。该字节数也包括数据链路组帧的字节");
    public static final Mib IfOutUcastPkts               = put("ifOutUcastPkts", IfEntry.child(17), "Counter", AccessMode.Read, "上层协议(如IP)需要发送到一个网络单播地址的报文数。该数量包括丢弃的或未发送的报文数");
    public static final Mib IfOutNUcastPkts              = put("ifOutNUcastPkts", IfEntry.child(18), "Counter", AccessMode.Read, "上层协议(如IP)需要发送到一个非单播地址的报文数。该数量包括丢弃的或因为某种原因未发送的报文数");
    public static final Mib IfOutDiscards                = put("ifOutDiscards", IfEntry.child(19), "Counter", AccessMode.Read, "由于某种与特定错误条件无关的原因，而不能发送的报文数。例如，可能由报文TTL超时导致");
    public static final Mib IfOutErrors                  = put("ifOutErrors", IfEntry.child(20), "Counter", AccessMode.Read, "由于错误而不能发送的报文数量");
    public static final Mib IfOutQlen                    = put("ifOutQlen", IfEntry.child(21), "Gauge", AccessMode.Read, "该设备上的输出报文队列长度");
    public static final Mib IfSpecific                   = put("ifSpecific", IfEntry.child(22), "ObjectIdentifier", AccessMode.Read, "MIB引用定义，指向一个用于实现该网络接口的特定介质类型");
    /**地址转换组 (.1.3.6.1.2.1.3) (!反对使用!)*/
    public static final Mib AT                           = put("at", new int[] {1, 3, 6, 1, 2, 1, 3}, "OctetString", AccessMode.All, "[地址转换组]");
    public static final Mib atTable                      = put("atTable", AT.child(1), "Sequence.atEntry", AccessMode.Read, "网络地址和物理(数据链路)地址之间的一个映射");
    public static final Mib atEntry                      = put("atEntry", atTable.child(1), "Sequence", AccessMode.Read, "包含下面列出的其余对象的一个特定映射");
    public static final Mib atIfIndex                    = put("atIfIndex", atEntry.child(1), "Integer", AccessMode.All, "指向每个特定映射");
    public static final Mib atPhysAddress                = put("atPhysAddress", atEntry.child(2), "PhysAddress", AccessMode.All, "介质相关的物理地址(是一个有效的IP地址)");
    public static final Mib atNetAddress                 = put("atNetAddress", atEntry.child(3), "NetworkAddress", AccessMode.All, "介质相关物理地址所关联的IP地址");
    /**网际协议组 (.1.3.6.1.2.1.4)*/
    public static final Mib IP                           = put("ip", new int[] {1, 3, 6, 1, 2, 1, 4}, "OctetString", AccessMode.All, "[网际协议组]");
    public static final Mib IpForwarding                 = put("ipForwarding", IP.child(1), "Integer", AccessMode.All, "指出系统是否作为一个IP网关(路由器)或者仅作为一个不提供转发服务的正规主机。可取的值有Forwarding(1)和notForwarding(2)");
    public static final Mib IpDefaultTTL                 = put("ipDefaultTTL", IP.child(2), "Integer", AccessMode.All, "置于IP报文的TTL字段中的生存期值");
    public static final Mib IpInReceives                 = put("ipInReceives", IP.child(3), "Counter", AccessMode.Read, "从系统所有可操作接口接收的输入报文的总数");
    public static final Mib IpInHdrError                 = put("ipInHdrError", IP.child(4), "Counter", AccessMode.Read, "由于IP报文头部错误而丢弃的输入报文数量");
    public static final Mib IpInaddrErrors               = put("ipInaddrErrors", IP.child(5), "Counter", AccessMode.Read, "对该系统来说，因为最终IP目的地址无效而被丢弃的输入报文数量");
    public static final Mib IpForwDatagrams              = put("ipForwDatagrams", IP.child(6), "Counter", AccessMode.Read, "本地系统作为网关或路由器试图转发的报文数量");
    public static final Mib IpInUnknownProtos            = put("ipInUnknownProtos", IP.child(7), "NetworkAddress", AccessMode.Read, "从网络上成功接收，但由于系统对报文所请求的网络层协议不支持或者未知，而丢弃的报文数量");
    public static final Mib IpInDiscards                 = put("ipInDiscards", IP.child(8), "Counter", AccessMode.Read, "由于缺乏缓冲空间或其他与报文自身无关的条件，而丢弃的输入报文的数量");
    public static final Mib IpInDelivers                 = put("ipInDelivers", IP.child(9), "Counter", AccessMode.Read, "成功传递给上层协议的输入报文的数量");
    public static final Mib IpOutRequests                = put("ipOutRequests", IP.child(10), "Counter", AccessMode.Read, "上层协议为发送而传递给IP协议的IP报文的数量");
    public static final Mib IpOutDiscards                = put("ipOutDiscards", IP.child(11), "Counter", AccessMode.Read, "由于缺乏缓冲空间或其他与报文自身无关的条件，而丢弃的输出报文的数量");
    public static final Mib IpOutNoRoutes                = put("ipOutNoRoutes", IP.child(12), "Counter", AccessMode.Read, "因为没有路由到所需目标网络，而丢弃的报文数量");
    public static final Mib IpReasmTimeout               = put("ipReasmTimeout", IP.child(13), "Counter", AccessMode.Read, "输入的IP分组报文在它们被重组之前保留的时间间隔(以秒为单位)");
    public static final Mib IpReasmReqds                 = put("ipReasmReqds", IP.child(14), "Counter", AccessMode.Read, "接收到的必须重组的IP分组报文数量");
    public static final Mib IpReasmOKs                   = put("ipReasmOKs", IP.child(15), "Counter", AccessMode.Read, "成功重组的IP分组报文的数量");
    public static final Mib IpReasmFails                 = put("ipReasmFails", IP.child(16), "Counter", AccessMode.Read, "检测到的重组失败的数量");
    public static final Mib IpFragOK                     = put("ipFragOK", IP.child(17), "Counter", AccessMode.Read, "已经被成功分组的报文数量");
    public static final Mib IpFragsFails                 = put("ipFragsFails", IP.child(18), "Counter", AccessMode.Read, "因为IP头部包含不分组标志，使得没有分组的报文数量");
    public static final Mib IpFragsCreates               = put("ipFragsCreates", IP.child(19), "Counter", AccessMode.Read, "该系统上产生的IP报文分组的数量");
    public static final Mib IpAddrTable                  = put("ipAddrTable", IP.child(20), "Sequence.ipAddrEntry", AccessMode.None, "有关系统的IP地址的地址信息表");
    public static final Mib IpRouteTable                 = put("ipRouteTable", IP.child(21), "Sequence.ipRouteEntry", AccessMode.None, "到特定目标的路由");
    public static final Mib IpNetToMediaTable            = put("ipNetToMediaTable", IP.child(22), "Sequence.ipNetToMediaEntry", AccessMode.None, "IP地址和数据链路地址之间的映射");
    public static final Mib IpRoutingDiscards            = put("ipRoutingDiscards", IP.child(23), "Counter", AccessMode.Read, "尽管事实上有效，但被丢弃的报文数量");
    public static final Mib IpAddrEntry                  = put("ipAddrEntry", IpAddrTable.child(1), "Sequence", AccessMode.Read, "实体对象信息表");
    public static final Mib IpAdEntAddr                  = put("ipAdEntAddr", IpAddrEntry.child(1), "IpAddress", AccessMode.Read, "实体的IP地址");
    public static final Mib IpAdEntIndex                 = put("ipAdEntIndex", IpAddrEntry.child(2), "Integer", AccessMode.Read, "唯一标识该实体所对应接口的索引值");
    public static final Mib IpAdEntNetMask               = put("ipAdEntNetMask", IpAddrEntry.child(3), "IpAddress", AccessMode.Read, "与该实体的IP地址相关联的子网掩码");
    public static final Mib IpAdEntBcastAddr             = put("ipAdEntBcastAddr", IpAddrEntry.child(4), "Integer", AccessMode.Read, "与该实体的IP地址相对应的广播地址");
    public static final Mib IpAdEntReasmMaxSize          = put("ipAdEntReasmMaxSize", IpAddrEntry.child(5), "Integer", AccessMode.Read, "该实体所能重组的最大输入IP数据报大小");
    public static final Mib IpRouteEntry                 = put("ipRouteEntry", IpRouteTable.child(1), "Sequence", AccessMode.None, "到特定目标的路由");
    public static final Mib IpRouteDest                  = put("ipRouteDest", IpRouteEntry.child(1), "IpAddress", AccessMode.All, "该路由定义的目标IP地址");
    public static final Mib IpRouteIfIndex               = put("ipRouteIfIndex", IpRouteEntry.child(2), "Integer", AccessMode.All, "标识符本地接口的索引，可通过该接口到达该路由的下一跳");
    public static final Mib IpRouteMetric1               = put("ipRouteMetric1", IpRouteEntry.child(3), "Integer", AccessMode.All, "该路由的主路由选择度量");
    public static final Mib IpRouteMetric2               = put("ipRouteMetric2", IpRouteEntry.child(4), "Integer", AccessMode.All, "该路由的一个可选路由选择度量");
    public static final Mib IpRouteMetric3               = put("ipRouteMetric3", IpRouteEntry.child(5), "Integer", AccessMode.All, "该路由的一个可选路由选择度量");
    public static final Mib IpRouteMetric4               = put("ipRouteMetric4", IpRouteEntry.child(6), "Integer", AccessMode.All, "该路由的一个可选路由选择度量");
    public static final Mib IpRouteNextHop               = put("ipRouteNextHop", IpRouteEntry.child(7), "IpAddress", AccessMode.All, "该路由下一跳的IP地址");
    public static final Mib IpRouteType                  = put("ipRouteType", IpRouteEntry.child(8), "Integer", AccessMode.All, "该路由的类型标识符。有效的类型包括：other(1)、invalid(2)、direct(3)和indirect(4)");
    public static final Mib IpRouteProto                 = put("ipRouteProto", IpRouteEntry.child(9), "Integer", AccessMode.All, "用于了解路由的路由选择机制");
    public static final Mib IpRouteAge                   = put("ipRouteAge", IpRouteEntry.child(10), "IpAddress", AccessMode.All, "从路由被更新或验证以来经过的时间(秒)");
    public static final Mib IpRouteMask                  = put("ipRouteMask", IpRouteEntry.child(11), "IpAddress", AccessMode.All, "目标地址使用(与)的掩码地址");
    public static final Mib IpRouteMetric5               = put("ipRouteMetric5", IpRouteEntry.child(12), "Integer", AccessMode.All, "该路由的一个可选路由选择度量");
    public static final Mib IpRouteInfo                  = put("ipRouteInfo", IpRouteEntry.child(13), "ObjectIdentifier", AccessMode.Read, "MIB定义指向负责该路由的路由选择协议");
    public static final Mib IpNetToMediaEntry            = put("ipNetToMediaEntry", IpNetToMediaTable.child(1), "Sequence", AccessMode.All, "IpNetToMediaEntry信息表");
    public static final Mib IpNetToMediaIfIndex          = put("ipNetToMediaIfIndex", IpNetToMediaEntry.child(1), "Integer", AccessMode.All, "用来标识符本地接口的索引，网络地址和物理地址映射是从该接口获得的");
    public static final Mib IpNetToMediaPhysAddress      = put("ipNetToMediaPhysAddress", IpNetToMediaEntry.child(2), "PhysAddress", AccessMode.All, "介质相关的物理地址；例如，Ethernet地址");
    public static final Mib IpNetToMediaNetAddress       = put("ipNetToMediaNetAddress", IpNetToMediaEntry.child(3), "IpAddress", AccessMode.All, "对应该介质相关物理地址的IP地址");
    public static final Mib IpNetToMediaType             = put("ipNetToMediaType", IpNetToMediaEntry.child(4), "Counter", AccessMode.All, "产生地址映射的类型。类型包括：other(1)、invalid(2)、dynamic(3)和static(4)");
    /**ICMP组 (.1.3.6.1.2.1.5)*/
    public static final Mib ICMP                         = put("ICMP", new int[] {1, 3, 6, 1, 2, 1, 5}, "OctetString", AccessMode.All, "[ICMP组]");
    public static final Mib IcmpInMsgs                   = put("icmpInMsgs", ICMP.child(1), "Counter", AccessMode.Read, "接收到的ICMP消息的数量");
    public static final Mib IcmpInErrors                 = put("icmpInErrors", ICMP.child(2), "Counter", AccessMode.Read, "接收到的包含某些ICMP指定错误的ICMP消息的数量");
    public static final Mib IcmpInDestUnreachs           = put("icmpInDestUnreachs", ICMP.child(3), "Counter", AccessMode.Read, "接收到的ICMP目标不可达消息的数量");
    public static final Mib IcmpInTimeExcds              = put("icmpInTimeExcds", ICMP.child(4), "Counter", AccessMode.Read, "接收到的ICMP超时消息的数量");
    public static final Mib IcmpInParmProbs              = put("icmpInParmProbs", ICMP.child(5), "Counter", AccessMode.Read, "接收到的ICMP参数错误消息的数量");
    public static final Mib IcmpInSrcQuenchs             = put("icmpInSrcQuenchs", ICMP.child(6), "Counter", AccessMode.Read, "接收到的ICMP源端抑制消息的数量");
    public static final Mib IcmpInRedirects              = put("icmpInRedirects", ICMP.child(7), "Counter", AccessMode.Read, "接收到的ICMP重定向消息的数量");
    public static final Mib IcmpInEchos                  = put("icmpInEchos", ICMP.child(8), "Counter", AccessMode.Read, "接收到的ICMP回应请求消息的数量");
    public static final Mib IcmpInEchoReps               = put("icmpInEchoReps", ICMP.child(9), "Counter", AccessMode.Read, "接收到的ICMP回应应答消息的数量");
    public static final Mib IcmpInTimestamps             = put("icmpInTimestamps", ICMP.child(10), "Counter", AccessMode.Read, "接收到的ICMP时间戳请求消息的数量");
    public static final Mib IcmpInTimestampReps          = put("icmpInTimestampReps", ICMP.child(11), "Counter", AccessMode.Read, "接收到的ICMP时间戳应答消息的数量");
    public static final Mib IcmpInAddrMasks              = put("icmpInAddrMasks", ICMP.child(12), "Counter", AccessMode.Read, "接收到的ICMP地址掩码请求消息的数量");
    public static final Mib IcmpAddrMasksReps            = put("icmpAddrMasksReps", ICMP.child(13), "Counter", AccessMode.Read, "接收到的ICMP地址掩码应答消息的数量");
    public static final Mib IcmpOutMsgs                  = put("icmpOutMsgs", ICMP.child(14), "Counter", AccessMode.Read, "该系统试图发送的ICMP消息的数量");
    public static final Mib IcmpOutErrors                = put("icmpOutErrors", ICMP.child(15), "Counter", AccessMode.Read, "该系统由于ICMP错误而不能发送的ICMP消息的数量");
    public static final Mib IcmpOutDestUnreachs          = put("icmpOutDestUnreachs", ICMP.child(16), "Counter", AccessMode.Read, "设备发送的ICMP目的不可达到的消息的数量");
    public static final Mib IcmpOutTimeExcds             = put("icmpOutTimeExcds", ICMP.child(17), "Counter", AccessMode.Read, "该系统发送的ICMP超时消息的数量");
    public static final Mib IcmpOutParmProbs             = put("icmpOutParmProbs", ICMP.child(18), "Counter", AccessMode.Read, "该系统发送的ICMP参数错误消息的数量");
    public static final Mib IcmpOutSrcQuenchs            = put("icmpOutSrcQuenchs", ICMP.child(19), "Counter", AccessMode.Read, "该系统发送的ICMP源端抑制消息的数量");
    public static final Mib IcmpOutRedirects             = put("icmpOutRedirects", ICMP.child(20), "Counter", AccessMode.Read, "该系统发送的ICMP重定向消息的数量");
    public static final Mib IcmpOutEchos                 = put("icmpOutEchos", ICMP.child(21), "Counter", AccessMode.Read, "该系统发送的ICMP回应请求消息的数量");
    public static final Mib IcmpOutEchoReps              = put("icmpOutEchoReps", ICMP.child(22), "Counter", AccessMode.Read, "该系统发送的ICMP回应应答消息的数量");
    public static final Mib IcmpOutTimestamps            = put("icmpOutTimestamps", ICMP.child(23), "Counter", AccessMode.Read, "该系统发送的ICMP时间戳请求消息的数量");
    public static final Mib IcmpOutTimestampReps         = put("icmpOutTimestampReps", ICMP.child(24), "Counter", AccessMode.Read, "该系统发送的ICMP时间戳应答消息的数量");
    public static final Mib IcmpOutAddrMasks             = put("icmpOutAddrMasks", ICMP.child(25), "Counter", AccessMode.Read, "该系统发送的ICMP地址掩码请求消息的数量");
    public static final Mib IcmpOutAddrMaskReps          = put("icmpOutAddrMaskReps", ICMP.child(26), "Counter", AccessMode.Read, "该系统发送的ICMP地址掩码应答消息的数量");
    /**TCP组 (.1.3.6.1.2.1.6)*/
    public static final Mib TCP                          = put("TCP", new int[] {1, 3, 6, 1, 2, 1, 6}, "OctetString", AccessMode.All, "[TCP组]");
    public static final Mib TcpRtoAlgorithm              = put("tcpRtoAlgorithm", TCP.child(1), "Counter", AccessMode.Read, "重发时间算法，可能包含的值：other(1)、constant(2)、rsre(3)和vanj(4)");
    public static final Mib TcpRtoMin                    = put("tcpRtoMin", TCP.child(2), "Counter", AccessMode.Read, "重发定时器最小值");
    public static final Mib TcpRtoMax                    = put("tcpRtoMax", TCP.child(3), "Counter", AccessMode.Read, "重发定时器最大值");
    public static final Mib TcpMaxConn                   = put("tcpMaxConn", TCP.child(4), "Counter", AccessMode.Read, "该系统支持TCP连接的总数(限制)");
    public static final Mib TcpActiveOpens               = put("tcpActiveOpens", TCP.child(5), "Counter", AccessMode.Read, "该系统支持的主动打开的连接数量");
    public static final Mib TcpPassiveOpens              = put("tcpPassiveOpens", TCP.child(6), "Counter", AccessMode.Read, "该系统支持的被动打开的连接数量");
    public static final Mib TcpAttemptFails              = put("tcpAttemptFails", TCP.child(7), "Counter", AccessMode.Read, "该系统上发生的试图连接失败的数量");
    public static final Mib TcpEstabResets               = put("tcpEstabResets", TCP.child(8), "Counter", AccessMode.Read, "在该系统上发生的连接复位数量");
    public static final Mib TcpCurrEstab                 = put("tcpCurrEstab", TCP.child(9), "Counter", AccessMode.Read, "处于ESTABLISHED或CLOSE-WAIT状态的TCP连接的数量");
    public static final Mib TcpInSegs                    = put("tcpInSegs", TCP.child(10), "Counter", AccessMode.Read, "接收到的分组数量，包括接收到的错误分组");
    public static final Mib TcpOutSegs                   = put("tcpOutSegs", TCP.child(11), "Counter", AccessMode.Read, "发送的分组数量，但除了那些包含重发的字节的分组");
    public static final Mib TcpRetransSegs               = put("tcpRetransSegs", TCP.child(12), "Counter", AccessMode.Read, "重发分组数量");
    public static final Mib TcpConnTable                 = put("tcpConnTable", TCP.child(13), "Sequence.tcpConnEntry", AccessMode.None, "TCP连接信息");
    public static final Mib TcpConnEntry                 = put("tcpConnEntry", TcpConnTable.child(1), "Sequence", AccessMode.None, "TcpConnEntry信息表");
    public static final Mib TcpConnState                 = put("tcpConnState", TcpConnEntry.child(1), "Integer", AccessMode.All, "TCP连接的状态");
    public static final Mib TcpConnLocalAddress          = put("tcpConnLocalAddress", TcpConnEntry.child(2), "IpAddress", AccessMode.Read, "这个TCP连接的本地IP地址。当连接处于听的状态时，就可以和这个节点相连的任何IP接口建立连接，这时这个值是0.0.0.0");
    public static final Mib TcpConnLocalPort             = put("tcpConnLocalPort", TcpConnEntry.child(3), "Integer", AccessMode.Read, "该TCP连接的本地端口号");
    public static final Mib TcpConnRemAddress            = put("tcpConnRemAddress", TcpConnEntry.child(4), "IpAddress", AccessMode.Read, "该TCP连接的远程IP地址");
    public static final Mib TcpConnRemPort               = put("tcpConnRemPort", TcpConnEntry.child(5), "Integer", AccessMode.Read, "该TCP连接的远程端口号");
    public static final Mib TcpInErrors                  = put("tcpInErrors", TCP.child(14), "Counter", AccessMode.Read, "接收到错误的分组数");
    public static final Mib TcpOutRsts                   = put("tcpOutRsts", TCP.child(15), "Counter", AccessMode.Read, "发送包含RST标志的TCP分组的数量");
    /**UDP组 (.1.3.6.1.2.1.7)*/
    public static final Mib UDP                          = put("UDP", new int[] {1, 3, 6, 1, 2, 1, 7}, "OctetString", AccessMode.All, "[UDP组]");
    public static final Mib UdpInDatagrams               = put("udpInDatagrams", UDP.child(1), "Counter", AccessMode.Read, "传递给上层协议和应用程序的UDP数据报的数量");
    public static final Mib UdpNoPorts                   = put("udpNoPorts", UDP.child(2), "Counter", AccessMode.Read, "接收到没有提供特定应用程序端口的UDP数据报的数量");
    public static final Mib UdpInErrors                  = put("udpInErrors", UDP.child(3), "Counter", AccessMode.Read, "接收到不能被传递的UDP数据报的数量，不能被传递的原因与有效应用程序或目标端口无关");
    public static final Mib UdpOutDatagrams              = put("udpOutDatagrams", UDP.child(4), "Counter", AccessMode.Read, "从该系统发出的UDP数据报的数量");
    public static final Mib UdpTable                     = put("udpTable", UDP.child(5), "Sequence.udpEntry", AccessMode.None, "UDP侦听程序地址和端口信息");
    public static final Mib UdpEntry                     = put("udpEntry", UdpTable.child(1), "Sequence", AccessMode.None, "每个UDP会话的一个udpTable表项");
    public static final Mib UdpLocalAddress              = put("udpLocalAddress", UdpEntry.child(1), "IpAddress", AccessMode.Read, "该UDP侦听程序、服务、或应用程序的本地IP地址");
    public static final Mib UdpLocalPort                 = put("udpLocalPort", UdpEntry.child(2), "Integer", AccessMode.Read, "该UDP侦听程序、服务或应用程序的本地端口(socket)号");
    /**SNMP组 (.1.3.6.1.2.1.11)*/
    public static final Mib SNMP                         = put("SNMP", new int[] {1, 3, 6, 1, 2, 1, 11}, "OctetString", AccessMode.All, "[SNMP组]");
    public static final Mib snmpInPkts                   = put("snmpInPkts", SNMP.child(1), "Counter", AccessMode.Read, "传递给该代理的SNMP消息的数量");
    public static final Mib snmpOutPkts                  = put("snmpOutPkts", SNMP.child(2), "Counter", AccessMode.Read, "该代理发送出去的SNMP消息的数量");
    public static final Mib snmpInBadVersions            = put("snmpInBadVersions", SNMP.child(3), "Counter", AccessMode.Read, "传递给该代理，但该代理不支持的SNMP消息的数量");
    public static final Mib snmpInBadCommunityNames      = put("snmpInBadCommunityNames", SNMP.child(4), "Counter", AccessMode.Read, "传递给该代理的包含未知区名的SNMP消息的数量");
    public static final Mib snmpInBadCommunityUses       = put("snmpInBadCommunityUses", SNMP.child(5), "Counter", AccessMode.Read, "传递给该代理的某种SNMP消息的数量，这种SNMP消息包含一个SNMP操作，而该操作根据提供的区名而不允许执行");
    public static final Mib snmpInASNParseErrs           = put("snmpInASNParseErrs", SNMP.child(6), "Counter", AccessMode.Read, "对输入的SNMP消息解码时，发生BER和ASN错误的数量");
    public static final Mib snmpInTooBigs                = put("snmpInTooBigs", SNMP.child(8), "Counter", AccessMode.Read, "传递给该代理的SNMPPDU发生tooBig错误的数量");
    public static final Mib snmpInNoSuchNames            = put("snmpInNoSuchNames", SNMP.child(9), "Counter", AccessMode.Read, "传递给该代理的SNMP PDUs 中error-status域中是`noSuchName`的错误信息");
    public static final Mib snmpInBadValues              = put("snmpInBadValues", SNMP.child(10), "Counter", AccessMode.Read, "传递给该代理的SNMP PDUs 中error-status域中是`badValue`的错误信息");
    public static final Mib snmpInReadOnlys              = put("snmpInReadOnlys", SNMP.child(11), "Counter", AccessMode.Read, "传递给该代理的SNMP PDUs 中error-status域中是`readOnly`的错误信息");
    public static final Mib snmpInGenErrs                = put("snmpInGenErrs", SNMP.child(12), "Counter", AccessMode.Read, "传递给该代理的SNMP PDUs 中error-status域中是`genErr`的错误信息");
    public static final Mib snmpInTotalReqVars           = put("snmpInTotalReqVars", SNMP.child(13), "Counter", AccessMode.Read, "返回对有效Get-Request and Get-Next PDUs回答的消息总数");
    public static final Mib snmpInTotalSetVars           = put("snmpInTotalSetVars", SNMP.child(14), "Counter", AccessMode.Read, "返回对有效Set-Request PDUs回答的消息总数");
    public static final Mib snmpInGetRequests            = put("snmpInGetRequests", SNMP.child(15), "Counter", AccessMode.Read, "被该代理接收和处理的 Get-Request PDUs 总数");
    public static final Mib snmpInGetNexts               = put("snmpInGetNexts", SNMP.child(16), "Counter", AccessMode.Read, "被该代理接收和处理的 Get-Next PDUs 总数");
    public static final Mib snmpInSetRequests            = put("snmpInSetRequests", SNMP.child(17), "Counter", AccessMode.Read, "由该代理接受并处理的SNMPset-requests的数量");
    public static final Mib snmpInGetResponses           = put("snmpInGetResponses", SNMP.child(18), "Counter", AccessMode.Read, "被该代理接收和处理的 Get-Response PDUs 总数");
    public static final Mib snmpInTraps                  = put("snmpInTraps", SNMP.child(19), "Counter", AccessMode.Read, "别该代理接收和处理的 Trap PDUs 总数");
    public static final Mib snmpOutTooBig                = put("snmpOutTooBig", SNMP.child(20), "Counter", AccessMode.Read, "由该代理产生的SNMPPDU发生tooBig错误的数量");
    public static final Mib snmpOutNoSuchNames           = put("snmpOutNoSuchNames", SNMP.child(21), "Counter", AccessMode.Read, "由该代理产生的 SNMP PDUs 发生noSuchName错误的数量");
    public static final Mib snmpOutBadValues             = put("snmpOutBadValues", SNMP.child(22), "Counter", AccessMode.Read, "由该代理产生的 SNMP PDUs 出现badValue错误的数量");
    public static final Mib snmpOutGenErrs               = put("snmpOutGenErrs", SNMP.child(24), "Counter", AccessMode.Read, "由该代理产生的 SNMP PDUs 发生genErr错误的数量");
    public static final Mib snmpOutGetRequests           = put("snmpOutGetRequests", SNMP.child(25), "Counter", AccessMode.Read, "由该代理产生的 Get-Request PDUs 总数");
    public static final Mib snmpOutGetNexts              = put("snmpOutGetNexts", SNMP.child(26), "Counter", AccessMode.Read, "由该代理产生的 Get-Next PDUs 总数");
    public static final Mib snmpOutSetRequests           = put("snmpOutSetRequests", SNMP.child(27), "Counter", AccessMode.Read, "由该代理产生的 Set-Request PDUs 总数");
    public static final Mib snmpOutGetResponses          = put("snmpOutGetResponses", SNMP.child(28), "Counter", AccessMode.Read, "由该代理产生的 Get-Response PDUs 总数");
    public static final Mib snmpOutTraps                 = put("snmpOutTraps", SNMP.child(29), "Counter", AccessMode.Read, "由该代理产生的 Trap PDUs 总数");
    public static final Mib snmpEnableAuthenTraps        = put("snmpEnableAuthenTraps", SNMP.child(30), "Integer", AccessMode.All, "指定该代理是否允许产生认证错误的traps，有两个值enabled和disabled");
    public static final Mib snmpSilentDrops              = put("snmpSilentDrops", SNMP.child(31), "Counter", AccessMode.Read, "传递给该代理但是被丢弃的GetRequest-PDUs, GetNextRequest-PDUs, GetBulkRequest-PDUs, SetRequest-PDUs, and InformRequest-PDUs 总数。因为要求回答的包大小大于限制的值和最大的消息大小");
    public static final Mib snmpProxyDrops               = put("snmpProxyDrops", SNMP.child(32), "Counter", AccessMode.Read,  "传递给该代理但是被丢弃的GetRequest-PDUs, GetNextRequest-PDUs, GetBulkRequest-PDUs, SetRequest-PDUs, and InformRequest-PDUs 总数。Because the transmission of the (possibly translated) message to a proxy target failed in a manner (other than a time-out) such that no Response-PDU could be returned.");
    /**IfMIB (.1.3.6.1.2.1.31)*/
    public static final Mib IfMIB                        = put("ifMIB", new int[] {1, 3, 6, 1, 2, 1, 31}, "OctetString", AccessMode.Read, "[ifMIB MODULE-IDENTITY]");
    public static final Mib IfMIBObjects                 = put("ifMIBObjects", IfMIB.child(1), "OctetString", AccessMode.Read, "");
    public static final Mib IfXTable                     = put("ifXTable", IfMIBObjects.child(1), "Sequence.ifXEntry", AccessMode.Read, "");
    public static final Mib IfXEntry                     = put("ifXEntry", IfXTable.child(1), "Sequence", AccessMode.Read, "");
    public static final Mib IfName                       = put("ifName", IfXEntry.child(1), "OctetString", AccessMode.Read, "");
    public static final Mib IfInMulticastPkts            = put("ifInMulticastPkts", IfXEntry.child(2), "Counter", AccessMode.Read, "");
    public static final Mib IfInBroadcastPkts            = put("ifInBroadcastPkts", IfXEntry.child(3), "Counter", AccessMode.Read, "");
    public static final Mib IfOutMulticastPkts           = put("ifOutMulticastPkts", IfXEntry.child(4), "Counter", AccessMode.Read, "");
    public static final Mib IfOutBroadcastPkts           = put("ifOutBroadcastPkts", IfXEntry.child(5), "Counter", AccessMode.Read, "");
    public static final Mib IfHCInOctets                 = put("ifHCInOctets", IfXEntry.child(6), "OctetString", AccessMode.Read, "");
    public static final Mib IfHCInUcastPkts              = put("ifHCInUcastPkts", IfXEntry.child(7), "Counter", AccessMode.Read, "");
    public static final Mib IfHCInMulticastPkts          = put("ifHCInMulticastPkts", IfXEntry.child(8), "Counter", AccessMode.Read, "");
    public static final Mib IfHCInBroadcastPkts          = put("ifHCInBroadcastPkts", IfXEntry.child(9), "Counter", AccessMode.Read, "");
    public static final Mib IfHCOutOctets                = put("ifHCOutOctets", IfXEntry.child(10), "OctetString", AccessMode.Read, "");
    public static final Mib IfHCOutUcastPkts             = put("ifHCOutUcastPkts", IfXEntry.child(11), "Counter", AccessMode.Read, "");
    public static final Mib IfHCOutMulticastPkts         = put("ifHCOutMulticastPkts", IfXEntry.child(12), "Counter", AccessMode.Read, "");
    public static final Mib IfHCOutBroadcastPkts         = put("ifHCOutBroadcastPkts", IfXEntry.child(13), "Counter", AccessMode.Read, "");
    public static final Mib IfLinkUpDownTrapEnable       = put("ifLinkUpDownTrapEnable", IfXEntry.child(14), "Integer", AccessMode.Read, "");
    public static final Mib IfHighSpeed                  = put("ifHighSpeed", IfXEntry.child(15), "Gauge", AccessMode.Read, "");
    public static final Mib IfPromiscuousMode            = put("ifPromiscuousMode", IfXEntry.child(16), "Integer", AccessMode.Read, "");
    public static final Mib IfConnectorPresent           = put("ifConnectorPresent", IfXEntry.child(17), "Integer", AccessMode.Read, "");
    public static final Mib IfAlias                      = put("ifAlias", IfXEntry.child(18), "OctetString", AccessMode.Read, "");
    public static final Mib IfCounterDiscontinuityTime   = put("ifCounterDiscontinuityTime", IfXEntry.child(19), "TimeStamp", AccessMode.Read, "");
    public static final Mib IfStackTable                 = put("ifStackTable", IfMIBObjects.child(2), "Sequence.ifStackEntry", AccessMode.Read, "");
    public static final Mib IfStackEntry                 = put("ifStackEntry", IfStackTable.child(1), "Sequence", AccessMode.Read, "");
    public static final Mib IfStackHigherLayer           = put("ifStackHigherLayer", IfStackEntry.child(1), "Counter", AccessMode.Read, "");
    public static final Mib IfStackLowerLayer            = put("ifStackLowerLayer", IfStackEntry.child(2), "Counter", AccessMode.Read, "");
    public static final Mib IfStackStatus                = put("ifStackStatus", IfStackEntry.child(3), "Integer", AccessMode.Read, "");
    public static final Mib IfTestTable                  = put("ifTestTable", IfMIBObjects.child(3), "Sequence.ifTestEntry", AccessMode.Read, "");
    public static final Mib IfTestEntry                  = put("ifTestEntry", IfTestTable.child(1), "Sequence", AccessMode.Read, "");
    public static final Mib IfTestId                     = put("ifTestId", IfTestEntry.child(1), "ObjectIdentifier", AccessMode.Read, "");
    public static final Mib IfTestStatus                 = put("ifTestStatus", IfTestEntry.child(2), "Integer", AccessMode.Read, "");
    public static final Mib IfTestType                   = put("ifTestType", IfTestEntry.child(3), "Integer", AccessMode.Read, "");
    public static final Mib IfTestResult                 = put("ifTestResult", IfTestEntry.child(4), "Integer", AccessMode.Read, "");
    public static final Mib IfTestCode                   = put("ifTestCode", IfTestEntry.child(5), "Integer", AccessMode.Read, "");
    public static final Mib IfTestOwner                  = put("ifTestOwner", IfTestEntry.child(6), "OctetString", AccessMode.Read, "");
    public static final Mib IfRcvAddressTable            = put("ifRcvAddressTable", IfMIBObjects.child(4), "Sequence.ifRcvAddressEntry", AccessMode.Read, "");
    public static final Mib IfRcvAddressEntry            = put("ifRcvAddressEntry", IfRcvAddressTable.child(1), "Sequence", AccessMode.Read, "");
    public static final Mib IfRcvAddressAddress          = put("ifRcvAddressAddress", IfRcvAddressEntry.child(1), "IpAddress", AccessMode.Read, "");
    public static final Mib IfRcvAddressStatus           = put("ifRcvAddressStatus", IfRcvAddressEntry.child(2), "Integer", AccessMode.Read, "");
    public static final Mib IfRcvAddressType             = put("ifRcvAddressType", IfRcvAddressEntry.child(3), "Integer", AccessMode.Read, "");
    public static final Mib IfTableLastChange            = put("ifTableLastChange", IfMIBObjects.child(5), "TimeTicks", AccessMode.Read, "");
    public static final Mib IfStackLastChange            = put("ifStackLastChange", IfMIBObjects.child(6), "TimeTicks", AccessMode.Read, "");
    public static final Mib IfConformance                = put("ifConformance", IfMIB.child(2), "Sequence.ifGroups", AccessMode.Read, "");
    public static final Mib IfGroups                     = put("ifGroups", IfConformance.child(1), "Sequence", AccessMode.Read, "");
    public static final Mib IfGeneralGroup               = put("ifGeneralGroup", IfGroups.child(1), "Integer", AccessMode.Read, "");
    public static final Mib IfFixedLengthGroup           = put("ifFixedLengthGroup", IfGroups.child(2), "Integer", AccessMode.Read, "");
    public static final Mib IfHCFixedLengthGroup         = put("ifHCFixedLengthGroup", IfGroups.child(3), "Integer", AccessMode.Read, "");
    public static final Mib IfPacketGroup                = put("ifPacketGroup", IfGroups.child(4), "Integer", AccessMode.Read, "");
    public static final Mib IfHCPacketGroup              = put("ifHCPacketGroup", IfGroups.child(5), "Integer", AccessMode.Read, "");
    public static final Mib IfVHCPacketGroup             = put("ifVHCPacketGroup", IfGroups.child(6), "Integer", AccessMode.Read, "");
    public static final Mib IfRcvAddressGroup            = put("ifRcvAddressGroup", IfGroups.child(7), "Integer", AccessMode.Read, "");
    public static final Mib IfTestGroup                  = put("ifTestGroup", IfGroups.child(8), "Integer", AccessMode.Read, "");
    public static final Mib IfStackGroup                 = put("ifStackGroup", IfGroups.child(9), "Integer", AccessMode.Read, "");
    public static final Mib IfGeneralInformationGroup    = put("ifGeneralInformationGroup", IfGroups.child(10), "Integer", AccessMode.Read, "");
    public static final Mib IfStackGroup2                = put("ifStackGroup2", IfGroups.child(11), "Integer", AccessMode.Read, "");
    public static final Mib IfOldObjectsGroup            = put("ifOldObjectsGroup", IfGroups.child(12), "Integer", AccessMode.Read, "");
    public static final Mib IfCounterDiscontinuityGroup  = put("ifCounterDiscontinuityGroup", IfGroups.child(13), "Integer", AccessMode.Read, "");
    public static final Mib LinkUpDownNotificationsGroup = put("linkUpDownNotificationsGroup", IfGroups.child(14), "Integer", AccessMode.Read, "");
    public static final Mib IfCompliances                = put("ifCompliances", IfConformance.child(2), "Sequence", AccessMode.Read, "");
    public static final Mib IfCompliance                 = put("ifCompliance", IfCompliances.child(1), "Integer", AccessMode.Read, "");
    public static final Mib IfCompliance2                = put("ifCompliance2", IfCompliances.child(2), "Integer", AccessMode.Read, "");
    public static final Mib IfCompliance3                = put("ifCompliance3", IfCompliances.child(3), "Integer", AccessMode.Read, "");

    /*private*/
    public static final Mib HrMemorySize             = put("hrMemorySize", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 2}, "Counter", AccessMode.Read, "获取内存大小");
    public static final Mib HrStorageIndex           = put("hrStorageIndex", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 1}, "Integer", AccessMode.Read, "存储设备编号");
    public static final Mib HrStorageType            = put("hrStorageType", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 2}, "Integer", AccessMode.Read, "存储设备类型");
    public static final Mib HrStorageDescr           = put("hrStorageDescr", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 3}, "OctetString", AccessMode.Read, "存储设备描述");
    public static final Mib HrStorageAllocationUnits = put("hrStorageAllocationUnits", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 4}, "Integer", AccessMode.Read, "簇的大小");
    public static final Mib HrStorageSize            = put("hrStorageSize", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 5}, "Counter", AccessMode.Read, "簇的的数目");
    public static final Mib HrStorageUsed            = put("hrStorageUsed", new int[] {1, 3, 6, 1, 2, 1, 25, 2, 3, 1, 6}, "Counter", AccessMode.Read, "使用多少，跟总容量相除就是占用率");
    public static final Mib HrProcessorLoad          = put("hrProcessorLoad", new int[] {1, 3, 6, 1, 2, 1, 25, 3, 3, 1, 2}, "Counter", AccessMode.Read, "CPU的当前负载，N个核就有N个负载");
    public static final Mib MemTotalSwap             = put("memTotalSwap", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 3}, "Counter", AccessMode.Read, "Total Swap Size(虚拟内存)");
    public static final Mib MemAvailSwap             = put("memAvailSwap", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 4}, "Counter", AccessMode.Read, "Available Swap Space");
    public static final Mib MemTotalReal             = put("memTotalReal", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 5}, "Counter", AccessMode.Read, "Total RAM in machine");
    public static final Mib MemAvailReal             = put("memAvailReal", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 6}, "Counter", AccessMode.Read, "Total RAM used");
    public static final Mib MemTotalFree             = put("memTotalFree", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 11}, "Counter", AccessMode.Read, "Total RAM Free");
    public static final Mib MemShared                = put("memShared", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 13}, "Counter", AccessMode.Read, "Total RAM Shared");
    public static final Mib MemBuffer                = put("memBuffer", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 14}, "Counter", AccessMode.Read, "Total RAM Buffered");
    public static final Mib MemCached                = put("memCached", new int[] {1, 3, 6, 1, 4, 1, 2021, 4, 15}, "Counter", AccessMode.Read, "Total Cached Memory");
    public static final Mib DskPath                  = put("dskPath", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 2}, "OctetString", AccessMode.Read, "Path where the disk is mounted");
    public static final Mib DskDevice                = put("dskDevice", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 3}, "OctetString", AccessMode.Read, "Path of the device for the partition");
    public static final Mib DskTotal                 = put("dskTotal", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 6}, "Counter", AccessMode.Read, "Total size of the disk/partion (kBytes)");
    public static final Mib DskAvail                 = put("dskAvail", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 7}, "Counter", AccessMode.Read, "Available space on the disk");
    public static final Mib DskUsed                  = put("dskUsed", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 8}, "Counter", AccessMode.Read, "Used space on the disk");
    public static final Mib DskPercent               = put("dskPercent", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 9}, "Counter", AccessMode.Read, "Percentage of space used on disk");
    public static final Mib DskPercentNode           = put("dskPercentNode", new int[] {1, 3, 6, 1, 4, 1, 2021, 9, 1, 10}, "OctetString", AccessMode.Read, "Percentage of inodes used on disk");
    public static final Mib Load5                    = put("Load5", new int[] {1, 3, 6, 1, 4, 1, 2021, 10, 1, 3, 1}, "Integer", AccessMode.Read, "Load5");
    public static final Mib Load10                   = put("Load10", new int[] {1, 3, 6, 1, 4, 1, 2021, 10, 1, 3, 2}, "Integer", AccessMode.Read, "Load10");
    public static final Mib Load15                   = put("Load15", new int[] {1, 3, 6, 1, 4, 1, 2021, 10, 1, 3, 3}, "Integer", AccessMode.Read, "Load15");
    public static final Mib SsSwapIn                 = put("ssSwapIn", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 3}, "Counter", AccessMode.Read, "ssSwapIn");
    public static final Mib SsSwapOut                = put("SsSwapOut", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 4}, "Counter", AccessMode.Read, "SsSwapOut");
    public static final Mib SsIOSent                 = put("ssIOSent", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 5}, "Counter", AccessMode.Read, "ssIOSent");
    public static final Mib SsIOReceive              = put("ssIOReceive", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 6}, "Counter", AccessMode.Read, "ssIOReceive");
    public static final Mib SsSysInterrupts          = put("ssSysInterrupts", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 7}, "Counter", AccessMode.Read, "ssSysInterrupts");
    public static final Mib SsSysContext             = put("ssSysContext", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 8}, "Counter", AccessMode.Read, "ssSysContext");
    public static final Mib SsCpuUser                = put("ssCpuUser", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 9}, "OctetString", AccessMode.Read, "用户CPU百分比");
    public static final Mib SsCpuSystem              = put("ssCpuSystem", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 10}, "OctetString", AccessMode.Read, "系统CPU百分比");
    public static final Mib SsCpuIdle                = put("ssCpuIdle", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 11}, "Counter", AccessMode.Read, "空闲CPU百分比");
    public static final Mib SsCpuRawUser             = put("ssCpuRawUser", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 50}, "OctetString", AccessMode.Read, "原始用户CPU使用时间");
    public static final Mib SsCpuRawNice             = put("ssCpuRawNice", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 51}, "OctetString", AccessMode.Read, "原始nice占用时间");
    public static final Mib SsCpuRawSystem           = put("ssCpuRawSystem", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 52}, "OctetString", AccessMode.Read, "原始系统CPU使用时间");
    public static final Mib SsCpuRawIdle             = put("ssCpuRawIdle", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 53}, "Counter", AccessMode.Read, "原始CPU空闲时间");
    public static final Mib SsCpuRawWait             = put("ssCpuRawWait", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 54}, "Counter", AccessMode.Read, "ssCpuRawWait");
    public static final Mib SsCpuRawInterrupt        = put("ssCpuRawInterrupt", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 56}, "Counter", AccessMode.Read, "ssCpuRawInterrupt");
    public static final Mib SsIORawSent              = put("ssIORawSent", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 57}, "Counter", AccessMode.Read, "ssIORawSent");
    public static final Mib SsIORawReceived          = put("ssIORawReceived", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 58}, "Counter", AccessMode.Read, "ssIORawReceived");
    public static final Mib SsRawInterrupts          = put("ssRawInterrupts", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 59}, "Counter", AccessMode.Read, "ssRawInterrupts");
    public static final Mib SsRawContexts            = put("ssRawContexts", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 60}, "Counter", AccessMode.Read, "ssRawContexts");
    public static final Mib SsCpuRawSoftIRQ          = put("ssCpuRawSoftIRQ", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 61}, "Counter", AccessMode.Read, "ssCpuRawSoftIRQ");
    public static final Mib SsRawSwapIn              = put("ssRawSwapIn", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 62}, "Counter", AccessMode.Read, "ssRawSwapIn");
    public static final Mib SsRawSwapOut             = put("ssRawSwapOut", new int[] {1, 3, 6, 1, 4, 1, 2021, 11, 63}, "Counter", AccessMode.Read, "ssRawSwapOut");
    public static final Mib LinkDown                 = put("linkDown", new int[] {1, 3, 6, 1, 6, 3, 1, 1, 5, 3}, "Integer", AccessMode.Read, "");
    public static final Mib LinkUp                   = put("linkUp", new int[] {1, 3, 6, 1, 6, 3, 1, 1, 5, 4}, "Integer", AccessMode.Read, "");
    public static final Mib IfAC                     = put("ifAC", new int[] {1, 3, 6, 1, 4, 1, 318, 1, 1, 26, 6, 3, 1, 5, 1}, "OctetString", AccessMode.Read, "");

    // VariableFormatter
    static {
        java.lang.System.out.println("ⓘ Mib2Library.mib.size = " + mibs.size());
        maps.put(IfType.getName(), new IfTypeVF());
        maps.put(IfAdminStatus.getName(), new IfStatusVF());
        maps.put(IfOperStatus.getName(), new IfStatusVF());
        maps.put(IpForwarding.getName(), new IpForwardingVF());
        maps.put(IpRouteType.getName(), new IpRouteTypeVF());
        maps.put(IpNetToMediaType.getName(), new IpNetToMediaTypeVF());
        maps.put(TcpConnState.getName(), new TcpConnStateVF());
        maps.put(TcpRtoAlgorithm.getName(), new TcpRtoAlgorithmVF());
    }

    // ==-TEST-CODE-===========================================================================================================
    private Mib2Library() {
        java.lang.System.out.println("ⓘ [TEST-CODE] 构造方法执行了");
    }

    static {
        java.lang.System.out.println("ⓘ [TEST-CODE] 静态代码块执行了");
    }

    {
        java.lang.System.out.println("ⓘ [TEST-CODE] 普通代码块执行了");
    }

    public static void main(String[] args) {
        java.lang.System.out.println("ⓘ [TEST-CODE] main方法开始执行");
        new Mib2Library();
        java.lang.System.out.println("ⓘ [TEST-CODE] main方法执行完毕");
    }
}
