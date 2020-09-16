package cn.hl.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-08-06 15:57:58
 * @version $ Id: IpNetToMediaTypeVF.java, v 0.1  hyman Exp $
 */
public class IpNetToMediaTypeVF extends VariableFormatter {

    //other(1)、invalid(2)、dynamic(3)和static(4)

    public static final int OTHER   = 1;
    public static final int INVALID = 2;
    public static final int DYNAMIC = 3;
    public static final int STATIC  = 4;

    public IpNetToMediaTypeVF() {
        tags = new String[] {"*(0)", "Other(1)", "Invalid(2)", "Dynamic(3)", "Static(4)"};
    }
}
