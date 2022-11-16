package cn.hl.ax.snmp.formatter;

import org.snmp4j.smi.VariableBinding;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hyman
 * @date 2019-08-06 11:17:59
 */
public class IfTypeVF extends VariableFormatter {
    private static final Map<Integer, String> valueMap = new HashMap<>();

    static {
        valueMap.put(0, "Virtual(0)");
        valueMap.put(1, "Other(1)");
        valueMap.put(2, "Regular1822(2)");
        valueMap.put(3, "HDH1822(3)");
        valueMap.put(4, "DDNX25(4)");
        valueMap.put(5, "RFC877X25(5)");
        valueMap.put(6, "EthernetCSMACD(6)");
        valueMap.put(7, "ISO88023CSMACD(7)");
        valueMap.put(8, "ISO88024TokenBus(8)");
        valueMap.put(9, "ISO88025TokenRing(9)");
        valueMap.put(10, "ISO88026MAN(10)");
        valueMap.put(11, "StarLan(11)");
        valueMap.put(12, "Proteon10Mbit(12)");
        valueMap.put(13, "Proteon80Mbit(13)");
        valueMap.put(14, "HyperChannel(14)");
        valueMap.put(15, "FDDI(15)");
        valueMap.put(16, "LAPB(16)");
        valueMap.put(17, "SDLC(17)");
        valueMap.put(18, "DS1(18)");
        valueMap.put(19, "E1(19)");
        valueMap.put(20, "BasicISDN(20)");
        valueMap.put(21, "PrimaryISDN(21)");
        valueMap.put(22, "PropPointToPointSerial(22)");
        valueMap.put(23, "PPP(23)");
        valueMap.put(24, "SoftwareLoopBack(24)");
        valueMap.put(25, "EON(25)");
        valueMap.put(26, "Ethernet3Mbit(26)");
        valueMap.put(27, "NSIP(27)");
        valueMap.put(28, "SLIP(28)");
        valueMap.put(29, "ULTRA(29)");
        valueMap.put(30, "DS3(30)");
        valueMap.put(31, "SIP(31)");
        valueMap.put(32, "FrameRelay(32)");
        valueMap.put(33, "RS232(33)");
        valueMap.put(34, "PARA(34)");
        valueMap.put(35, "ArcNet(35)");
        valueMap.put(36, "ArcNetPlus(36)");
        valueMap.put(37, "ATM(37)");
        valueMap.put(38, "MIOX25(38)");
        valueMap.put(39, "SONET(39)");
        valueMap.put(40, "X25PLE(40)");
        valueMap.put(41, "ISO88022LLC(41)");
        valueMap.put(42, "LocalTalk(42)");
        valueMap.put(43, "SMDSDXI(43)");
        valueMap.put(44, "FrameRelayService(44)");
        valueMap.put(45, "V35(45)");
        valueMap.put(46, "HSSI(46)");
        valueMap.put(47, "HIPPI(47)");
        valueMap.put(48, "Modem(48)");
        valueMap.put(49, "AAL5(49)");
        valueMap.put(50, "SONETPath(50)");
        valueMap.put(51, "SONETVT(51)");
        valueMap.put(52, "SMDSICIP(52)");
        valueMap.put(53, "PropVirtual(53)");
        valueMap.put(54, "PropMultiplexor(54)");
        valueMap.put(55, "IEEE80212(55)");
        valueMap.put(56, "FibreChannel(56)");
        valueMap.put(57, "HIPPIInterface(57)");
        valueMap.put(58, "FrameRelayInterConnect(58)");
        valueMap.put(59, "AFLANE8023(59)");
        valueMap.put(60, "AFLANE8025(60)");
        valueMap.put(61, "CCTEMUL(61)");
        valueMap.put(62, "FastETHER(62)");
        valueMap.put(63, "ISDN(63)");
        valueMap.put(64, "V11(64)");
        valueMap.put(65, "V36(65)");
        valueMap.put(66, "G703AT64K(66)");
        valueMap.put(67, "G703AT2MB(67)");
        valueMap.put(68, "QLLC(68)");
        valueMap.put(69, "FastETHERFX(69)");
        valueMap.put(70, "Channel(70)");
        valueMap.put(71, "IEEE80211(71)");
        valueMap.put(72, "IBM370PARCHAN(72)");
        valueMap.put(73, "ESCON(73)");
        valueMap.put(74, "DLSW(74)");
        valueMap.put(75, "ISDNS(75)");
        valueMap.put(76, "ISDNU(76)");
        valueMap.put(77, "LAPD(77)");
        valueMap.put(78, "IPSwitch(78)");
        valueMap.put(79, "RSRB(79)");
        valueMap.put(80, "ATMLogical(80)");
        valueMap.put(81, "DS0(81)");
        valueMap.put(82, "DS0Bundle(82)");
        valueMap.put(83, "BSC(83)");
        valueMap.put(84, "ASYNC(84)");
        valueMap.put(85, "CNR(85)");
        valueMap.put(86, "ISO88025DTR(86)");
        valueMap.put(87, "EPLRS(87)");
        valueMap.put(88, "ARAP(88)");
        valueMap.put(89, "PropCNLS(89)");
        valueMap.put(90, "HostPAD(90)");
        valueMap.put(91, "TermPAD(91)");
        valueMap.put(92, "FrameRelayMPI(92)");
        valueMap.put(93, "X213(93)");
        valueMap.put(94, "ADSL(94)");
        valueMap.put(95, "RADSL(95)");
        valueMap.put(96, "SDSL(96)");
        valueMap.put(97, "VDSL(97)");
        valueMap.put(98, "ISO88025CRFPINT(98)");
        valueMap.put(99, "MYRINET(99)");
        valueMap.put(100, "VoiceEM(100)");
        valueMap.put(101, "VoiceFXO(101)");
        valueMap.put(102, "VoiceFXS(102)");
        valueMap.put(103, "VoiceENCAP(103)");
        valueMap.put(104, "VoiceOverIP(104)");
        valueMap.put(105, "ATMDXI(105)");
        valueMap.put(106, "ATMFUNI(106)");
        valueMap.put(107, "ATMIMA(107)");
        valueMap.put(108, "PPPMultilinkBundle(108)");
        valueMap.put(109, "IPOVERCDLC(109)");
        valueMap.put(110, "IPOVERCLAW(110)");
        valueMap.put(111, "StackToStack(111)");
        valueMap.put(112, "VirtualIPAddress(112)");
        valueMap.put(113, "MPC(113)");
        valueMap.put(114, "IPOverATM(114)");
        valueMap.put(115, "ISO88025Fiber(115)");
        valueMap.put(116, "TDLC(116)");
        valueMap.put(117, "GigabitEthernet(117)");
        valueMap.put(118, "HDLC(118)");
        valueMap.put(119, "LAPF(119)");
        valueMap.put(120, "V37(120)");
        valueMap.put(121, "X25MLP(121)");
        valueMap.put(122, "X25HuntGroup(122)");
        valueMap.put(123, "TRASNPHDLC(123)");
        valueMap.put(124, "INTERLEAVE(124)");
        valueMap.put(125, "FAST(125)");
        valueMap.put(126, "IP(126)");
        valueMap.put(127, "DOCSCABLEMACLAYER(127)");
        valueMap.put(128, "DOCSCABLEDOWNSTREAM(128)");
        valueMap.put(129, "DOCSCABLEUPSTREAM(129)");
        valueMap.put(130, "A12MPPSWITCH(130)");
        valueMap.put(131, "Tunnel(131)");
        valueMap.put(132, "Coffee(132)");
        valueMap.put(133, "CES(133)");
        valueMap.put(134, "ATMSubInterface(134)");
        valueMap.put(135, "L2VLAN(135)");
        valueMap.put(136, "L3IPVLAN(136)");
        valueMap.put(137, "L3IPXVLAN(137)");
        valueMap.put(138, "DigitalPowerLine(138)");
        valueMap.put(139, "MEDIAMAILOVERIP(139)");
        valueMap.put(140, "DTM(140)");
        valueMap.put(141, "DCN(141)");
        valueMap.put(142, "IPForward(142)");
        valueMap.put(143, "MSDSL(143)");
        valueMap.put(144, "IEEE1394(144)");
        valueMap.put(145, "IF_GSN(145)");
        valueMap.put(146, "DVBRCCMACLayer(146)");
        valueMap.put(147, "DVBRCCDOWNStream(147)");
        valueMap.put(148, "DVBRCCUPStream(148)");
        valueMap.put(149, "ATMVirtual(149)");
        valueMap.put(150, "MPLSTunnel(150)");
        valueMap.put(151, "SRP(151)");
        valueMap.put(152, "VoiceOverATM(152)");
        valueMap.put(153, "VoiceOverFrameRelay(153)");
        valueMap.put(154, "IDSL(154)");
        valueMap.put(155, "CompositeLink(155)");
        valueMap.put(156, "SS7SIGLINK(156)");
        valueMap.put(157, "PropWirelessP2P(157)");
        valueMap.put(158, "FRForward(158)");
        valueMap.put(159, "RFC1483(159)");
        valueMap.put(160, "USB(160)");
        valueMap.put(161, "IEEE8023ADLag(161)");
        valueMap.put(65534, "SwitchStack(65534)");
    }

    private String address = null;

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String format(VariableBinding vb) {
        if (vb == null) {
            return "";
        }
        int val = vb.getVariable().toInt();
        String txt = vb.getVariable().toString();
        if (address != null && address.contains("127.0.0.1")) {
            String[] tags = {"*", "softwareLoopback", "ethernetCSMACD"};
            return val >= tags.length ? txt : tags[val];
        }
        String tag = valueMap.get(val);
        return tag == null ? txt : tag;
    }
}
/*
ifType在rfc-1213中的定义为
ifType OBJECT-TYPE
    SYNTAX  INTEGER {
        other(1),          -- none of the following
        regular1822(2),
        hdh1822(3),
        ddn-x25(4),
        rfc877-x25(5),
        ethernet-csmacd(6),-- (以太网MAC协议），那就是网卡了
        iso88023-csmacd(7),
        iso88024-tokenBus(8),
        iso88025-tokenRing(9),
        iso88026-man(10),
        starLan(11),
        proteon-10Mbit(12),
        proteon-80Mbit(13),
        hyperchannel(14),
        fddi(15),
        lapb(16),
        sdlc(17),
        ds1(18),           -- T-1
        e1(19),            -- european equiv. of T-1
        basicISDN(20),
        primaryISDN(21),   -- proprietary serial
        propPointToPointSerial(22), --专用串行接口
        ppp(23),
        softwareLoopback(24),-- 用于在同一系统之间的过程传输
        eon(25),            -- CLNP over IP [11]
        ethernet-3Mbit(26),
        nsip(27),           -- XNS over IP
        slip(28),           -- generic SLIP
        ultra(29),          -- ULTRA technologies
        ds3(30),            -- T-3
        sip(31),            -- SMDS
        frame-relay(32)
    }
    ACCESS  read-only
    STATUS  mandatory
    DESCRIPTION
          "The type of interface, distinguished according to
          the physical/link protocol(s) immediately `below'
          the network layer in the protocol stack."
    ::= { ifEntry 3 }
网上说从编号1-54都有，没看到。补充资料如下：
①46 hssi高速串行接口；
②PC机上的串口是不是RS－232，这个接口类型号是33（rs232）
解释：本机返回1:softwareLoopback; 2:ethernetCsmacd(csmacd: 带冲突检测的载波侦听多路访问)
*/