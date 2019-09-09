package cn.hl.ax.data;

import cn.hl.ax.JavaBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * http://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html
 * <br/><br/><hr/>
 * <b>Operations on String that are null safe.</b>
 * <ul><li>IsEmpty/IsBlank - checks if a String contains text
 * </li><li>Trim/Strip - removes leading and trailing whitespace
 * </li><li>Equals/Compare - compares two strings null-safe
 * </li><li>startsWith - check if a String starts with a prefix null-safe
 * </li><li>endsWith - check if a String ends with a suffix null-safe
 * </li><li>IndexOf/LastIndexOf/Contains - null-safe index-of checks
 * </li><li>IndexOfAny/LastIndexOfAny/IndexOfAnyBut/LastIndexOfAnyBut - index-of any of a set of Strings
 * </li><li>ContainsOnly/ContainsNone/ContainsAny - does String contains only/none/any of these characters
 * </li><li>Substring/Left/Right/Mid - null-safe substring extractions
 * </li><li>SubstringBefore/SubstringAfter/SubstringBetween - substring extraction relative to other strings
 * </li><li>Split/Join - splits a String into an array of substrings and vice versa
 * </li><li>Remove/Delete - removes part of a String
 * </li><li>Replace/Overlay - Searches a String and replaces one String with another
 * </li><li>Chomp/Chop - removes the last part of a String
 * </li><li>AppendIfMissing - appends a suffix to the end of the String if not present
 * </li><li>PrependIfMissing - prepends a prefix to the start of the String if not present
 * </li><li>LeftPad/RightPad/Center/Repeat - pads a String
 * </li><li>UpperCase/LowerCase/SwapCase/Capitalize/Uncapitalize - changes the case of a String
 * </li><li>CountMatches - counts the number of occurrences of one String in another
 * </li><li>IsAlpha/IsNumeric/IsWhitespace/IsAsciiPrintable - checks the characters in a String
 * </li><li>DefaultString - protects against a null input String
 * </li><li>Rotate - rotate (circular shift) a String
 * </li><li>Reverse/ReverseDelimited - reverses a String
 * </li><li>Abbreviate - abbreviates a string using ellipsis or another given String
 * </li><li>Difference - compares Strings and reports on their differences
 * </li><li>LevenshteinDistance - the number of changes needed to change one String into another
 * </li></ul>
 * <b>The StringUtils class defines certain words related to String handling.</b>
 * <ul><li>null - null
 * </li><li>empty - a zero-length string ("")
 * </li><li>space - the space character (' ', char 32)
 * </li><li>whitespace - the characters defined by Character.isWhitespace(char)
 * </li><li>trim - the characters <= 32 as in String.trim()
 * </li></ul>
 *
 * <p>StringUtils handles null input Strings quietly. That is to say that a null input will return null.
 * Where a boolean or int is being returned details vary by method.</p><br/>
 *
 * <p>A side effect of the null handling is that a NullPointerException should be considered a bug in StringUtils.</p><br/>
 *
 * <p>Methods in this class give sample code to explain their operation.
 * The symbol * is used to indicate any input including null.</p>
 * @author hyman
 */
public class DataUtils extends StringUtils {
    /*
    public static boolean isEmpty(CharSequence cs)
    常用函数之一，判断字符串是否为""或者null
    StringUtils.isEmpty(null)      = true
    StringUtils.isEmpty("")        = true
    StringUtils.isEmpty(" ")       = false
    StringUtils.isEmpty("bob")     = false
    StringUtils.isEmpty("  bob  ") = false

    public static boolean isNotEmpty(CharSequence cs)
    最常用函数之一，跟上面方法相对
    StringUtils.isNotEmpty(null)      = false
    StringUtils.isNotEmpty("")        = false
    StringUtils.isNotEmpty(" ")       = true
    StringUtils.isNotEmpty("bob")     = true
    StringUtils.isNotEmpty("  bob  ") = true

    public static boolean isAnyEmpty(CharSequence... css)
    任意一个参数为空的话，返回true，如果这些参数都不为空的话返回false。
    在写一些判断条件的时候，这个方法还是很实用的。
    StringUtils.isAnyEmpty(null)             = true
    StringUtils.isAnyEmpty(null, "foo")      = true
    StringUtils.isAnyEmpty("", "bar")        = true
    StringUtils.isAnyEmpty("bob", "")        = true
    StringUtils.isAnyEmpty("  bob  ", null)  = true
    StringUtils.isAnyEmpty(" ", "bar")       = false
    StringUtils.isAnyEmpty("foo", "bar")     = false

    public static boolean isNoneEmpty(CharSequence... css)
    任意一个参数是空，返回false, 所有参数都不为空，返回true
    StringUtils.isNoneEmpty(null)             = false
    StringUtils.isNoneEmpty(null, "foo")      = false
    StringUtils.isNoneEmpty("", "bar")        = false
    StringUtils.isNoneEmpty("bob", "")        = false
    StringUtils.isNoneEmpty("  bob  ", null)  = false
    StringUtils.isNoneEmpty(" ", "bar")       = true
    StringUtils.isNoneEmpty("foo", "bar")     = true

    public static boolean isBlank(CharSequence cs)
    判断字符对象是不是空字符串，注意与isEmpty的区别
    StringUtils.isBlank(null)      = true
    StringUtils.isBlank("")        = true
    StringUtils.isBlank(" ")       = true
    StringUtils.isBlank("bob")     = false
    StringUtils.isBlank("  bob  ") = false

    public static boolean isNotBlank(CharSequence cs)
    StringUtils.isNotBlank(null)      = false
    StringUtils.isNotBlank("")        = false
    StringUtils.isNotBlank(" ")       = false
    StringUtils.isNotBlank("bob")     = true
    StringUtils.isNotBlank("  bob  ") = true

    原理同上
    public static boolean isAnyBlank(CharSequence... css)
    StringUtils.isAnyBlank(null)             = true
    StringUtils.isAnyBlank(null, "foo")      = true
    StringUtils.isAnyBlank(null, null)       = true
    StringUtils.isAnyBlank("", "bar")        = true
    StringUtils.isAnyBlank("bob", "")        = true
    StringUtils.isAnyBlank("  bob  ", null)  = true
    StringUtils.isAnyBlank(" ", "bar")       = true
    StringUtils.isAnyBlank("foo", "bar")     = false

    public static boolean isNoneBlank(CharSequence... css)
    StringUtils.isNoneBlank(null)             = false
    StringUtils.isNoneBlank(null, "foo")      = false
    StringUtils.isNoneBlank(null, null)       = false
    StringUtils.isNoneBlank("", "bar")        = false
    StringUtils.isNoneBlank("bob", "")        = false
    StringUtils.isNoneBlank("  bob  ", null)  = false
    StringUtils.isNoneBlank(" ", "bar")       = false
    StringUtils.isNoneBlank("foo", "bar")     = true

    public static String trim(String str)
    移除字符串两端的空字符串，制表符char <= 32如：\n \t
    如果为空的话，返回空，如果为""，返回""
    StringUtils.trim(null)          = null
    StringUtils.trim("")            = ""
    StringUtils.trim("     ")       = ""
    StringUtils.trim("abc")         = "abc"
    StringUtils.trim("    abc    ") = "abc"
    变体有
    public static String trimToNull(String str)
    public static String trimToEmpty(String str)
    不常用，跟trim()方法类似
    public static String strip(String str)
    public static String strip(String str, String stripChars)
    str：被处理的字符串，可为空；stripChars： 删除的字符串。
    StringUtils.strip(null, *)          = null
    StringUtils.strip("", *)            = ""
    StringUtils.strip("abc", null)      = "abc"
    StringUtils.strip("  abc", null)    = "abc"
    StringUtils.strip("abc  ", null)    = "abc"
    StringUtils.strip(" abc ", null)    = "abc"
    StringUtils.strip("  abcyx", "xyz") = "  abc"

    public static boolean equals(CharSequence cs1, CharSequence cs2)
    字符串比对方法，是比较实用的方法之一，两个比较的字符串都能为空，不会报空指针异常。
    StringUtils.equals(null, null)   = true
    StringUtils.equals(null, "abc")  = false
    StringUtils.equals("abc", null)  = false
    StringUtils.equals("abc", "abc") = true
    StringUtils.equals("abc", "ABC") = false

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2)
    上面方法的变体。字符串比较（忽略大小写），在验证码……等字符串比较，真是很实用。 
    StringUtils.equalsIgnoreCase(null, null)   = true
    StringUtils.equalsIgnoreCase(null, "abc")  = false
    StringUtils.equalsIgnoreCase("abc", null)  = false
    StringUtils.equalsIgnoreCase("abc", "abc") = true
    StringUtils.equalsIgnoreCase("abc", "ABC") = true

    public static int indexOf(CharSequence seq, int searchChar)
    indexOf这个方法不必多说，这个方法主要处理掉了空字符串的问题，不会报空指针，有一定用处
    StringUtils.indexOf(null, *)         = -1
    StringUtils.indexOf("", *)           = -1
    StringUtils.indexOf("aabaabaa", 'a') = 0
    StringUtils.indexOf("aabaabaa", 'b') = 2

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal)
    字符串在另外一个字符串里，出现第Ordinal次的位置 
    StringUtils.ordinalIndexOf(null, *, *)          = -1
    StringUtils.ordinalIndexOf(*, null, *)          = -1
    StringUtils.ordinalIndexOf("", "", *)           = 0
    StringUtils.ordinalIndexOf("aabaabaa", "a", 1)  = 0
    StringUtils.ordinalIndexOf("aabaabaa", "a", 2)  = 1
    StringUtils.ordinalIndexOf("aabaabaa", "b", 1)  = 2
    StringUtils.ordinalIndexOf("aabaabaa", "b", 2)  = 5
    StringUtils.ordinalIndexOf("aabaabaa", "ab", 1) = 1
    StringUtils.ordinalIndexOf("aabaabaa", "ab", 2) = 4
    StringUtils.ordinalIndexOf("aabaabaa", "", 1)   = 0
    StringUtils.ordinalIndexOf("aabaabaa", "", 2)   = 0

    public static int lastIndexOf(CharSequence seq, int searchChar)
    字符串最后一次出现的位置
    StringUtils.lastIndexOf(null, *)         = -1
    StringUtils.lastIndexOf("", *)           = -1
    StringUtils.lastIndexOf("aabaabaa", 'a') = 7
    StringUtils.lastIndexOf("aabaabaa", 'b') = 5

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal)
    字符串searchStr在str里面出现倒数第ordinal出现的位置
    StringUtils.lastOrdinalIndexOf(null, *, *)          = -1
    StringUtils.lastOrdinalIndexOf(*, null, *)          = -1
    StringUtils.lastOrdinalIndexOf("", "", *)           = 0
    StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
    StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
    StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
    StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
    StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
    StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
    StringUtils.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
    StringUtils.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8

    public static boolean contains(CharSequence seq, int searchChar)
    字符串seq是否包含searchChar
    StringUtils.contains(null, *)    = false
    StringUtils.contains("", *)      = false
    StringUtils.contains("abc", 'a') = true
    StringUtils.contains("abc", 'z') = false

    public static boolean containsAny(CharSequence cs, char... searchChars)
    包含后面数组中的任意对象，返回true
    StringUtils.containsAny(null, *)                = false
    StringUtils.containsAny("", *)                  = false
    StringUtils.containsAny(*, null)                = false
    StringUtils.containsAny(*, [])                  = false
    StringUtils.containsAny("zzabyycdxx",['z','a']) = true
    StringUtils.containsAny("zzabyycdxx",['b','y']) = true
    StringUtils.containsAny("aba", ['z'])           = false

    public static String substring(String str, int start)
    字符串截取 
    StringUtils.substring(null, *)   = null
    StringUtils.substring("", *)     = ""
    StringUtils.substring("abc", 0)  = "abc"
    StringUtils.substring("abc", 2)  = "c"
    StringUtils.substring("abc", 4)  = ""
    StringUtils.substring("abc", -2) = "bc"
    StringUtils.substring("abc", -4) = "abc"

    public static String left(String str, int len)
    public static String right(String str, int len)
    public static String mid(String str, int pos, int len)
    这三个方法类似都是截取字符串

    public static String[] split(String str, String separatorChars)
    字符串分割 
    StringUtils.split(null, *)         = null
    StringUtils.split("", *)           = []
    StringUtils.split("abc def", null) = ["abc", "def"]
    StringUtils.split("abc def", " ")  = ["abc", "def"]
    StringUtils.split("abc  def", " ") = ["abc", "def"]
    StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]

    public static <T> String join(T... elements)
    字符串连接
    StringUtils.join(null)            = null
    StringUtils.join([])              = ""
    StringUtils.join([null])          = ""
    StringUtils.join(["a", "b", "c"]) = "abc"
    StringUtils.join([null, "", "a"]) = "a"

    public static String join(Object[] array, char separator)
    特定字符串连接数组，很多情况下还是蛮实用，不用自己取拼字符串 
    StringUtils.join(null, *)               = null
    StringUtils.join([], *)                 = ""
    StringUtils.join([null], *)             = ""
    StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
    StringUtils.join(["a", "b", "c"], null) = "abc"
    StringUtils.join([null, "", "a"], ';')  = ";;a"

    public static String deleteWhitespace(String str)
    删除空格 
    StringUtils.deleteWhitespace(null)         = null
    StringUtils.deleteWhitespace("")           = ""
    StringUtils.deleteWhitespace("abc")        = "abc"
    StringUtils.deleteWhitespace("   ab  c  ") = "abc"

    public static String removeStart(String str, String remove)
    删除以特定字符串开头的字符串，如果没有的话，就不删除。 
    StringUtils.removeStart(null, *)      = null
    StringUtils.removeStart("", *)        = ""
    StringUtils.removeStart(*, null)      = *
    StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
    StringUtils.removeStart("domain.com", "www.")       = "domain.com"
    StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
    StringUtils.removeStart("abc", "")    = "abc"

    public static String rightPad(String str, int size, char padChar)
    生成订单号的时候还是很实用的。右边自动补齐。 
    StringUtils.rightPad(null, *, *)     = null
    StringUtils.rightPad("", 3, 'z')     = "zzz"
    StringUtils.rightPad("bat", 3, 'z')  = "bat"
    StringUtils.rightPad("bat", 5, 'z')  = "batzz"
    StringUtils.rightPad("bat", 1, 'z')  = "bat"
    StringUtils.rightPad("bat", -1, 'z') = "bat"

    public static String leftPad(String str, int size, char padChar)
    左边自动补齐 
    StringUtils.leftPad(null, *, *)     = null
    StringUtils.leftPad("", 3, 'z')     = "zzz"
    StringUtils.leftPad("bat", 3, 'z')  = "bat"
    StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
    StringUtils.leftPad("bat", 1, 'z')  = "bat"
    StringUtils.leftPad("bat", -1, 'z') = "bat"

    public static String center(String str, int size)
    将字符在某特定长度下，句子 
    StringUtils.center(null, *)   = null
    StringUtils.center("", 4)     = "    "
    StringUtils.center("ab", -1)  = "ab"
    StringUtils.center("ab", 4)   = " ab "
    StringUtils.center("abcd", 2) = "abcd"
    StringUtils.center("a", 4)    = " a  "


    public static String capitalize(String str)
    首字母大写
    StringUtils.capitalize(null)  = null
    StringUtils.capitalize("")    = ""
    StringUtils.capitalize("cat") = "Cat"
    StringUtils.capitalize("cAt") = "CAt"

    public static String swapCase(String str)
    反向大小写 
    StringUtils.swapCase(null)                 = null
    StringUtils.swapCase("")                   = ""
    StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"

    public static boolean isAlpha(CharSequence cs)
    判断字符串是否由字母组成 
    StringUtils.isAlpha(null)   = false
    StringUtils.isAlpha("")     = false
    StringUtils.isAlpha("  ")   = false
    StringUtils.isAlpha("abc")  = true
    StringUtils.isAlpha("ab2c") = false
    StringUtils.isAlpha("ab-c") = false

    public static String defaultString(String str, String defaultStr)
    默认字符串，相当于三目运算，前面弱为空，则返回后面一个参数 
    StringUtils.defaultString(null, "NULL")  = "NULL"
    StringUtils.defaultString("", "NULL")    = ""
    StringUtils.defaultString("bat", "NULL") = "bat"

    public static String reverse(String str)
    字符串翻转
    StringUtils.reverse(null)  = null
    StringUtils.reverse("")    = ""
    StringUtils.reverse("bat") = "tab"

    public static String abbreviate(String str, int maxWidth)
    缩略字符串，省略号要占三位。maxWith小于3位会报错。
    StringUtils.abbreviate(null, *)      = null
    StringUtils.abbreviate("", 4)        = ""
    StringUtils.abbreviate("abcdefg", 6) = "abc..."
    StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
    StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
    StringUtils.abbreviate("abcdefg", 4) = "a..."
    StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException

    public static String abbreviate(String str, int offset, int maxWidth)
    缩略字符串的一些高级用法 
    StringUtils.abbreviate(null, *, *)                = null
    StringUtils.abbreviate("", 0, 4)                  = ""
    StringUtils.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
    StringUtils.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
    StringUtils.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
    StringUtils.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
    StringUtils.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
    StringUtils.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
    StringUtils.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
    StringUtils.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
    StringUtils.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
    StringUtils.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
    StringUtils.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException

    public static String wrap(String str, char wrapWith)
    包装，用后面的字符串对前面的字符串进行包装 
    StringUtils.wrap(null, *)        = null
    StringUtils.wrap("", *)          = ""
    StringUtils.wrap("ab", '\0')     = "ab"
    StringUtils.wrap("ab", 'x')      = "xabx"
    StringUtils.wrap("ab", '\'')     = "'ab'"
    StringUtils.wrap("\"ab\"", '\"') = "\"\"ab\"\""
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 检查字符是否在[a~z,A~Z,0~9,汉字]中
     * @param ch 字符
     * @return true:合法, false:非法
     */
    public static boolean isValid(char ch) {
        return (ch >= 0x4E00 && ch <= 0X9FA5) //
                || (ch >= 'a' && ch <= 'z')   //
                || (ch >= 'A' && ch <= 'Z')   //
                || (ch >= '0' && ch <= '9');  //
    }

    /**
     * 检查JAVA Bean对象是否合法
     * <br>此方法首先会过滤掉Bean对象的id属性，然后检查所有其他属性是否都为null
     * @param bean 待检查对象
     * @return false: JAVA Bean所有属性(除了id)均为null, true: JAVA Bean有属性不为null
     */
    public static boolean isValid(Object bean) {
        return !isInvalid(bean);
    }

    /**
     * 检查JAVA Bean对象是否合法
     * <br>此方法首先会过滤掉Bean对象的id属性，然后检查所有其他属性是否都为null
     * @param bean 待检查对象
     * @return true: JAVA Bean所有属性(除了id)均为null, false: JAVA Bean有属性不为null
     */
    public static boolean isInvalid(Object bean) {
        if (bean == null) {
            return true;
        }
        try {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("get") && !method.getName().equals("getId")) {
                    if (method.invoke(bean) != null) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 检查status里是否包含flag
     * @param status -- flag1 | flag2 | ...
     * @param flag -- 2^n
     * @return true: status包含flag
     */
    public static boolean statusHasFlag(int status, int flag) {
        return status > 0 && (status & flag) == flag;
    }

    /**
     * 检查status是否为单一flag
     * @param status -- flag1 | flag2 | ...
     * @return true:单一, false:非单一
     */
    public static boolean isMonoStatus(int status) {
        return status > 0 && (status & (status - 1)) == 0;
    }

    /**
     * 检查status所有的状态位是否都为真
     * @param status -- flag1 | flag2 | ...
     * @param ranks -- flag的类型数量
     * @return true:全真, false:非全真
     */
    public static boolean isFullStatus(int status, int ranks) {
        return status > 0 && status == (int) (Math.pow(2, ranks) - 1);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /** BLANK数据默认值 */
    public static final String BLANK_DATA_TAG = "";
    /** ERROR数据默认值 */
    public static final String ERROR_DATA_TAG = "N/A";

    /**
     * 获取有效的数据值(Valid Value)<br>
     * 若传进来的数据为NULL则返回系统BLANK_DATA_TAG
     * @param value
     * @return
     */
    public static String getVVb(String value) {
        return isBlank(value) ? BLANK_DATA_TAG : value;
    }

    /**
     * 获取有效的数据值(Valid Value)<br>
     * 若传进来的数据为NULL则返回系统ERROR_DATA_TAG
     * @param value
     * @return
     */
    public static String getVVe(String value) {
        return isBlank(value) ? ERROR_DATA_TAG : value;
    }

    /**
     * 去除文本中的非法字符
     * @param txt
     * @return
     */
    public static String removeSign(String txt) {
        StringBuffer sb = new StringBuffer();
        for (char cc : txt.toCharArray())
            if (DataUtils.isValid(cc)) {
                sb.append(cc);
            }
        return sb.toString();
    }

    /**
     * 获取字符串 字节长度
     * @param txt
     * @return
     */
    public static int getSize(String txt) {
        if (txt == null)
            return -1;
        int cnt = 0;
        for (int i = 0; i < txt.length(); i++) {
            cnt += (String.valueOf(txt.charAt(i)).getBytes().length > 1) ? 2 : 1;
        }
        return cnt;
    }

    /**
     * 格式化字符串至指定长度
     * @param txt
     * @param len
     * @return
     */
    public static String rightPadEx(String txt, int len) {
        return rightPadEx(txt, len, (char) 32);//
    }

    /**
     * 格式化字符串至指定长度
     * @param txt
     * @param len
     * @param fill
     * @return
     */
    public static String rightPadEx(String txt, int len, char fill) {
        txt = txt == null ? "" : txt;
        int txtlen = getSize(txt);
        if (txtlen > len) {
            String _txt_ = txt;
            txt = "";
            int cnt = 0, cl;
            char ch;
            for (int i = 0; i < _txt_.length(); i++) {
                ch = _txt_.charAt(i);
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
            for (; txtlen < len; txtlen++) {
                txt += fill;
            }
        }
        return txt;
    }

    /**
     * <style>table{border-right:1px solid #ccc;border-bottom:1px solid #ccc;margin:2px;}
     * th,td{border-left:1px solid #ccc;border-top:1px solid #ccc;padding:2px 4px;}</style>
     * 格式化数值
     * @param value -- 原数值
     * @param pattern -- 格式字符串, 如果pattern为null则默认保留2位小数(四舍五入)
     * <table border=0 cellpadding=0 cellspacing=0>
     * 	<tr><th>符号</th><th>含义</th><th>例子</th></tr>
     * 	<tr><td align=center>0</td><td>阿拉伯数字,如果不存在则显示为 0</td><td>eg: 0000.00 --> 123.3435=0123.34 ; 12345.6543=12345.65 ; 12345=12345.00</td></tr>
     * 	<tr><td align=center>#</td><td>阿拉伯数字</td><td>eg: ####.## --> 123.3435=123.34 ; 12345.6543=12345.65 ; 12345=12345</td></tr>
     * 	<tr><td align=center>.</td><td>小数分隔符或货币小数分隔符</td><td>eg: ####.## --> 123.3435=123.34 ;</td></tr>
     * 	<tr><td align=center>,</td><td>分组分隔符</td><td>eg: #,###.## --> 123456.3435=123,456.3435 ; 123123456.3435=123,123,456.3435</td></tr>
     * 	<tr><td align=center>%</td><td> 前缀或后缀  是  乘以 100 并显示为百分数</td><td>eg: #.##% --> 0.9867=98.67% ; </td></tr>
     * 	<tr><td align=center>\u2030</td><td>前缀或后缀  是  乘以 1000 并显示为千分数 </td><td>eg: #.##% --> 0.98678=986.78‰ ; </td></tr>
     * </table>
     * @return	String 格式后的字符串
     * @throws Exception
     */
    public static String fmtValue(Double value, String pattern) throws Exception {
        DecimalFormat format = null;
        if (pattern == null) {
            format = new DecimalFormat("0.00");
        } else {
            format = new DecimalFormat(pattern);
        }
        return format.format(value);
    }

    /**
     * 获取百分比数
     * @param molecular -- 分子
     * @param denominator -- 分母
     * @param formater -- 百分比数格式，为null时默认使用[###0.00]
     * @return
     */
    public static String getRateByVals(String molecular, String denominator, String formater) throws Exception {
        double iMolecular = Double.parseDouble(molecular);
        double iDenominator = Double.parseDouble(denominator);
        double rate = iMolecular / iDenominator * 100;
        if (formater == null || formater.trim().equals("")) formater = "###0.00";
        DecimalFormat dFormater = new java.text.DecimalFormat(formater);
        return dFormater.format(rate) + "%";
    }

    /**
     * 将字符串性的数据值或百分数值格式化去掉小数点后的数值
     * <br>百分数值格式化后保留"%"
     * @param value -- 待格式化值
     * @return
     */
    public static String formatVal2Int(String value) throws Exception {
        return formatVal(value, null);
    }

    /**
     * 将字符串性的数据值或百分数值格式化成制定的格式
     * <br>百分数值格式化后保留"%"
     * @param value -- 待格式化值
     * @param formater -- 格式化格式,null值时使用默认格式"###################0"
     * @return
     */
    public static String formatVal(String value, String formater) throws Exception {
        if (value == null)
            throw new Exception("Invalid value.");
        if (formater == null || formater.trim().equals(""))
            formater = "###################0";
        DecimalFormat dFormater = new java.text.DecimalFormat(formater);
        try {
            value = dFormater.format(Double.parseDouble(value));
        } catch (Exception ee) {
            try {
                int len = value.length() - 1;
                value = dFormater.format(Double.parseDouble(value.substring(0, len))) + value.substring(len);
            } catch (Exception eee) {
            }
        }
        return value;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    private static HashMap<String, ArrayList<Integer>> numberMap = new HashMap<>();

    public static void randomCycleClear() {
        numberMap.clear();
    }

    /**
     * Get a random number
     * @param max
     * @return [0, max)
     */
    public static int randomInt(int max) {
        return randomInt(0, max, null);
    }

    /**
     * Get a random number
     * @param max
     * @param cycle
     * @return [0, max)
     */
    public static int randomInt(int max, String cycle) {
        return randomInt(0, max, cycle);
    }

    /**
     * Get a random number
     * @param min
     * @param max
     * @return [min, max)
     */
    public static int randomInt(int min, int max) {
        return randomInt(min, max, null);
    }

    /**
     * Get a random number
     * @param min
     * @param max
     * @param cycle
     * @return [min, max)
     */
    public static int randomInt(int min, int max, String cycle) {
        max = Math.max(Math.max(max, min), 1); // 1 <= max <= ∞
        min = Math.max(Math.min(max, min), 0); // 0 <= min <= max
        if (max - min == 1) {
            return min;
        }
        int out = min + (int) (Math.random() * (max - min));
        if (cycle != null && !cycle.trim().equals("")) {
            ArrayList<Integer> numbers = numberMap.get(cycle);
            if (numbers == null) {
                numbers = new ArrayList<Integer>();
                numberMap.put(cycle, numbers);
            }
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        JavaBean bean = new JavaBean();
        bean.setId("No.99");
        System.out.println(bean + " - Valid: " + isValid(bean));
        bean.setAge(9999);
        System.out.println(bean + " - Valid: " + isValid(bean));
        //
        System.out.println("statusHasFlag(12, 2) → " + statusHasFlag(12, 2));
        System.out.println("isFullStatus(15, 4) → " + isFullStatus(15, 4));
        System.out.println("isMonoStatus(9) → " + isMonoStatus(9));
        //
        System.out.println("randomInt(8) ← " + randomInt(8));
        String sp;
        for (int i = 0; i < 22; i++) {
            sp = i / 10 % 2 == 0 ? " → " : " ← ";
            System.out.println("randomInt(10, MONO)" + sp + randomInt(10, "MONO"));
        }
        System.out.println("randomInt(256, 512) ← " + randomInt(256, 512));
    }
}
