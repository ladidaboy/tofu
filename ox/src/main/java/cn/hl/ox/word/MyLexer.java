package cn.hl.ox.word;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.enume.EnumUtils;
import cn.hl.ox.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析
 * 关键字，运算符一符一类
 * 标识符，常数，分隔符各自一类
 * 运算符未处理组合运算 ++、--、+= 等
 */
public class MyLexer extends TypeUtil {
    private class OI {
        KeyType type;
        String  chars;

        OI(KeyType type, String chars) {
            this.type = type;
            this.chars = chars;
        }

        @Override
        public String toString() {
            String msg = "#" + (type.ordinal() < 10 ? "0" : "") + type.ordinal();
            msg += " [" + DataUtils.rightPad(type.name(), 12) + "]";
            msg += " ➢ " + (type == KeyType.WRAP ? "\\n" : chars);
            return msg;
        }
    }

    private int    ii;
    private char   ch;
    private String src, txt;
    private List<OI> ret = new ArrayList<>();

    public MyLexer() {
    }

    /**
     * 词法分析
     */
    public void analyse(File file) throws IOException {
        ii = 0;
        txt = "";
        ret.clear();
        src = FileUtil.readFile(file);
        while (ii < src.length()) {
            getChar();
            getBC();
            if (isLetter(ch)) { // 如果ch为字母
                while (isLetter(ch) || isDigit(ch)) {
                    concat();
                    getChar();
                }
                retract(); // 回调
                if (isKeyWord(txt)) {
                    saveLog(EnumUtils.fromName(KeyType.class, txt.toUpperCase()), txt); // word为关键字
                } else {
                    saveLog(KeyType.ID, txt); // word为标识符
                }
                txt = "";
            } else if (isDigit(ch)) {
                while (isDigit(ch)) { // ch为数字
                    concat();
                    getChar();
                }
                if (!isLetter(ch)) { // 不能数字+字母
                    retract(); // 回调
                    saveLog(KeyType.DIGIT, txt); // 是整形
                } else {
                    saveLog(KeyType.ERROR, txt); // 非法
                }
                txt = "";
            } else if (isOperator(ch)) { // 运算符
                if (ch == '/') {
                    getChar();
                    if (ch == '*') { // 为/*注释
                        while (true) {
                            getChar();
                            if (ch == '*') { // 为多行注释结束
                                getChar();
                                if (ch == '/') {
                                    getChar();
                                    break;
                                }
                            }
                        }
                        saveLog(KeyType.COMMENT, "/***/");
                    }
                    if (ch == '/') { // 为//单行注释
                        while (ch != TypeUtil.LINE_CHAR) {
                            getChar();
                        }
                        saveLog(KeyType.COMMENT, "//***");
                        //saveLog("linewrap", TypeUtil.LINE_CHAR);
                    }
                    retract();
                }
                switch (ch) {
                    case '+':
                        saveLog(KeyType.PLUS, ch);
                        break;
                    case '-':
                        saveLog(KeyType.MIN, ch);
                        break;
                    case '*':
                        saveLog(KeyType.MUL, ch);
                        break;
                    case '/':
                        saveLog(KeyType.DIV, ch);
                        break;
                    case '>':
                        saveLog(KeyType.GT, ch);
                        break;
                    case '<':
                        saveLog(KeyType.LT, ch);
                        break;
                    case '=':
                        saveLog(KeyType.EQ, ch);
                        break;
                    case '&':
                        saveLog(KeyType.AND, ch);
                        break;
                    case '|':
                        saveLog(KeyType.OR, ch);
                        break;
                    case '~':
                        saveLog(KeyType.NOT, ch);
                        break;
                    default:
                        break;
                }
            } else if (isQuotation(ch)) { // 字符串
                char tag = ch, prev;
                do {
                    concat();
                    prev = ch;
                    getChar();
                } while (tag != ch || (tag == ch && prev == '\\'));
                concat();
                saveLog(KeyType.STRING, txt);
                txt = "";
            } else if (isSeparators(ch)) { // 分界符
                saveLog(KeyType.SEPARATORS, ch);
            } else {
                saveLog(KeyType.ERROR, ch);
            }
        }
    }

    /**
     * 将下一个输入字符读到ch中，搜索指示器前移一个字符
     */
    private void getChar() {
        ch = src.charAt(ii);
        ii++;
    }

    /** 检查ch中的字符是否为空白，若是则调用getChar()直至ch中进入一个非空白字符*/
    private void getBC() {
        // 确定指定字符依据 Java 标准是否为空白字符
        if (ii < src.length()) {
            while (Character.isWhitespace(ch)) {
                getChar();
            }
        }
    }

    /**将ch连接到word之后*/
    private void concat() {
        txt += ch;
    }

    /** 将搜索指示器回调一个字符位置，将ch值为空白字 */
    private void retract() {
        ii--;
        ch = ' ';
    }

    /**
     * 保存日志
     * @param type 字符类型
     * @param cc 当前字符
     */
    private void saveLog(KeyType type, char cc) {
        saveLog(type, cc + "");
    }

    /**
     * 保存日志
     * @param type 字符类型
     * @param chars 当前字符
     */
    private void saveLog(KeyType type, String chars) {
        type = type == null ? KeyType.ERROR : type;
        OI oi = new OI(type, chars);
        System.out.println(oi);
        ret.add(oi);
    }

    public void display() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0, k = 1; j < ret.size(); j++, k++) {
            OI _this_ = ret.get(j);
            if (_this_.type == KeyType.COMMENT) {
                continue;
            }
            sb.append(_this_.chars);
            if (k < ret.size() && check(_this_) && check(ret.get(k))) {
                sb.append(' ');
            }
        }
        System.out.println("➣ After compression: \n" + sb.toString());
    }

    private boolean check(OI oi) {
        int o = oi.type.ordinal();
        return (o > KeyType.KA.ordinal() && o < KeyType.KZ.ordinal()) || oi.type == KeyType.ID;
    }

    public static void main(String[] args) {
        try {
            MyLexer lexer = new MyLexer();
            URI uri = MyLexer.class.getResource("TypeUtil.txt").toURI();
            lexer.analyse(new File(uri));
            lexer.display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}