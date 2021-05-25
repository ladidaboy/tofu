package cn.hl.ax.snmp.formatter;

import cn.hl.ax.snmp.mib.Mib;
import cn.hl.ax.snmp.mib.Mib2Library;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

/**
 * @author hyman
 * @date 2021-05-25 18:34:06
 */
public class OidInfoVF extends VariableFormatter {
    @Override
    public String format(VariableBinding vb) {
        String val = vb.getVariable().toString();
        OID oid = new OID(val);
        Mib mib = Mib2Library.getMib(oid);
        if (mib != null) {
            val = val.replace(mib.getOidText(), mib.getName());
        }
        return val;
    }
}
