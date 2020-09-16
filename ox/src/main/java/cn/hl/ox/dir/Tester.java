package cn.hl.ox.dir;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tester {
    private static final Log logger = LogFactory.getLog(Tester.class);

    public static void main(String[] args) {
        logger.info(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        logger.info(DirUtils.getCurrentClassPath());
        logger.info(DirUtils.getRootPath());
        logger.info(DirUtils.getUserDir());
    }
}
