package cn.hl.ax.ascii;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static cn.hl.ax.ascii.Chinese2Pinyin.getFullSpell;

/**
 * @author hyman
 * @date 2020-09-16 10:33:51
 */
@Slf4j
public class Chinese2PinyinTests {
    @Test
    public void test4getFullSpell() {
        String str;
        boolean blank = true;
        str = "➢ 上海101,普降喜雨";
        log.info(str + " --> " + getFullSpell(str, blank));

        str = "➣ 张牙舞爪，啊》。";
        log.info(str + " --> " + getFullSpell(str, blank));

        str = "➢ 小强，可耻下场。";
        log.info(str + " --> " + getFullSpell(str, blank));

        str = "➣ 中文";
        log.info(str + " --> " + getFullSpell(str, blank));

        str = "➢ 忠雯";
        log.info(str + " --> " + getFullSpell(str, blank));
    }
}
