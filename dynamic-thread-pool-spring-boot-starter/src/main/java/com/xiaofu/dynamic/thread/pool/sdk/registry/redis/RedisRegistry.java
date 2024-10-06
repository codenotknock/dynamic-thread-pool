package com.xiaofu.dynamic.thread.pool.sdk.registry.redis;

import com.xiaofu.dynamic.thread.pool.sdk.registry.IRegistry;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.enums.RegistryEnum;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description Redis 实现注册中心
 **/

public class RedisRegistry implements IRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistry.class);

    private RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities) {
        if (CollectionUtils.isEmpty(threadPoolEntities)) {
            return;
        }
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnum.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.delete();
        list.addAll(threadPoolEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnum.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
