/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.task.ThreadExecutorConfiguration;
import cn.hl.ax.spring.base.demo.DemoTaskService.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * @author hyman
 * @date 2019-12-13 17:00:35
 */
@Component
public class Test4Task {
    private static Logger logger = LoggerFactory.getLogger(Test4Task.class);
    private static Param  p1, p2, p3, p4;
    private static int bound = 16;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThreadExecutorConfiguration.class);
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThreadExecutorAsyncConfigurer.class);
        DemoTaskService instance = context.getBean(DemoTaskService.class);

        test4HasResult(instance);
        test4NulResult(instance);
        test4ParResult(instance);
        test4MixResult(instance);

        logger.error("Param(p1): " + p1);
        logger.error("Param(p2): " + p2);
        logger.error("Param(p3): " + p3);
        logger.error("Param(p4): " + p4);
    }

    private static void test4HasResult(DemoTaskService instance) {
        logger.debug("ⓘRUN test4HasResult begin...");
        List<Future<Map<String, Object>>> futures = new ArrayList<>();

        // 以下调用会启动线程进行执行，多线程并发
        Future<Map<String, Object>> future1 = instance.asyncReturnFuture(9);
        futures.add(future1);
        Future<Map<String, Object>> future2 = instance.asyncReturnFuture(1);
        futures.add(future2);

        // 会等待所有线程都执行结束，拿到结果
        try {
            for (Future<Map<String, Object>> future : futures) {
                Map<String, Object> result = future.get();
                logger.info(future.isDone() + " >> " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("ⓘRUN test4HasResult complete");
    }

    private static void test4NulResult(DemoTaskService instance) {
        Random rd = new Random();
        logger.debug("ⓘRUN test4NulResult begin...");

        // 以下方法会启动多线程，并发执行
        instance.asyncReturnVoid(rd.nextInt(bound));
        instance.asyncReturnVoid(rd.nextInt(bound));
        instance.asyncReturnVoid(rd.nextInt(bound));

        logger.debug("ⓘRUN test4NulResult complete");
    }

    private static void test4ParResult(DemoTaskService instance) {
        logger.debug("ⓘRUN test4ParResult begin...");
        Random rd = new Random();

        p1 = new Param();
        instance.asyncReturnParam(rd.nextInt(bound), p1);
        logger.info("test4ParResult(p1): " + p1);

        p2 = new Param();
        instance.asyncReturnParam(rd.nextInt(bound), p2);
        logger.info("test4ParResult(p2): " + p2);

        logger.debug("ⓘRUN test4ParResult complete");
    }

    private static void test4MixResult(DemoTaskService instance) {
        Random rd = new Random();
        logger.debug("ⓘRUN test4MixResult start...");
        Future<Map<String, Object>> future;

        {
            // ✲ asyncReturnFuture会阻断后续程序的执行，为了等待此函数的返回结果
            future = instance.asyncReturnFuture(2);
            try {
                logger.info(future.isDone() + " >> " + future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ✲ asyncReturnVoid由于无返回结果，所有不会阻断后续程序的执行
            instance.asyncReturnVoid(rd.nextInt(bound));

            p3 = new Param();
            // ✲ asyncReturnParam由于无返回结果，所以与asyncReturnVoid表现一致
            instance.asyncReturnParam(rd.nextInt(bound), p3);
            logger.info("test4ParResult(p3): " + p3);
        }

        {
            future = instance.asyncReturnFuture(4);
            try {
                logger.info(future.isDone() + " >> " + future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            future = instance.asyncReturnFuture(6);
            try {
                logger.info(future.isDone() + " >> " + future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }

            instance.asyncReturnVoid(rd.nextInt(bound));

            p4 = new Param();
            instance.asyncReturnParam(rd.nextInt(bound), p4);
            logger.info("test4ParResult(p4): " + p4);
        }

        logger.debug("ⓘRUN test4MixResult complete");
    }
}
