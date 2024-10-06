package com.xiaofu.dynamic.thread.pool.sdk.domain;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.dto.UpdateThreadPoolConfigDTO;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.hook.ResizableCapacityLinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 动态线程池服务
 **/

public class DynamicThreadPoolService implements IDynamicThreadPoolService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPoolService.class);


    private String applicationName;

    private Map<String, ThreadPoolExecutor> threadPoolExecutorMap;


    public DynamicThreadPoolService(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        ArrayList<ThreadPoolConfigEntity> threadPoolList = new ArrayList<>();
        threadPoolExecutorMap.forEach((beanName, executor) -> {
            threadPoolList.add(ThreadPoolConfigEntity.build(applicationName, beanName, executor));
        });

        return threadPoolList;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        return ThreadPoolConfigEntity.build(applicationName, threadPoolName, threadPoolExecutorMap.get(threadPoolName));
    }

    @Override
    public Boolean updateThreadPoolConfig(UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO) {
        if (updateThreadPoolConfigDTO == null) {
            return false;
        }

        if (!Objects.equals(updateThreadPoolConfigDTO.getApplicationName(), applicationName)) {
            return false;
        }
        String threadPoolName = updateThreadPoolConfigDTO.getThreadPoolName();
        if (threadPoolName == null) {
            return false;
        }
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor == null) {
            return false;
        }
        Integer corePoolSize = updateThreadPoolConfigDTO.getCorePoolSize();
        Integer maximumPoolSize = updateThreadPoolConfigDTO.getMaximumPoolSize();
        // CorePoolSize 小于等于 MaximumPoolSize
        if (maximumPoolSize < corePoolSize) {
            LOGGER.error("动态线程池, 变更配置时出错(最大线程数小于核心线程数): {}", updateThreadPoolConfigDTO);
            return false;
        }

        // 变更时注意设置值的顺序, 始终满足CorePoolSize 小于等于 MaximumPoolSize
        if (corePoolSize < threadPoolExecutor.getMaximumPoolSize()) {
            threadPoolExecutor.setCorePoolSize(updateThreadPoolConfigDTO.getCorePoolSize());
            threadPoolExecutor.setMaximumPoolSize(updateThreadPoolConfigDTO.getMaximumPoolSize());
        } else {
            threadPoolExecutor.setMaximumPoolSize(updateThreadPoolConfigDTO.getMaximumPoolSize());
            threadPoolExecutor.setCorePoolSize(updateThreadPoolConfigDTO.getCorePoolSize());
        }

        // 变更阻塞队列的大小
        ResizableCapacityLinkedBlockingQueue queue =
                (ResizableCapacityLinkedBlockingQueue) threadPoolExecutor.getQueue();
        queue.setCapacity(updateThreadPoolConfigDTO.getQueueCapacity());

        return true;
    }

}


