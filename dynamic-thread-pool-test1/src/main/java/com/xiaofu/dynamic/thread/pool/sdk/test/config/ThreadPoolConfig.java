package com.xiaofu.dynamic.thread.pool.sdk.test.config;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.hook.ResizableCapacityLinkedBlockingQueue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 线程池配置
 **/

@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor01(ThreadPoolConfigProperties properties) {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new ResizableCapacityLinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                getRejectedExecutionHandler(properties.getPolicy())
        );
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor02(ThreadPoolConfigProperties properties) {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new ResizableCapacityLinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                getRejectedExecutionHandler(properties.getPolicy())
        );
    }


    /**
     * 获取线程池拒绝策略
     *
     * @param policy
     * @return
     */
    private RejectedExecutionHandler getRejectedExecutionHandler(String policy) {
        switch (policy) {
            case "DiscardPolicy":
                return new ThreadPoolExecutor.DiscardPolicy();
            case "DiscardOldestPolicy":
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            case "CallerRunsPolicy":
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case "AbortPolicy":
                return new ThreadPoolExecutor.AbortPolicy();
            default:
                return new ThreadPoolExecutor.DiscardOldestPolicy();
        }
    }

}
