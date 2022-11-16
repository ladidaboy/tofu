package cn.hl.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-08-06 17:23:09
 */
public class TcpRtoAlgorithmVF extends VariableFormatter {

    //other(1)、constant(2)、rsre(3)和vanj(4)

    public static final int OTHER    = 1;
    public static final int CONSTANT = 2;
    public static final int RSRE     = 3;
    public static final int VANJ     = 4;

    public TcpRtoAlgorithmVF() {
        tags = new String[] {"*(0)", "Other(1)", "Constant(2)", "RSRE(3)", "VANJ(4)"};
    }
}
