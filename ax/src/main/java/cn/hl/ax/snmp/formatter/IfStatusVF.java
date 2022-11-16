package cn.hl.ax.snmp.formatter;

/**
 * @author hyman
 * @date 2019-08-06 14:07:52
 */
public class IfStatusVF extends VariableFormatter {

    //up(1)、down(2)和testing(3)

    public static final int UP      = 1;
    public static final int DOWN    = 2;
    public static final int TESTING = 3;

    public IfStatusVF() {
        tags = new String[] {"*(0)", "Up(1)", "Down(2)", "Testing(3)"};
    }
}
