package com.xiaofu.dynamic.thread.pool.sdk.trigger.job;

import com.alibaba.fastjson2.JSON;
import com.xiaofu.dynamic.thread.pool.sdk.registry.IRegistry;
import com.xiaofu.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 线程池数据上报任务
 **/

public class ThreadPoolDataReportJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private IDynamicThreadPoolService dynamicThreadPoolService;

    private IRegistry registry;

    public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "${dynamic.thread.pool.registry.reportCron}")
    public void reportThreadPoolData() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);

        LOGGER.info("动态线程池, 上报线程池信息: {}", JSON.toJSONString(threadPoolConfigEntities));

        // 遍历每个线程池信息, 上报配置信息
        threadPoolConfigEntities.forEach(threadPoolConfigEntity -> {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);

            LOGGER.info("动态线程池, 上报线程池配置信息: {}", JSON.toJSONString(threadPoolConfigEntity));
        });
    }
}