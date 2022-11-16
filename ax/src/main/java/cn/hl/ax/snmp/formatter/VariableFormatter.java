package cn.hl.ax.snmp.formatter;

import org.snmp4j.smi.VariableBinding;

/**
 * @author hyman
 * @date 2019-08-06 11:15:08
 */
public abstract class VariableFormatter {
    protected String[] tags = new String[0];

    /**
     * 格式化Variable
     *
     * @param vb VB
     * @return formatter text
     */
    public String format(VariableBinding vb) {
        if (vb == null) {
            return "";
        }
        int val = vb.getVariable().toInt();
        return val >= tags.length ? "" + val : tags[val];
    }
}
