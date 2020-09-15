package cn.hl.ox.log;

import lombok.extern.log4j.Log4j;

/**
 * @author hyman
 * @date 2020-09-14 16:56:06
 * @version $ Id: Demo.java, v 0.1  hyman Exp $
 */
@Log4j
public class Demo {
    public static void main(String[] args) {
        log.debug("窗前明月光");
        log.info("疑是地上霜");
        log.warn("举头望明月");
        log.error("低头思故乡");
    }
}
