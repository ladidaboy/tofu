package cn.hl.ax.ascii;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * @author hyman
 */
public class RMBConvert {
    /**
     * 阶值 {仟万亿......仟，佰，拾，元}
     */
    private static final char[] UNITS  = {'仟', '佰', '拾', '万', '仟', '佰', '拾', '亿', '仟', '佰', '拾', '万', '仟', '佰', '拾', '元'};
    /**
     * 大写数字
     */
    private static final char[] DIGITS = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

    private static final DecimalFormat FORMATTER = new DecimalFormat("0.00");

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static String getUpper(long amount) {
        StringBuilder result = new StringBuilder();
        String temp = String.valueOf(amount);
        char cc;
        for (int i = 0; i < temp.length(); i++) {
            cc = temp.charAt(i);
            result.append(DIGITS[(int) cc - 48]);
        }
        return result.toString();
    }

    /**
     * 小写金额 转 大写金额
     * <li>最大支持<font color='red'>百万亿</font>(15位整数，2位小数) ；或<font color='red'>仟万亿</font>(16位整数)</li>
     * <li>超过最大支持<font color='red'>仟万亿</font>(16位整数)，只输出大写数字</li> <li>整数部分越大，则小数部分输出越不精确</li>
     * <li>负数，则忽略负号</li>
     *
     * @param amount 金额
     * @return 大写金额
     */
    public static String parse(BigDecimal amount) {
        amount = amount.abs();
        StringBuilder result = new StringBuilder();
        //= 整数部分 ======================================================================================================
        StringBuilder sbInteger = new StringBuilder();
        long integerVal = amount.longValue();
        // 转成大写
        String integerTxt = getUpper(integerVal);
        int txtLen = integerTxt.length();

        String regex_01 = "(零.)+";
        String regex_00 = "(零)+";
        String regex_0 = "零";
        char zero = '零';

        if (txtLen <= 16) {
            //= 仟万亿内 ==============================================================================
            int index = 0;
            for (int i = 16 - txtLen; i < 16; i++) {
                sbInteger.append(integerTxt.charAt(index++));
                // 加权值
                sbInteger.append(UNITS[i]);
            }
            // 加权后, 未合并连续零
            String tmpIntegerTxt = sbInteger.toString();
            int tmpIntegerLen = tmpIntegerTxt.length();
            // 加权后, 未处理连续零
            sbInteger = new StringBuilder();
            if (txtLen >= 13) {
                // 万亿 ,合并连续的 零X
                String str = tmpIntegerTxt.substring(0, tmpIntegerLen - 12 * 2).replaceAll(regex_01, regex_0);
                sbInteger.append(str.charAt(str.length() - 1) == zero ? str.substring(0, str.length() - 1) + '万' : str);
            }
            if (txtLen >= 9) {
                // 亿 ,合并连续的 零X
                int start = Math.max(tmpIntegerLen - 12 * 2, 0);
                String str = tmpIntegerTxt.substring(start, tmpIntegerLen - 8 * 2).replaceAll(regex_01, regex_0);
                sbInteger.append(str.charAt(str.length() - 1) == zero ? str.substring(0, str.length() - 1) + '亿' : str);
            }
            if (txtLen >= 5) {
                // 万 ,合并连续的 零X
                int start = Math.max(tmpIntegerLen - 8 * 2, 0);
                String str = tmpIntegerTxt.substring(start, tmpIntegerLen - 4 * 2).replaceAll(regex_01, regex_0);
                sbInteger.append(str.charAt(str.length() - 1) == zero ? str.substring(0, str.length() - 1) + '万' : str);
            }
            if (txtLen >= 1) {
                // 元 ,合并连续的 零X
                int start = Math.max(tmpIntegerLen - 4 * 2, 0);
                String str = tmpIntegerTxt.substring(start, tmpIntegerLen).replaceAll(regex_01, regex_0);
                sbInteger.append(str.charAt(str.length() - 1) == zero ? str.substring(0, str.length() - 1) + '元' : str);
            }
            // 最后处理
            String lastIntegerTxt = sbInteger.toString().replaceAll(regex_00, regex_0);
            if (lastIntegerTxt.length() == 1) {
                // 元
                lastIntegerTxt = zero + lastIntegerTxt;
                sbInteger = new StringBuilder(lastIntegerTxt);
            } else {
                // XX亿万玖仟元 -> XX亿零玖仟元
                int wIndex = lastIntegerTxt.lastIndexOf('万');
                if (txtLen >= 9 && wIndex > 0 && lastIntegerTxt.charAt(wIndex - 1) == '亿') {
                    lastIntegerTxt = lastIntegerTxt.substring(0, wIndex) + zero + lastIntegerTxt.substring(wIndex + 1);
                    sbInteger = new StringBuilder(lastIntegerTxt);
                }
            }
        } else {
            //= 超仟万亿 ==============================================================================
            sbInteger.append(integerTxt);
            sbInteger.append("元");
        }

        // - 加入结果 ---------------------------------------------------------------------------------
        if (txtLen <= 16) {
            // 处理连续零
            result.append(sbInteger.toString().replaceAll(regex_00, String.valueOf(zero)));
        } else {
            result.append(sbInteger);
        }

        //= 小数部分 (保留二位小数) 0.01 ========================================================================================
        double decimal = amount.remainder(BigDecimal.ONE).doubleValue();
        if (decimal > 0d) {
            StringBuilder sbDecimal = new StringBuilder();

            String decimalTxt = FORMATTER.format(decimal);
            decimalTxt = decimalTxt.substring(2);

            long jiao = Long.parseLong(decimalTxt.substring(0, 1));
            sbDecimal.append(getUpper(jiao)).append(jiao > 0 ? "角" : "");

            long fen = Long.parseLong(decimalTxt.substring(1, 2));
            if (fen > 0) {
                sbDecimal.append(getUpper(fen)).append("分");
            }

            // - 加入结果 -----------------------------------------------------------------------------
            result.append(sbDecimal);
        } else {
            result.append("整");
        }
        return result.toString();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static double getLadder(double ladder) {
        double _ladder = 1;
        while ((ladder /= 10000) >= 1) {
            _ladder *= 10000;
        }
        return _ladder;
    }

    /**
     * 将人民币金额大写转小写
     *
     * @param rmbUpperCase 大写的人民币金额
     * @return 小写金额
     */
    public static BigDecimal format(String rmbUpperCase) throws Exception {
        if (rmbUpperCase == null || rmbUpperCase.trim().length() == 0 || !Pattern.matches("^[零壹贰叁肆伍陆柒捌玖拾佰仟万亿元角分整]+$",
                rmbUpperCase)) {
            throw new Exception("Convert Error!");
        }

        for (int i = 0; i < DIGITS.length; i++) {
            rmbUpperCase = rmbUpperCase.replace(DIGITS[i], (char) (i + 48));
        }

        char[] rmbChars = rmbUpperCase.toCharArray();
        BigDecimal value = BigDecimal.ZERO;
        double base = 1;
        char cc;
        for (int j = rmbChars.length - 1; j >= 0; j--) {
            cc = rmbChars[j];
            switch (cc) {
                case '整':
                    break;
                case '亿':
                    base = 100000000;
                    break;
                case '万':
                    base = getLadder(base) * 10000;
                    break;
                case '仟':
                    base = getLadder(base) * 1000;
                    break;
                case '佰':
                    base = getLadder(base) * 100;
                    break;
                case '拾':
                    base = getLadder(base) * 10;
                    break;
                case '元':
                    base = 1;
                    break;
                case '角':
                    base = 0.1;
                    break;
                case '分':
                    base = 0.01;
                    break;
                default:
                    value = value.add(new BigDecimal(String.valueOf(((int) cc - 48) * base)));
            }
        }
        return value;
    }
}
