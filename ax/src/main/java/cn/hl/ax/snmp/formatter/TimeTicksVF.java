package cn.hl.ax.snmp.formatter;

import cn.hl.ax.data.DataUtils;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

/**
 * @author hyman
 * @date 2021-05-25 10:14:49
 */
public class TimeTicksVF extends VariableFormatter {
    @Override
    public String format(VariableBinding vb) {
        if (vb == null) {
            return "";
        }
        return hexToDateTime(vb.getVariable().toString());
    }

    /**
     * 将16进制的时间转换成标准的时间格式
     *
     * @param hexString 16进制时间
     * @return 标准时间
     */
    private static String hexToDateTime(String hexString) {
        if (DataUtils.isInvalid(hexString)) {
            return "";
        }
        String dateTime = "";
        try {
            byte[] values = OctetString.fromHexString(hexString).getValue();
            int year, month, day, hour, minute;

            year = values[0] * 256 + 256 + values[1];
            month = values[2];
            day = values[3];
            hour = values[4];
            minute = values[5];

            char[] formatStr = new char[22];
            int index = 3;
            int temp = year;
            for (; index >= 0; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[4] = '-';
            index = 6;
            temp = month;
            for (; index >= 5; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[7] = '-';
            index = 9;
            temp = day;
            for (; index >= 8; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[10] = ' ';
            index = 12;
            temp = hour;
            for (; index >= 11; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            formatStr[13] = ':';
            index = 15;
            temp = minute;
            for (; index >= 14; index--) {
                formatStr[index] = (char) (48 + (temp - temp / 10 * 10));
                temp /= 10;
            }
            dateTime = new String(formatStr, 0, formatStr.length).substring(0, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
