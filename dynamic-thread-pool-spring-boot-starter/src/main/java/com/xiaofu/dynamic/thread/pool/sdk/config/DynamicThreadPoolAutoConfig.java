package com.xiaofu.dynamic.thread.pool.sdk.config;

import com.alibaba.fastjson2.JSON;
import com.xiaofu.dynamic.thread.pool.sdk.registry.IRegistry;
import com.xiaofu.dynamic.thread.pool.sdk.registry.redis.RedisRegistry;
import com.xiaofu.dynamic.thread.pool.sdk.domain.DynamicThreadPoolService;
import com.xiaofu.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.enums.RegistryEnum;
import com.xiaofu.dynamic.thread.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import com.xiaofu.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigAdjustListener;
import jodd.util.StringUtil;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 动态线程池配置类
 **/
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolRegistryProperties.class)
@EnableScheduling
@Import(DynamicThreadPooRegistryRedisConfig.class)
public class DynamicThreadPoolAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    private String applicationName;

    @Bean("dynamicThreadPoolAService")
    public DynamicThreadPoolService dynamicThreadPoolAService(
            ApplicationContext applicationContext,
            Map<String, ThreadPoolExecutor> threadPoolExecutorMap,
            RedissonClient redissonClient) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

        if (StringUtil.isBlank(applicationName)) {
            applicationName = "缺省的";
            LOGGER.warn("动态线程池，启动提示。SpringBoot 应用未配置 spring.application.name 无法获取到应用名称！");
        }

        // 获取缓存数据，设置本地线程池
        Set<String> threadPoolExecutorKeys = threadPoolExecutorMap.keySet();
        for (String key : threadPoolExecutorKeys) {
            ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(
                    RegistryEnum.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + applicationName + "_" + key).get();
            if (null == threadPoolConfigEntity) {
                continue;
            }
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(key);
            threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
            threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
        }
        LOGGER.info("线程池信息：{}", JSON.toJSONString(threadPoolExecutorKeys));

        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }

    @Bean
    public IRegistry redisRegistry(RedissonClient dynamicThreadRedissonClient) {
        return new RedisRegistry(dynamicThreadRedissonClient);
    }

    @Bean
    public ThreadPoolDataReportJob threadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolDataReportJob(dynamicThreadPoolService, registry);
    }

    @Bean
    public ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolConfigAdjustListener(dynamicThreadPoolService, registry);
    }
    @Bean(name = "dynamicThreadPoolRedisTopic")
    public RTopic threadPoolConfigAdjustListener(RedissonClient redissonClient, ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener) {
        RTopic topic = redissonClient.getTopic(RegistryEnum.DYNAMIC_THREAD_POOL_ADJUST_REDIS_TOPIC_KEY.getKey() + "_" + applicationName);
        topic.addListener(ThreadPoolConfigEntity.class, threadPoolConfigAdjustListener);
        return topic;
    }


}
