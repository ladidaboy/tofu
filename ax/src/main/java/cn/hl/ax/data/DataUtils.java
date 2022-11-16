package cn.hl.ax.data;

import cn.hl.ax.CommonConst;
import cn.hl.ax.CommonConst.CompareFeature;
import cn.hl.ax.CommonConst.FormatPrettify;
import cn.hl.ax.clone.ReflectionUtils;
import cn.hl.ax.enums.BaseEnumInterface;
import cn.hl.ax.log.LogUtils;
import cn.hl.ax.spring.SpringUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.github.pagehelper.PageInfo;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hyman
 * @date 2020-09-14 20:54:39
 */
@Slf4j
public class DataUtils {
    /**
     * 数组是否包含文本
     * @param arr 数组
     * @param txt 文本
     * @return true.包含; false.不包含
     */
    public static boolean contains(String[] arr, String txt) {
        if (arr == null || arr.length == 0 || txt == null) {
            return false;
        }
        for (String src : arr) {
            if (src.equals(txt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合是否包含文本
     * @param clc 集合
     * @param txt 文本
     * @return true.包含; false.不包含
     */
    public static boolean contains(Collection<String> clc, String txt) {
        if (clc == null || clc.size() == 0 || txt == null) {
            return false;
        }
        for (String src : clc) {
            if (src.equals(txt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断主键编号是否有效
     * @param id 主键值
     * @return true.有效; false.无效
     */
    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    /**
     * 检查字符是否在[a~z,A~Z,0~9,汉字]中
     * @param ch 字符
     * @return true:合法, false:非法
     */
    public static boolean isValidChar(char ch) {
        return (ch >= 0x4E00 && ch <= 0X9FA5) || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
    }

    /**
     * 判断日期字符串是否有效
     * @param datetime 待判定日期字符串
     * @return true.有效; false.无效
     */
    public static boolean isValidDateTime(String datetime) {
        return isValidDateFormat(datetime, CommonConst.SF_YMD_HMS) // 年-月-日 时:分:秒
                || isValidDateFormat(datetime, CommonConst.SF_YMD) // 年-月-日
                || isValidDateFormat(datetime, CommonConst.SF_YMD_HMS_MS); // // 年-月-日 时:分:秒.毫秒
    }

    /**
     * 判断日期字符串是否有效(指定日期格式)
     * @param datetime 待判定日期字符串
     * @param formatter 日期格式
     * @return true.有效; false.无效
     */
    public static boolean isValidDateFormat(String datetime, String formatter) {
        if (datetime == null || formatter == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(formatter);
        Matcher matcher = pattern.matcher(datetime);
        if (matcher.matches()) {
            matcher = CommonConst.P_YMD.matcher(datetime);
            if (matcher.matches()) {
                int y = Integer.parseInt(matcher.group(1));
                int m = Integer.parseInt(matcher.group(2));
                int d = Integer.parseInt(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m - 1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return lastDay >= d;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断数据对象是否有效(至少有一个有效属性)
     * @param record JFinal.Record
     * @return true.有效; false.无效
     */
    public static boolean isValidRecord(Record record) {
        return record != null && !record.getColumns().isEmpty();
    }

    /**
     * 检查JAVA Bean对象是否合法
     * <br>此方法首先会过滤掉Bean对象的id属性，然后检查所有其他属性是否都为null
     * @param bean 待检查对象
     * @return false: JAVA Bean所有属性(除了id)均为null, true: JAVA Bean有属性不为null
     */
    public static boolean isValidBean(Object bean) {
        return isValidBean(bean, "id");
    }

    /**
     * 检查JAVA Bean对象是否合法
     * <br>此方法首先会过滤掉Bean对象的指定属性，然后检查所有其他属性是否都为null
     * @param bean 待检查对象
     * @param filterFields 过滤属性
     * @return false: JAVA Bean所有属性(除了id)均为null, true: JAVA Bean有属性不为null
     */
    public static boolean isValidBean(Object bean, String... filterFields) {
        if (bean == null) {
            return false;
        }
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (contains(filterFields, field.getName())) {
                    continue;
                }
                if (ReflectUtil.getFieldValue(bean, field) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查对象所有的属性是否有 有效值 (基于 ObjectUtil.isNotEmpty 方法 扩展)
     * @see cn.hutool.core.util.ObjectUtil
     * @param obj 待判定对象
     * @param filterFields (尝试过滤掉)不检查字段
     * @return 只要有一个属性值有效，对象即为有效，否则对象无效。null对象为无效。
     */
    public static boolean isValid(Object obj, String... filterFields) {
        if (obj == null) {
            return false;
        } else if (obj instanceof CharSequence) {
            return StrUtil.isNotEmpty((CharSequence) obj);
        } else if (obj instanceof Collection) {
            return CollUtil.isNotEmpty((Collection) obj);
        } else if (obj instanceof Iterable) {
            return IterUtil.isNotEmpty((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return IterUtil.isNotEmpty((Iterator) obj);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isNotEmpty(obj);
        } else if (obj instanceof Map) {
            return MapUtil.isNotEmpty((Map) obj);
        } else if (obj instanceof Record) {
            return isValidRecord((Record) obj);
        } else if (obj instanceof Object[]) {
            Object[] objects = (Object[]) obj;
            if (objects.length == 0) {
                return false;
            }
            for (Object o : objects) {
                if (isValid(o, filterFields)) {
                    return true;
                }
            }
            return false;
        } else if (!ReflectionUtils.isBasicDataType(obj)) {
            return isValidBean(obj, filterFields);
        } else {
            return false;
        }
    }

    /**
     * isValid 方法的逆向方法
     * @param obj 待判定对象
     * @param filterFields (尝试过滤掉)不检查字段
     * @return ! isValid
     */
    public static boolean isInvalid(Object obj, String... filterFields) {
        return !isValid(obj, filterFields);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 检查status里是否包含flag
     * @param status flag1 | flag2 | ...
     * @param flag 2^n
     * @return true: status包含flag
     */
    public static boolean statusHasFlag(int status, int flag) {
        return status > 0 && (status & flag) == flag;
    }

    /**
     * 检查status是否为单一flag
     * @param status flag1 | flag2 | ...
     * @return true:单一, false:非单一
     */
    public static boolean isMonoStatus(int status) {
        return status > 0 && (status & (status - 1)) == 0;
    }

    /**
     * 检查status所有的状态位是否都为真
     * @param status flag1 | flag2 | ...
     * @param ranks flag的类型数量
     * @return true:全真, false:非全真
     */
    public static boolean isFullStatus(int status, int ranks) {
        return status > 0 && status == (int) (Math.pow(2, ranks) - 1);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 转换输出 String
     */
    public static String convert2String(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return CommonConst.SDF_YMD_HMS.format(value);
        } else {
            return value.toString();
        }
    }

    /**
     * 转换输出 Integer
     */
    public static Integer convert2Integer(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            log.warn(LogUtils.getSimpleMessages(e));
            return null;
        }
    }

    /**
     * 转换输出 Date
     */
    public static Date convert2Date(Object date) {
        if (date instanceof Date) {
            return (Date) date;
        } else if (date instanceof String) {
            String dateChar = (String) date;
            Date formatDate;
            try {
                formatDate = DateUtils.parseDate(dateChar, "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
            } catch (Exception e) {
                log.warn(LogUtils.getSimpleMessages(e));
                formatDate = null;
            }
            return formatDate;
        } else if (date instanceof Long) {
            return new Date((Long) date);
        } else {
            return null;
        }
    }

    /**
     * 格式化对象成字符串
     * @param value 数值对象
     * @return 字符串
     */
    public static String formatValue(Object value) {
        return formatValue(value, FormatPrettify.NONE);
    }

    /**
     * 格式化对象成字符串
     * @param value 数值对象
     * @param prettifies 格式化特效
     * @return 字符串
     */
    public static String formatValue(Object value, FormatPrettify... prettifies) {
        String ss;
        if (value == null) {
            ss = null;
        } else if (value instanceof Date || isValidDateTime(value.toString())) {
            if (isValidDateTime(value.toString())) {
                ss = value.toString();
            } else if (FormatPrettify.DATE_YMD.findIn(prettifies)) {
                ss = CommonConst.SDF_YMD.format((Date) value);
            } else {
                ss = CommonConst.SDF_YMD_HMS.format((Date) value);
            }
            if (FormatPrettify.DATE.findIn(prettifies)) {
                ss = '\'' + ss + '\'';
            }
        } else if (value instanceof BigDecimal) {
            BigDecimal bg = (BigDecimal) value;
            if (new BigDecimal(bg.intValue()).compareTo(bg) == 0) {
                //整数
                ss = String.valueOf(bg.intValue());
            } else {
                //小数
                ss = String.valueOf(bg.doubleValue());
            }
        } else if (value instanceof BaseEnumInterface) {
            ss = String.valueOf(((BaseEnumInterface) value).getValue());
            if (FormatPrettify.ENUM.findIn(prettifies)) {
                ss = value.toString() + '(' + ss + ')';
            }
        } else {
            ss = value.toString();
        }
        return ss;
    }

    /**
     * 将字符串性的数据值或百分数值格式化去掉小数点后的数值
     * <br>百分数值格式化后保留"%"
     * @param value 数值对象
     * @return 格式化数值
     */
    public static String formatNumber(String value) {
        return formatNumber(value, null);
    }

    /**
     * 将字符串性的数据值或百分数值格式化成制定的格式
     * <br>百分数值格式化后保留"%"
     * @param value 待格式化值
     * @param formatter 格式化格式,null值时使用默认格式"###################0"
     * @return 格式后的字符串
     */
    public static String formatNumber(String value, String formatter) {
        if (value == null) {
            return null;
        }
        formatter = isValid(formatter) ? formatter : "###################0";
        DecimalFormat decimalFormat = new java.text.DecimalFormat(formatter);
        try {
            value = decimalFormat.format(Double.parseDouble(value));
        } catch (Exception e) {
            try {
                int len = value.length() - 1;
                value = decimalFormat.format(Double.parseDouble(value.substring(0, len))) + value.substring(len);
            } catch (Exception ee) {
                log.warn(LogUtils.getSimpleMessages(e));
            }
        }
        return value;
    }

    public static String formatDecimal(Double value) {
        return formatDecimal(value, null);
    }

    /**
     * 格式化数值
     * @param value 原数值
     * @param pattern 格式字符串, 如果pattern为null则默认保留2位小数(四舍五入)
     * <style>table{border-right:1px solid #ccc;border-bottom:1px solid #ccc;margin:2px;}
     * th,td{border-left:1px solid #ccc;border-top:1px solid #ccc;padding:2px 4px;}</style>
     * <table border=1>
     * <tr><th>符号</th><th>含义</th><th>例子</th></tr>
     * <tr><td>0</td><td>阿拉伯数字,如果不存在则显示为0</td><td>eg: 0000.00 --> 123.3435=0123.34 ; 12345.6543=12345.65 ; 12345=12345.00</td></tr>
     * <tr><td>#</td><td>阿拉伯数字</td><td>eg: ####.## --> 123.3435=123.34 ; 12345.6543=12345.65 ; 12345=12345</td></tr>
     * <tr><td>.</td><td>小数分隔符或货币小数分隔符</td><td>eg: ####.## --> 123.3435=123.34 ;</td></tr>
     * <tr><td>,</td><td>分组分隔符</td><td>eg: #,###.## --> 123456.3435=123,456.3435 ; 123123456.3435=123,123,456.3435</td></tr>
     * <tr><td>%</td><td> 前缀或后缀 是 乘以 100 并显示为百分数</td><td>eg: #.##% --> 0.9867=98.67% ; </td></tr>
     * <tr><td>\u2030</td><td>前缀或后缀 是 乘以 1000 并显示为千分数 </td><td>eg: #.##% --> 0.98678=986.78‰ ; </td></tr>
     * </table>
     * @return 格式后的字符串
     */
    public static String formatDecimal(Double value, String pattern) {
        if (value == null) {
            return null;
        }
        pattern = isValid(pattern) ? pattern : "0.00";
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(value);
    }

    /**
     * 获取百分比数值
     * @param molecular 分子
     * @param denominator 分母
     * @param formatter 百分比数格式，为null时默认使用[###0.00]
     * @return 百分比字符串
     */
    public static String format2Ratio(String molecular, String denominator, String formatter) {
        double iMolecular = Double.parseDouble(molecular);
        double iDenominator = Double.parseDouble(denominator);
        double rate = iMolecular / iDenominator * 100;
        formatter = isValid(formatter) ? formatter : "###0.00";
        DecimalFormat decimalFormat = new java.text.DecimalFormat(formatter);
        return decimalFormat.format(rate) + "%";
    }

    /**
     * 保留小数位数(四舍五入)
     * @param value 数值
     * @param digits 位数
     * @return 数值
     */
    public static double getScale(double value, int digits) {
        /*
        方式一：四舍五入
            double f = 111231.5585;
            BigDecimal b = new BigDecimal(f);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        方式二：
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            df.format(你要格式化的数字);
            例：new java.text.DecimalFormat("#.00").format(3.1415926)
            #.00 表示两位小数 #.0000四位小数 以此类推...
        方式三：
            double d = 3.1415926;
            String result = String .format("%.2f",d);
            %.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型
        方式四：
            NumberFormat ddf1=NumberFormat.getNumberInstance() ;
            void setMaximumFractionDigits(int digits)
            digits 显示的数字位数
            为格式化对象设定小数点后的显示的最多位,显示的最后位是舍入的
        */
        return new BigDecimal(value).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取数组排序后指定比例位置的数值(e.g.95值)
     * @param data 数组
     * @param percent 百分比值 [0.0, 100.0]
     * @return T
     * @throws RuntimeException When `data` is blank / `percent` not in valid range.
     */
    public static <T extends Comparable<T>> T getPercentIndex(T[] data, double percent) {
        if (isInvalid(data)) {
            throw new RuntimeException("`data` is empty");
        }
        if (percent < 0 || percent > 100) {
            throw new RuntimeException("illegal `percent`");
        }
        Arrays.sort(data);
        int index = (int) ((data.length - 1) * percent / 100);
        return data[index];
    }

    /**
     * 获取数组排序后指定比例位置的数值(e.g.95值)
     * @param data 数组
     * @param percent 百分比值 [0.0, 100.0]
     * @return T
     * @throws RuntimeException When `data` is blank / `percent` not in valid range.
     */
    public static <T extends Comparable<T>> T getPercentIndex(List<T> data, double percent) {
        if (isInvalid(data)) {
            throw new RuntimeException("`data` is empty");
        }
        if (percent < 0 || percent > 100) {
            throw new RuntimeException("illegal `percent`");
        }
        Collections.sort(data);
        int index = (int) ((data.size() - 1) * percent / 100);
        return data.get(index);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 判断对象是否相等(默认两个NULL不相等)
     * @param val1 值1
     * @param val2 值2
     * @return true.相等; false.不相等
     */
    public static boolean isSame(Object val1, Object val2) {
        return isSame(val1, val2, CompareFeature.NONE);
    }

    /**
     * 判断对象是否相等
     * @param val1 值1
     * @param val2 值2
     * @param features 比较特效
     * @return true.相等; false.不相等
     */
    public static boolean isSame(Object val1, Object val2, CompareFeature... features) {
        if (val1 == null && val2 == null) {
            return CompareFeature.NULL_EQUALS.findIn(features);
        } else if (val1 != null && val2 != null) {
            if (isValidDateTime(val1.toString())) {
                val1 = convert2Date(val1);
            }
            if (isValidDateTime(val2.toString())) {
                val2 = convert2Date(val2);
            }
            Class<?> clz1 = val1.getClass(), clz2 = val2.getClass();
            if (clz1 == clz2 && clz1 != String.class) {
                if (clz1 == BigDecimal.class) {
                    return ((BigDecimal) val1).compareTo((BigDecimal) val2) == 0;
                } else if (clz1 == Date.class) {
                    if (CompareFeature.DATE_YMD.findIn(features)) {
                        String d1 = formatValue(val1, FormatPrettify.DATE_YMD);
                        String d2 = formatValue(val2, FormatPrettify.DATE_YMD);
                        return d1.equals(d2);
                    } else {
                        return ((Date) val1).compareTo((Date) val2) == 0;
                    }
                } else {
                    return val1.equals(val2);
                }
            } else {
                String s1 = formatValue(val1);
                String s2 = formatValue(val2);
                return CompareFeature.IGNORE_CASE.findIn(features) ? s1.equalsIgnoreCase(s2) : s1.equals(s2);
            }
        } else {
            return false;
        }
    }

    /**
     * 判断对象是否相等(默认忽略大小写)
     * @param val1 值1
     * @param val2 值2
     * @return true.不相等; false.相等
     */
    public static boolean notSame(Object val1, Object val2) {
        return !isSame(val1, val2, CompareFeature.IGNORE_CASE);
    }

    /**
     * 判断是否匹配数组中的一个值
     * @param target 目标对象
     * @param values 数组对象
     * @return true.匹配到; false.未匹配到
     */
    public static boolean matchOne(Object target, Object... values) {
        if (isInvalid(target) || isInvalid(values)) {
            return false;
        }
        for (Object one : values) {
            if (isSame(one, target)) {
                return true;
            }
        }
        return false;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 获取有效的数据值(Valid Value)<br>
     * 若传进来的数据为NULL则返回`EMPTY`
     */
    public static String getVb(String value) {
        return isValid(value) ? value : CommonConst.S_EMPTY;
    }

    /**
     * 获取有效的数据值(Valid Value)<br>
     * 若传进来的数据为NULL则返回`ERROR`
     */
    public static String getVe(String value) {
        return isValid(value) ? value : CommonConst.S_ERROR;
    }

    /**
     * 获取有效的数据值(Valid Value)<br>
     * 若传进来的数据为NULL则返回`default`
     */
    public static String getVd(String value, String def) {
        return isValid(value) ? value : def;
    }

    /**
     * 去除文本中的非法字符(只保留 0-9 a-z A-Z 中文)
     */
    public static String pruneText(String txt) {
        if (isInvalid(txt)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char cc : txt.toCharArray()) {
            if (isValidChar(cc)) {
                sb.append(cc);
            }
        }
        return sb.toString();
    }

    /**
     * 压缩文本(去除无效空行等特殊字符,多个空格压缩成一个)
     * @param text 待处理文本
     * @return 压缩后文本
     */
    public static String compressText(String text) {
        if (isValid(text)) {
            // 移除 空行
            text = text.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
            // 换行 转成 空格
            text = text.replaceAll("\n", " ");
            // 空白符号/中文空格 转成 空格
            text = text.replaceAll("[\\s 　]", " ");
            // 多个空格(\t\n) 转成 一个空格 \\p{Blank}{2,}
            text = text.replaceAll("[ \\t]{2,}", " ").trim();
        }
        return text;
    }

    /**
     * 压缩文本(去除无效空行、空格等特殊字符)
     * @param text 待处理文本
     * @return 压缩后文本
     */
    public static String compressTextEx(String text) {
        if (isValid(text)) {
            text = compressText(text);
            // 移除 所有空格
            text = text.replaceAll(" ", "");
        }
        return text;
    }

    /**
     * 获取字符串 字节长度
     */
    public static int getSize(String txt) {
        if (txt == null) {
            return -1;
        }
        int cnt = 0;
        for (int i = 0; i < txt.length(); i++) {
            cnt += (String.valueOf(txt.charAt(i)).getBytes().length > 1) ? 2 : 1;
        }
        return cnt;
    }

    /**
     * 格式化字符串至指定长度
     */
    public static String rightPadEx(String txt, int len) {
        return rightPadEx(txt, len, (char) 32);
    }

    /**
     * 格式化字符串至指定长度
     */
    public static String rightPadEx(String txt, int len, char fill) {
        txt = getVd(txt, CommonConst.S_EMPTY);
        int txtLen = getSize(txt);
        if (txtLen > len) {
            String txt1 = txt;
            txt = "";
            int cnt = 0, cl, i;
            char ch;
            for (i = 0; i < txt1.length(); i++) {
                ch = txt1.charAt(i);
                cl = (String.valueOf(ch).getBytes().length > 1) ? 2 : 1;
                cnt += cl;
                if (cnt <= len - 3) {
                    txt += ch;
                } else {
                    cnt -= cl;
                    break;
                }
            }
            txt += ((cnt == len - 3) ? "" : ".") + "...";
        } else {
            for (; txtLen < len; txtLen++) {
                txt += fill;
            }
        }
        return txt;
    }

    /**
     * MD5加密
     * @param data 原数据
     * @return 加密后字符串
     */
    public static String md5DigestAsHex(String data, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(charset));
            byte[] ss = md.digest();
            StringBuilder result = new StringBuilder();
            for (byte s : ss) {
                result.append(Integer.toHexString((0x000000FF & s) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.warn(LogUtils.getSimpleMessages(e));
        }
        return CommonConst.S_EMPTY;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 下划线转驼峰
     * @param attr 下划线规则的名称
     * @return 驼峰规则的名称
     */
    public static String lineToHump(String attr) {
        attr = attr.toLowerCase();
        Matcher matcher = CommonConst.P_LINE.matcher(attr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return StringUtils.uncapitalize(sb.toString());
    }

    /**
     * 驼峰转下划线
     * @param attr 驼峰规则的名称
     * @return 下划线规则的名称
     */
    public static String humpToLine(String attr) {
        Matcher matcher = CommonConst.P_HUMP.matcher(attr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, CommonConst.S_UNDERSCORE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        if (StringUtils.startsWith(sb, CommonConst.S_UNDERSCORE)) {
            return sb.substring(1);
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine(String)})
     * @param attr 驼峰规则的名称
     * @return 属性名称
     */
    public static String humpToLine2(String attr) {
        return attr.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 包装单个对象成数组
     * @param source 目标对象
     * @param <E> 类名
     */
    public static <E> E[] boxingArray(E source) {
        if (source == null) {
            return null;
        } else {
            E[] arr = ArrayUtil.newArray(source.getClass().getComponentType(), 1);
            arr[0] = source;
            return arr;
        }
    }

    /**
     * 往数组添加数组
     * @param target 目标数组
     * @param source 待添加数组
     * @param <E> 类名
     */
    public static <E> E[] add2Array(E[] target, E[] source) {
        if (target == null || isInvalid(source)) {
            return isInvalid(target) ? isInvalid(source) ? null : source : target;
        }
        return ArrayUtil.append(target, source);
    }

    /**
     * 往数组添加元素
     * @param target 目标数组
     * @param source 待添加元素
     * @param <E> 类名
     */
    public static <E> E[] add2Array(E[] target, E source) {
        if (target == null || isInvalid(source)) {
            return isInvalid(target) ? isInvalid(source) ? null : boxingArray(source) : target;
        }
        return ArrayUtil.append(target, source);
    }

    /**
     * 往集合添加集合
     * @param target 目标集合
     * @param source 待添加集合
     * @param <E> 类名
     */
    public static <E> void add2List(Collection<E> target, Collection<E> source) {
        if (target == null || isInvalid(source)) {
            return;
        }
        target.addAll(source);
    }

    /**
     * 往集合添加元素
     * @param target 目标集合
     * @param source 待添加元素
     * @param <E> 类名
     */
    public static <E> void add2List(Collection<E> target, E source) {
        if (target == null || isInvalid(source)) {
            return;
        }
        target.add(source);
    }

    /**
     * 安全化空数组
     */
    public static <T> List<T> safeEmpty(List<T> data) {
        return data == null ? new ArrayList<>() : data;
    }

    /**
     * 安全化空分页
     */
    public static <T> PageInfo<T> safeEmpty(PageInfo<T> page) {
        page = page == null ? new PageInfo<>() : page;
        page.setList(safeEmpty(page.getList()));
        return page;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 从指定的枚举类型中按复合状态过滤出命中的枚举集合
     * @param clz 枚举类型
     * @param field 使用字段
     * @param complexValue 复合状态值
     * @param <E> 类名
     * @return 命中的枚举集合
     */
    public static <E extends Enum<E>> List<E> fromComplexField(Class<E> clz, String field, Integer complexValue) {
        if (complexValue == null) {
            throw new EnumConstantNotPresentException(clz, field + "(null)");
        }
        List<E> out = new ArrayList<>();
        EnumSet<E> set = EnumSet.allOf(clz);
        for (E e : set) {
            try {
                int value = (int) ReflectUtil.getFieldValue(e, field);
                if (statusHasFlag(complexValue, value)) {
                    out.add(e);
                }
            } catch (Exception ex) {
                log.warn(LogUtils.getSimpleMessages(ex));
            }
        }
        return out;
    }

    /**
     * 获取集合类的实际Java类型
     * @param data 数组对象
     * @return 实际类名
     */
    public static <T> Class<?> getCollectionClz(Collection<T> data) {
        if (isInvalid(data)) {
            throw new RuntimeException("Invalid Collection(data)");
        }
        T ele = data.stream().findFirst().get();
        return ele.getClass();
    }

    /**
     * 获取 M模型 实现了指定接口的 T模型
     * <br/> eg. M implements Converter&lt;T&gt;
     * @param modelClazz m-class
     * @param converterClazz c-class
     * @param <M> JavaBean模型
     * @param <C> Converter接口
     * @return 接口上定义的T模型类
     */
    public static <M, C> Class<?> getClazzT(Class<M> modelClazz, Class<C> converterClazz) {
        if (modelClazz == null || converterClazz == null) {
            return null;
        }
        if (converterClazz.isAssignableFrom(modelClazz)) {
            Type[] types = modelClazz.getGenericInterfaces();
            for (Type type : types) {
                Class<?> cc = TypeUtil.getClass(type);
                if (converterClazz.isAssignableFrom(cc)) {
                    return (Class<?>) TypeUtil.getTypeArgument(type, 0);
                }
            }
        }
        return null;
    }

    /**
     * 判断 M模型 是否实现了指定接口的 T模型
     * <br/> eg. M implements Converter&lt;T&gt;
     * @param modelClazz m-class
     * @param converterClazz c-class
     * @param targetClazz t-class
     * @param <M> JavaBean模型
     * @param <C> Converter接口
     * @param <T> JavaBean模型
     * @return true/false
     */
    public static <M, C, T> boolean isModelAssignableFromT(Class<M> modelClazz, Class<C> converterClazz, Class<T> targetClazz) {
        if (modelClazz == null || converterClazz == null || targetClazz == null) {
            return false;
        }
        if (converterClazz.isAssignableFrom(modelClazz)) {
            Type[] types = modelClazz.getGenericInterfaces();
            for (Type type : types) {
                Class<?> cc = TypeUtil.getClass(type);
                if (converterClazz.isAssignableFrom(cc)) {
                    Type[] args = TypeUtil.getTypeArguments(type);
                    for (Type arg : args) {
                        if (TypeUtil.getClass(arg) == targetClazz) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 按指定的属性值进行断言
     * <br/>主要用于数组过滤及分组
     * @param keyExtractor 属性提取器
     * @param <T> 泛型
     * @return 断言
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public static MessageSource getMessageSource() {
        MessageSource source = SpringUtils.getBean("messageSource");
        if (source == null) {
            Map<String, MessageSource> mapSource = SpringUtils.getBeansOfType(MessageSource.class);
            if (!mapSource.isEmpty()) {
                source = mapSource.values().stream().findFirst().get();
            }
        }
        return source;
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @return 信息值
     */
    public static String getMessage(String key) {
        return getMessage(key, getMessageSource(), null);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param source 国际化源
     * @return 信息值
     */
    public static String getMessage(String key, MessageSource source) {
        return getMessage(key, source, null);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param language 语言名称
     * @return 信息值
     */
    public static String getMessage(String key, String language) {
        return getMessage(key, getMessageSource(), language);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param source 国际化源
     * @param language 语言名称
     * @return 信息值
     */
    public static String getMessage(String key, MessageSource source, String language) {
        if (source == null || isInvalid(key)) {
            return key;
        }
        try {
            Locale locale;
            if (isInvalid(language)) {
                locale = LocaleContextHolder.getLocale();
                //locale = Locale.US;
            } else {
                locale = new Locale(language);
            }
            return source.getMessage(key, null, locale);
        } catch (Exception ex) {
            return key;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 通过IP整数数值获取IP.V4表示的数值
     */
    public static String parseIpV4(int ipBit) {
        String ipBinary = Integer.toBinaryString(ipBit);
        ipBinary = rightPadEx(ipBinary, 32, '0');
        List<Integer> ipV4Sections = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            ipV4Sections.add(Integer.valueOf(ipBinary.substring((i - 1) * 8, i * 8), 2));
        }
        return StringUtils.join(ipV4Sections, ".");
    }

    /**
     * 获取IP地址数组
     * @param ip e.g. 1.2.3.4 or 1.2.3.4/8
     * @return 数组[sec1, sec2, sec3, sec4, bit, mask]
     */
    public static int[] getNumericIpV4(String ip) {
        String[] segments = ip.split("/");
        String[] sections = segments[0].split("[.]");
        if (sections.length != 4) {
            throw new RuntimeException("Wrong IP address[" + ip + "]");
        }
        int bit = 0;
        int[] ips = new int[6];
        for (int i = 0; i < 4; i++) {
            int ss = Integer.parseInt(sections[i]);
            if (ss < 0 || ss > 255) {
                throw new RuntimeException("Wrong IP address[" + ip + "]");
            }
            bit |= (ss << (8 * (3 - i)));
            ips[i] = ss;
        }
        ips[4] = bit;
        if (segments.length > 1) {
            ips[5] = Integer.parseInt(segments[1]);
        }
        return ips;
    }

    /**
     * 三个私有地址IP地址区域
     * <ol>
     *     <li>10.0.0.0/8:     10.0.0.0 ~~ 10.255.255.255</li>
     *     <li>172.16.0.0/12:  172.16.0.0 ~~ 172.31.255.255</li>
     *     <li>192.168.0.0/16: 192.168.0.0 ~~ 192.168.255.255</li>
     * </ol>
     * @param ip e.g. 1.2.3.4 or 1.2.3.4/8
     */
    public static boolean isInternalIp(String ip) {
        return isInternalIp(getNumericIpV4(ip));
    }

    /**
     * @see #isInternalIp(String)
     * @param address IP地址数组, 长度必须为4, 每位数值范围[0,255)
     */
    public static boolean isInternalIp(int[] address) {
        final int S1 = address[0];
        final int S2 = address[1];
        final int S3 = address[2];
        final int S4 = address[3];
        final int SB = address[4];//ip bit
        final int SM = address[5];//ip mask
        // Benchmark 1: 10.x.x.x/8
        final int B1S1 = 10;
        // Benchmark 2: 172.(16~31).x.x/12
        final int B2S1 = 172;
        final int B2S2S = 16;
        final int B2S2B = 31;
        // Benchmark 3: 192.168.x.x/16
        final int B3S1 = 192;
        final int B3S2 = 168;
        // 判定是否是内网IP地址
        boolean intranet = S1 == B1S1 || (S1 == B2S1 && S2 >= B2S2S && S2 <= B2S2B) || (S1 == B3S1 && S2 == B3S2);
        // 判定IP网段是否有效(判定高位归零后，IP地址是否为0)
        if (intranet && SM >= 8 && SM < 32) {
            int ipbit = SB;//S1 << 24 | S2 << 16 | S3 << 8 | S4;
            // 高位归0(在segment之前的位数)
            ipbit &= 0xFFFFFFFF >>> SM;
            // 低位归0(在segment之后的8整位数)
            //ipbit = ipbit >> ((32 - SM) / 8 * 8);
            return ipbit == 0;
        }
        return intranet;
    }

    /**
     * 判定ip地址是否在ipSegment这个地址段里(IP网段地址是否一致)
     */
    public static boolean ipInSegment(String ip, String ipSegment) {
        int[] ips = getNumericIpV4(ip), segmentIps = getNumericIpV4(ipSegment);
        //ips[5] = segmentIps[5];
        boolean intranet = isInternalIp(ips);
        int netmask = 0xFFFFFFFF << (32 - segmentIps[5]);
        return intranet && (ips[4] & netmask) == (segmentIps[4] & netmask);
    }

    /**
     * 获取IP网段的IP地址范围
     * @param ipSegment IP网段
     * @return [ Min, Max ]
     */
    public static int[] getIpSegmentRange(String ipSegment) {
        int[] ips = getNumericIpV4(ipSegment);
        int sb = ips[4], sm = ips[5];
        if (sm < 8 || sm > 32 || !isInternalIp(ips)) {
            throw new RuntimeException("Invalid IP segment(" + ipSegment + ")");
        }
        //
        String msg = "IpSegment(" + ipSegment + ").Usable-IP-Range = ";
        int ipRoot, ipRoof;
        if (sm == 32) {
            ipRoot = ipRoof = sb;
            msg += "(" + parseIpV4(sb) + ")<*>";
        } else if (sm == 31) {
            ipRoot = sb;
            ipRoof = sb + 1;
            msg += "(" + parseIpV4(ipRoof) + ")<1>";
        } else {
            // IP网段：首地址为网关地址，尾地址为广播地址
            ipRoof = 0xFFFFFFFF >>> ips[5] ^ 1;
            ipRoot = ips[4];
            ipRoof |= ipRoot;
            ipRoot |= 1;
            msg += "[" + parseIpV4(ipRoot) + ", " + parseIpV4(ipRoof) + "]<" + (ipRoof - ipRoot + 1) + ">";
        }
        System.out.println("\033[37;4m" + msg + "\033[0m");
        return new int[] {ipRoot, ipRoof};
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 拷贝自己
     * @param from 待拷贝对象
     * @param <T> 类名
     * @return T
     */
    public static <T> T copyProperties(T from) {
        if (from == null) {
            return null;
        } else {
            return (T) copyProperties(from, from.getClass());
        }
    }

    /**
     * 拷贝成目标Class的对象
     * @param from 待拷贝对象
     * @param toClazz 目标Class
     * @param <T> 类名
     * @return T
     */
    public static <T> T copyProperties(Object from, Class<T> toClazz) {
        T to = null;
        try {
            to = toClazz.newInstance();
            BeanUtil.copyProperties(from, to, new CopyOptions().ignoreNullValue().ignoreError());
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn(LogUtils.getSimpleMessages(e));
        }
        return to;
    }

    /**
     * 从from对象中拷贝数据到to对象
     * @param from 数据源
     * @param to 目标对象
     * @param fieldMap from属性 映射 to属性
     * @param copyNull 是否拷贝空值
     */
    public static void copyData(Object from, Object to, HashMap<String, String> fieldMap, boolean copyNull) {
        for (String field : fieldMap.keySet()) {
            Object value = ReflectionUtils.getFieldValueEx(from, field);
            if (!copyNull && value == null) {
                continue;
            }
            ReflectionUtils.setFieldValue(to, fieldMap.get(field), value);
        }
    }

    /**
     * 从from对象中拷贝数据到to对象
     * @param from 数据源
     * @param to 目标对象
     * @param fieldMap from属性 映射 to属性
     * @param copyNull 是否拷贝空值
     */
    public static void copyRecord(Object from, Record to, HashMap<String, String> fieldMap, boolean copyNull) {
        for (String field : fieldMap.keySet()) {
            Object value = ReflectionUtils.getFieldValueEx(from, field);
            if (!copyNull && value == null) {
                continue;
            }
            to.set(fieldMap.get(field), value);
        }
    }

    /**
     * 从from对象中拷贝数据到to对象(按from的key作为统一属性)
     * @param from 数据源
     * @param to 目标对象
     * @param copyNull 是否拷贝空值
     */
    public static void copyRecord(Map<String, Object> from, Record to, boolean copyNull) {
        Object value;
        for (String key : from.keySet()) {
            value = from.get(key);
            if (!copyNull && value == null) {
                continue;
            }
            to.set(key, value);
        }
    }

    /**
     * 从from对象中拷贝数据到to对象(按from的属性作为统一属性)
     * @param from 数据源
     * @param to 目标对象
     * @param copyNull 是否拷贝空值
     */
    public static void copyRecord(Record from, Record to, boolean copyNull) {
        String[] names = from.getColumnNames();
        Object[] values = from.getColumnValues();
        Object value;
        for (int i = 0; i < names.length; i++) {
            value = values[i];
            if (!copyNull && value == null) {
                continue;
            }
            to.set(names[i], value);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------

    private static final HashMap<String, ArrayList<Integer>> RANDOM_NUMBERS = new HashMap<>();

    public static void randomCycleClear() {
        RANDOM_NUMBERS.clear();
    }

    /**
     * Get a random number
     * @return [0, max)
     */
    public static int randomInt(int max) {
        return randomInt(0, max, null);
    }

    /**
     * Get a random number
     * @return [0, max)
     */
    public static int randomInt(int max, String cycle) {
        return randomInt(0, max, cycle);
    }

    /**
     * Get a random number
     * @return [min, max)
     */
    public static int randomInt(int min, int max) {
        return randomInt(min, max, null);
    }

    /**
     * Get a random number
     * @return [min, max)
     */
    public static int randomInt(int min, int max, String cycle) {
        max = Math.max(Math.max(max, min), 1); // 1 <= max <= ∞
        min = Math.max(Math.min(max, min), 0); // 0 <= min <= max
        if (max - min == 1) {
            return min;
        }
        int out = min + (int) (Math.random() * (max - min));
        if (isValid(cycle)) {
            ArrayList<Integer> numbers = RANDOM_NUMBERS.computeIfAbsent(cycle, k -> new ArrayList<>());
            if (numbers.isEmpty()) {
                for (int num = min; num < max; num++) {
                    numbers.add(num);
                }
            }
            int idx = randomInt(0, numbers.size(), null);
            out = numbers.get(idx);
            numbers.remove(idx);
        }
        return out;
    }

    /**
     * Get a random half collection
     * @return Collection
     */
    public static <T extends Collection<T>> Collection<T> randomHalf(Collection<T> data) {
        if (data == null || data.size() < 2) {
            return data;
        }
        List<T> out = new ArrayList<>();
        int size = data.size(), cnt = 0;
        Random rd = new Random();
        for (T obj : data) {
            if (rd.nextBoolean()) {
                cnt++;
                out.add(obj);
                if (cnt >= size / 2) {
                    break;
                }
            }
        }
        return out;
    }
}
