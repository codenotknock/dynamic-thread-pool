package com.xiaofu.dynamic.thread.pool.sdk.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.xiaofu.dynamic.thread.pool.sdk.registry.IRegistry;
import com.xiaofu.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.dto.UpdateThreadPoolConfigDTO;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 动态线程池变更监听
 **/

public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        LOGGER.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}", threadPoolConfigEntity.getThreadPoolName(), threadPoolConfigEntity.getPoolSize(), threadPoolConfigEntity.getMaximumPoolSize());
        dynamicThreadPoolService.updateThreadPoolConfig(UpdateThreadPoolConfigDTO.build(threadPoolConfigEntity));

        // 更新后上报最新数据
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);

        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        LOGGER.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
    }
}
