package cn.hl.kit.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-08-06 15:45:04
 * @version $ Id: IpForwardingVF.java, v 0.1  hyman Exp $
 */
public class IpForwardingVF extends VariableFormatter {
    //Forwarding(1)和notForwarding(2)
    public static final int FORWARDING     = 1;
    public static final int NOT_FORWARDING = 2;

    public IpForwardingVF() {
        tags = new String[] {"*(0)", "Forwarding(1)", "和notForwarding(2)"};
    }
}
