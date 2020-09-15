package cn.hl.kit.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-11-21 17:34:51
 * @version $ Id: TcpConnStateVF.java, v 0.1  hyman Exp $
 */
public class TcpConnStateVF extends VariableFormatter {
    //Closed(1), Listen(2), SynSent(3), SynReceived(4), Established(5),
    //FinWait1(6), FinWait2(7), CloseWait(8), LastAck(9), Closing(10), TimeWait(11), DeleteTCB(12)

    public static final int CLOSED       = 1;
    public static final int LISTEN       = 2;
    public static final int SYN_SENT     = 3;
    public static final int SYN_RECEIVED = 4;
    public static final int ESTABLISHED  = 5;
    public static final int FIN_WAIT1    = 6;
    public static final int FIN_WAIT2    = 7;
    public static final int CLOSE_WAIT   = 8;
    public static final int LAST_ACK     = 9;
    public static final int CLOSING      = 10;
    public static final int TIME_WAIT    = 11;
    public static final int DELETE_TCB   = 12;

    public TcpConnStateVF() {
        tags = new String[] {"*(0)", "Closed(1)", "Listen(2)", "SynSent(3)", "SynReceived(4)", "Established(5)", "FinWait1(6)",
                "FinWait2(7)", "CloseWait(8)", "LastAck(9)", "Closing(10)", "TimeWait(11)", "DeleteTCB(12)"};
    }
}
