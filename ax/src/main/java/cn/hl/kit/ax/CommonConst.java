package cn.hl.kit.ax;

import cn.hl.kit.ax.enums.BaseEnumInterface;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author hyman
 * @date 2020-09-15 10:21:54
 */
public class CommonConst {
    /**
     * 数据格式化特效
     */
    public enum FormatPrettify implements BaseEnumInterface {
        /**
         * 无
         */
        NONE {
            @Override
            public int getValue() {
                return 0;
            }
        },
        /**
         * 日期: yyyy-MM-dd
         */
        DATE {
            @Override
            public int getValue() {
                return 1;
            }
        },
        /**
         * 枚举类: name(#)
         */
        ENUM {
            @Override
            public int getValue() {
                return 2;
            }
        },
        /**
         * 日期: yyyy-MM-dd HH:mm:ss
         */
        DATE_YMD {
            @Override
            public int getValue() {
                return 4;
            }
        },
    }

    /**
     * 数据比较特效
     */
    public enum CompareFeature implements BaseEnumInterface {
        /**
         * 无
         */
        NONE {
            @Override
            public int getValue() {
                return 0;
            }
        },
        /**
         * 空值相等
         */
        NULL_EQUALS {
            @Override
            public int getValue() {
                return 1;
            }
        },
        /**
         * 忽略大小写
         */
        IGNORE_CASE {
            @Override
            public int getValue() {
                return 2;
            }
        },
        /**
         * 日期只比较 年月日
         */
        DATE_YMD {
            @Override
            public int getValue() {
                return 3;
            }
        },
    }

    public static final SimpleDateFormat SDF_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat SDF_DTT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SDF_DAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final Pattern P_YMD  = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
    public static final Pattern P_LINE = Pattern.compile("_(\\w)");
    public static final Pattern P_HUMP = Pattern.compile("[A-Z]");

    public static final String F_DATE            = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
    public static final String F_DATETIME        = F_DATE + " ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
    public static final String F_DATETIME_MILLIS = F_DATETIME + "\\.[0-9]{3}";

    public static final String S_EMPTY = "";
    public static final String S_SPACE = " ";
    public static final String S_ERROR = "N/A";
    public static final String S_NIL   = "null";
}
