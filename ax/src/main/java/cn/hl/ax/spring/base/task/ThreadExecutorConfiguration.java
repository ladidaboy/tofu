/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hyman
 * @date 2019-12-13 16:49:12
 */
@Configuration
@EnableAsync
//@ComponentScan("com.zenlayer.oss.base")
public class ThreadExecutorConfiguration {
    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池维护线程的最少数量
        executor.setCorePoolSize(20);
        //线程池维护线程的最大数量
        executor.setMaxPoolSize(100);
        //缓存队列
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("OSS-Executor-");
        /**
         * 对拒绝TASK的处理策略(AbortPolicy/CallerRunsPolicy/DiscardPolicy/DiscardOldestPolicy)
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //允许的空闲时间
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}
