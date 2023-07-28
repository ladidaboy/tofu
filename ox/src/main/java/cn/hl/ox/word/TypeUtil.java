package cn.hl.ox.word;

import cn.hl.ax.enums.EnumUtils;

/**
 * 对读取字符分类相关操作
 */
public class TypeUtil {
    public static final char LINE_CHAR    = '\n';
    public static final char OPERATORS[]  = {'+', '-', '*', '/', '=', '>', '<', '&', '|', '!'}; // 运算符数组
    public static final char SEPARATORS[] = {',', ';', '{', '}', '(', ')', '[', ']', '_', ':', '.', '"', '\''}; // 分界符数组

    /**
     * 判断是否为数字
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    /**
     * 判断是否为字母
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    /**
     * 判断是否为关键字
     * @param ss 需判断的字符串
     * @return boolean
     */
    public boolean isKeyWord(String ss) {
        try {
            KeyType key = EnumUtils.fromName(KeyType.class, ss.toUpperCase());
            int o = key.ordinal();
            return o > KeyType.KA.ordinal() && o <= KeyType.KZ.ordinal();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为运算符
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isOperator(char ch) {
        for (char operator : OPERATORS) {
            if (ch == operator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为分隔符
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isSeparators(char ch) {
        for (char separator : SEPARATORS) {
            if (ch == separator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为引号
     * @param ch 需判断的字符
     * @return boolean
     */
    public boolean isQuotation(char ch) {
        return ch == '"' || ch == '\'';
    }
}
