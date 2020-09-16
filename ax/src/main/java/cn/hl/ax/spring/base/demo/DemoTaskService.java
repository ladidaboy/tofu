/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * @author hyman
 * @date 2019-12-13 17:21:14
 */
@Service
public class DemoTaskService {
    private static Logger logger = LoggerFactory.getLogger(DemoTaskService.class);

    private static void sleepWhile(int sec, String title) {
        try {
            sec = Math.min(Math.max(sec, 1), 16);
            logger.info(title + " sleep(" + sec + ")");
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public Future<Map<String, Object>> asyncReturnFuture(int sec) {
        logger.info("async ➣ Future start...");
        Random rd = new Random();

        sleepWhile(sec, "async ➣ Future");

        Map<String, Object> result = new HashMap<>();
        result.put("Random", rd.nextInt(Integer.MAX_VALUE));
        result.put("Thread", Thread.currentThread().getName());

        logger.info("async ➣ Future finish!!");
        return new AsyncResult<>(result);
    }

    @Async
    public void asyncReturnVoid(int sec) {
        logger.info("async ➢ (void) start...");

        sleepWhile(sec, "async ➢ (void)");

        logger.info("async ➢ (void) finish!!");
    }

    @Async
    public void asyncReturnParam(int sec, Param p) {
        logger.info("async → &param start...");

        sleepWhile(sec, "async → &param");

        if (p != null) {
            p.value = Thread.currentThread().getName();
        }

        logger.info("async → &param finish!!");
    }

    public static class Param {
        private String value;

        @Override
        public String toString() {
            return "Param{value: '" + value + "'}";
        }
    }
}
