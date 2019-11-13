package cn.hl.ox.clone.bean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DataTypeOfAdvanced extends DataTypeOfBasic {
    private String id;
    private String[] codes;
    private List<String> listValue;
    private Map<String, String> mapValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCodes() {
        return codes;
    }

    public void setCodes(String[] codes) {
        this.codes = codes;
    }

    public List<String> getListValue() {
        return listValue;
    }

    public void setListValue(List<String> listValue) {
        this.listValue = listValue;
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map<String, String> mapValue) {
        this.mapValue = mapValue;
    }

    public String info() {
        return "A.B.{ID=" + id + ", PARENT=" + getParent() + ", CODES=" + Arrays.toString(getCodes()) + ", LIST=" + getListValue() + ", MAP=" + getMapValue() + "}";
    }

    @Override
    public String toString() {
        return "Advanced {ID=" + id + ", Byte=" + getByteValue() + ", Character=" + getCharValue() + ", Short=" + getShortValue() + ", Integer="
                + getIntValue() + ", Long=" + getLongValue() + ", Float=" + getFloatValue() + ", Double=" + getDoubleValue() + ", Boolean="
                + getBooleanValue() + ", LIST=" + getListValue() + ", MAP=" + getMapValue() + ", CODES=" + Arrays.toString(getCodes()) + "}";
    }
}
