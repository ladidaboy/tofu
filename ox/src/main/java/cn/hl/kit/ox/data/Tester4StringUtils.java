package cn.hl.kit.ox.data;

import org.apache.commons.lang3.StringUtils;

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
public class Tester4StringUtils {
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

    /*
    判断函数：
    1.判断是否为空，返回boolean：
    StringUtils.isEmpty(String str)
    2.判断是否非空，返回boolean：
    StringUtils.isNotEmpty(String str)
    3.判断空白，返回boolean：
    StringUtils.isBlank(String str)
    4.判断非空白，返回boolean：
    StringUtils.isNotBlank(String str)
    5.判断是否存在空白（数组），返回boolean：
    StringUtils.isAnyBlank(CharSequence… css)
    6.判断是否存在空（数组），返回boolean：
    StringUtils.isAnyEmpty(CharSequence… css)
    7.判断不存在空白（数组），返回boolean：
    StringUtils.isNoneBlank(CharSequence… css)
    8.判断不存在空（数组），返回boolean：
    StringUtils.isNoneEmpty(CharSequence… css)
    9.判断是否空白，返回boolean：
    StringUtils.isWhitespace(CharSequence cs)

    大小写函数：
    1.首字母大写，返回String：
    StringUtils.capitalize(String str)
    2.首字母小写，返回String：
    StringUtils.uncapitalize(String str)
    3.全部大写，返回String：
    StringUtils.upperCase(String str)
    4.全部小写，返回String：
    StringUtils.lowerCase(String str)
    5.大小写互相转化，返回String：
    StringUtils.swapCase(String str)
    6.判断是否全大写，返回boolean：
    StringUtils.isAllUpperCase(CharSequence cs)
    7.判断是否全小写，返回boolean：
    StringUtils.isAllLowerCase(CharSequence cs)

    删除函数：
    1.从字符串中删除某字符，返回String：
    StringUtils.remove(String str, char remove)
    2.从字符串中删除字符串，返回String：
    StringUtils.remove(String str, String remove)
    3.删除结尾匹配的字符串，返回String：
    StringUtils.removeEnd(String str, String remove)
    4.删除结尾匹配的字符串，忽略大小写，返回String：
    StringUtils.removeEndIgnoreCase(String str, String remove)
    5.正则表达式删除字符串，返回String：
    StringUtils.removePattern(String source, String regex)
    6.删除开头匹配的字符串，返回String：
    StringUtils.removeStart(String str, String remove)
    StringUtils.removeStartIgnoreCase(String str, String remove)
    7.删除所有空格，包括中间，返回String：
    StringUtils.deleteWhitespace(String str)

    字符替换函数：
    1.用replacement替换searchString字符串，返回String；
    max表示替换个数，默认全替换，为-1，可不填。0表示不换。其他表示从头开始替换n个
    StringUtils.replace(String text, String searchString, String replacement, int max)
    2.仅替换一个，从头开始，返回String：
    StringUtils.replaceOnce(String text, String searchString, String replacement)
    3.多个替换, searchList与replacementList需一一对应，返回String：
    StringUtils.replaceEach(String text, String[] searchList, String[] replacementList)
    4.多个循环替换,searchList与replacementList需一一对应，返回String：
    StringUtils.replaceEachRepeatedly(String text, String[] searchList, String[] replacementList)
    5.替换start到end的字符，返回String：
    StringUtils.overlay(String str,String overlay,int start,int end)

    拆分合并函数：
    1.特定符号分割字符串，默认为空格，可不填，返回字符数组：
    StringUtils.split(String str)
    2.特定符合分割字符串为长度为n的字符数组,n为0，表示全拆，返回字符数组：
    StringUtils.split(String str, String separatorChars, int n)
    3.合并函数，数组合并为字符串：
    StringUtils.join(byte[] array,char separator)
    4. 合并函数，separator为合并字符，当为null时，表示简单合并，亦可不填；startIndex和endIndex表示合并数组该下标间的字符，使用separator字符，亦可不填，表示全合并。
    StringUtils.join(Object[] array,char separator,int startIndex,int endIndex)

    截取函数
    1.截取字符串，返回String：
    StringUtils.substring(String str,int start)
    2.从某字符后字符开始截取，返回String：
    StringUtils.substringAfter(String str,String separator)
    3.截取至最后一处该字符出现，返回String：
    StringUtils.substringBeforeLast(String str,String separator)
    4.从第一次该字符出现后截取，返回String：
    StringUtils.substringAfterLast(String str,String separator)
    5.截取某字符中间的子字符串，返回String：
    StringUtils.substringBetween(String str,String tag)

    删除空白函数
    1.删除空格，返回String：
    StringUtils.trim(String str)
    2.转换空格为empty，返回String：
    StringUtils.trimToEmpty(String str)
    3.转换空格为null，返回String：
    StringUtils.trimToNull(String str)
    4.删除所有空格，包括字符串中间空格，返回String：
    StringUtils.deleteWhitespace(String str)

    判断是否相等函数
    1.判断是否相等，返回boolean：
    StringUtils.equals(CharSequence cs1,CharSequence cs2)
    2…判断是否相等，忽略大小写，返回boolean：
    StringUtils.equalsIgnoreCase(CharSequence cs1,CharSequence cs2)

    是否包含函数
    1.判断第一个参数字符串，是否都出参数2中，返回boolean：
    StringUtils.containsOnly(CharSequence cs,char… valid)
    2.判断字符串中所有字符，都不在参数2中，返回boolean：
    StringUtils.containsNone(CharSequence cs,char… searchChars)
    3.判断字符串是否以第二个参数开始，返回boolean：
    StringUtils.startsWith(CharSequence str,CharSequence prefix)
    3.判断字符串是否以第二个参数开始，忽略大小写，返回boolean：
    StringUtils.startsWithIgnoreCase(CharSequence str,CharSequence prefix)
     */

    public static void main(String[] args) {
        String txt = "hellO", key = "WOrld";
        System.out.println(StringUtils.join(new String[] {txt, key}, ","));
        System.out.println(StringUtils.upperCase(txt));
        System.out.println(StringUtils.lowerCase(key));
        System.out.println(StringUtils.reverse(txt));
    }
}
