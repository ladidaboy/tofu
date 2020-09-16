package cn.hl.ax.data;

/**
 * 进制转换器
 * @author Hyman
 */
public class AryTransfer {
    public static final char[] ARY_TAGS_36  = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    };
    public static final char[] ARY_TAGS_26  = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    };
    public static final char[] ARY_TAGS_CHN = new char[] {
            'ā', 'á', 'ǎ', 'à', 'ō', 'ó', 'ǒ', 'ò', 'ē', 'é', 'ě', 'è', 'ê',
            'ī', 'í', 'ǐ', 'ì', 'ū', 'ú', 'ǔ', 'ù', 'ǖ', 'ǘ', 'ǚ', 'ǜ', 'ü',
    };

    public static String toAry(long decimal, char[] tags) {
        if (decimal <= 0) {
            return String.valueOf(tags[0]);
        }
        int residue, radix = tags.length;
        String ary = "";
        while (decimal > 0) {
            residue = (int) (decimal % radix);
            ary = tags[residue] + ary;
            decimal /= radix;
        }
        return ary;
    }

    public static String to26Ary(long decimal) {
        return toAry(decimal, ARY_TAGS_26);
    }
}
