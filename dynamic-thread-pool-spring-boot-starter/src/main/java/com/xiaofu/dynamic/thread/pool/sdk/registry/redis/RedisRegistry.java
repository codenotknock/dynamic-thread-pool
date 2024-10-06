package com.xiaofu.dynamic.thread.pool.sdk.registry.redis;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.enums.RegistryEnum;
import com.xiaofu.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        RMap<String, ThreadPoolConfigEntity> rMap = redissonClient.getMap(RegistryEnum.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        RLock lock = redissonClient.getLock(RegistryEnum.REPORT_THREAD_POOL_CONFIG_LIST_REDIS_LOCK_KEY.getKey());
        try {
            boolean acquired = lock.tryLock(3000, 3000, TimeUnit.MILLISECONDS);
            if (!acquired) {
                // 不做处理, 等待下一次定时任务的上报即可
                LOGGER.info("动态线程池, 上报线程池列表时获取锁失败, 不做处理，等待下一次定时任务的上报");
            } else {
                reportThreadPoolRealProcess(threadPoolEntities, rMap);
            }
        } catch (Exception e) {
            LOGGER.error("动态线程池, 上报线程池列表时出现错误: {}", e.toString());
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnum.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" +
                threadPoolConfigEntity.getApplicationName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }


    private void reportThreadPoolRealProcess(List<ThreadPoolConfigEntity> threadPoolConfigEntities,
                                             RMap<String, ThreadPoolConfigEntity> rMap) {
        Map<String, ThreadPoolConfigEntity> batchUpdates = new HashMap<>();
        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            String threadPoolName = threadPoolConfigEntity.getThreadPoolName();
            batchUpdates.put(threadPoolName, threadPoolConfigEntity);
        }
        try {
            rMap.putAll(batchUpdates);
        } catch (Exception e) {
            LOGGER.error("批量更新线程池配置时出现错误: {}", e.getMessage(), e);
        }
    }

}
