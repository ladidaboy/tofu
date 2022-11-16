package cn.hl.ox.exp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hyman
 * @date 2019-08-27 17:52:11
 */
public class Tester {
    static Log logger = LogFactory.getLog(Tester.class);

    static class RootException extends RuntimeException {
        public RootException() {
            super("Ops! *-*");
        }

        static void start() {
            method1();
        }

        static void method1() {
            logger.info("RootException in method 1");
            method2();
        }

        static void method2() {
            logger.info("RootException in method 2");
            method3();
        }

        static void method3() {
            logger.info("RootException in method 3");
            method4();
        }

        static void method4() {
            logger.info("RootException in method 4");
            throw new RootException();
        }
    }

    static class LeafException extends RuntimeException {
        public LeafException() {
            super("LoL! ^_^");
        }

        @Override
        public Throwable fillInStackTrace() {
            return this;
        }

        static void start() {
            method1();
        }

        static void method1() {
            logger.info("LeafException in method 1");
            method2();
        }

        static void method2() {
            logger.info("LeafException in method 2");
            method3();
        }

        static void method3() {
            logger.info("LeafException in method 3");
            method4();
        }

        static void method4() {
            logger.info("LeafException in method 4");
            throw new LeafException();
        }
    }

    public static void main(String[] args) {
        try {
            RootException.start();
        } catch (Exception ex) {
            //ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
        }

        try {
            LeafException.start();
        } catch (Exception ex) {
            //ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
        }

        try {
            g();
        } catch (Exception ex) {
            //ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void f() throws Exception {
        throw new Exception("出问题啦！");
    }

    public static void g() throws Exception {
        logger.debug("Test for fillInStackTrace");
        try {
            f();
        } catch (Exception e) {
            e.printStackTrace();
            //throw e;
            //不要忘了强制类型转换
            throw (Exception) e.fillInStackTrace();
        }
    }

}
