package com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaofu
 * @date 2024/10/05
 * @program dynamic-thread-pool
 * @description 线程池配置实体
 **/

@Data
@AllArgsConstructor
public class ThreadPoolConfigEntity {

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maximumPoolSize;

    /**
     * 当前活跃线程数
     */
    private int activeCount;

    /**
     * 当前线程池大小线程数
     */
    private int poolSize;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * 队列大小
     */
    private int queueSize;

    /**
     * 队列剩余容量
     */
    private int remainingCapacity;


    public ThreadPoolConfigEntity() {
    }

    public ThreadPoolConfigEntity(String applicationName, String threadPoolName) {
        this.applicationName = applicationName;
        this.threadPoolName = threadPoolName;
    }


    /**
     * 建造一个线程池配置对象
     *
     * @param applicationName 应用名
     * @param threadPoolName  线程池名
     * @param executor        线程池执行器对象
     * @return 线程池配置对象
     */
    public static ThreadPoolConfigEntity build(String applicationName, String threadPoolName, ThreadPoolExecutor executor) {
        ThreadPoolConfigEntity configEntity = new ThreadPoolConfigEntity();

        configEntity.setApplicationName(applicationName);
        configEntity.setThreadPoolName(threadPoolName);
        configEntity.setCorePoolSize(executor.getCorePoolSize());
        configEntity.setMaximumPoolSize(executor.getMaximumPoolSize());
        configEntity.setActiveCount(executor.getActiveCount());
        configEntity.setPoolSize(executor.getPoolSize());
        configEntity.setQueueType(executor.getQueue().getClass().getSimpleName());
        configEntity.setQueueSize(executor.getQueue().size());
        configEntity.setRemainingCapacity(executor.getQueue().remainingCapacity());

        return configEntity;
    }
}
