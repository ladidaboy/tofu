package cn.hl.ox.data;

import org.apache.commons.lang3.StringUtils;

/**
 * @author hyman
 * @date 2019-08-26 16:33:32
 * @version $ Id: Tester4StringUtils.java, v 0.1  hyman Exp $
 */
public class Tester4StringUtils {
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
        System.out.println(StringUtils.join(new String[]{txt, key}, ","));
        System.out.println(StringUtils.upperCase(txt));
        System.out.println(StringUtils.lowerCase(key));
        System.out.println(StringUtils.reverse(txt));
    }
}
