package com.zzzlew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zzzlew
 * @Date: 2026/4/24 - 04 - 24 - 18:33
 * @Description: com.zzzlew.config
 * @version: 1.0
 */
@Configuration
public class ThreadPoolConfig {
    @Bean("imAsyncExecutor")
    public ExecutorService imAsyncExecutor() {
        int corePoolSize = Math.max(2, Runtime.getRuntime().availableProcessors());
        return new ThreadPoolExecutor(
                corePoolSize,          // 核心线程数
                corePoolSize * 2,      // 最大线程数
                60L,                   // 空闲存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),  // 有界队列，防止OOM
                new ThreadPoolExecutor.CallerRunsPolicy()  // 队列满时由调用线程执行
        );
    }
}
