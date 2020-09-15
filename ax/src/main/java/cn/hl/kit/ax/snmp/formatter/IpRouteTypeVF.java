package cn.hl.kit.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-08-06 15:52:17
 * @version $ Id: IpRouteTypeVF.java, v 0.1  hyman Exp $
 */
public class IpRouteTypeVF extends VariableFormatter {
    //other(1)、invalid(2)、direct(3)和indirect(4)
    public static final int OTHER    = 1;
    public static final int INVALID  = 2;
    public static final int DIRECT   = 3;
    public static final int INDIRECT = 4;

    public IpRouteTypeVF() {
        tags = new String[] {"*(0)", "Other(1)", "Invalid(2)", "Direct(3)", "Indirect(4)"};
    }
}
