package cn.hl.ax.data;

import cn.hl.ax.CommonConst;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static cn.hl.ax.data.ArrayUtils.differ;
import static cn.hl.ax.data.ArrayUtils.intersect;
import static cn.hl.ax.data.ArrayUtils.maxIntersect;
import static cn.hl.ax.data.ArrayUtils.union;

/**
 * @author hyman
 * @date 2020-09-16 10:46:49
 * @version $ Id: ArrayUtilsTests.java, v 0.1  hyman Exp $
 */
@Slf4j
public class ArrayUtilsTests {
    @Test
    public void test4intersect() {
        String[] s2 = {"22", "ac", "13", "de", "3C", "de", "Hi", "pq", "vv", "zZ", "TT", "po", "vs", "i8", "gt", "3C", "6p"};
        String[] s1 = {"11", "bd", "56", "i8", "Hi", "pq", "wx", "zZ", "po"};

        String[] intersect12 = intersect(s1, s2);
        log.info("ⓘ 交集是：{}", StrUtil.join(CommonConst.S_SPACE, intersect12));

        String[] differ12 = differ(s1, s2);
        log.info("ⓘ 差集是：{}", StrUtil.join(CommonConst.S_SPACE, differ12));

        String[] union12 = union(s1, s2);
        log.info("ⓘ 并集是：{}", StrUtil.join(CommonConst.S_SPACE, union12));

        String[] join12 = DataUtils.add2Array(s1, s2);
        log.info("ⓘ 原始集：{}", StrUtil.join(CommonConst.S_SPACE, join12));
    }

    @Test
    public void test4maxIntersect() {
        String t1 = "Apache Commons Logging is a thin adapter allowing configurable bridging to others, well known logging systems.";
        String t2 = "A SLF4J implementation which delegates to maven-plugin logging toolkit.";
        String text = maxIntersect(t1, t2);
        String[] texts = intersect(t1.split(CommonConst.S_SPACE), t2.split(CommonConst.S_SPACE));
        log.info("maxIntersect -> '{}'", text);
        log.info("intersect [] -> '{}'", StrUtil.join("', '", texts));
    }
}
