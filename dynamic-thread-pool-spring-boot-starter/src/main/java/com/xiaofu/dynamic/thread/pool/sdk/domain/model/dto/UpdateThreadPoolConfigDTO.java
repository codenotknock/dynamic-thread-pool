package com.xiaofu.dynamic.thread.pool.sdk.domain.model.dto;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 更新线程池配置参数
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateThreadPoolConfigDTO {

    /**
     * 应用程序名称
     */
    private String applicationName;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;

    /**
     * 队列容量
     */
    private Integer queueCapacity;

    public static UpdateThreadPoolConfigDTO build(ThreadPoolConfigEntity threadPoolConfigEntity) {
        UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO = new UpdateThreadPoolConfigDTO();

        updateThreadPoolConfigDTO.setApplicationName(threadPoolConfigEntity.getAppName());
        updateThreadPoolConfigDTO.setThreadPoolName(threadPoolConfigEntity.getThreadPoolName());
        updateThreadPoolConfigDTO.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        updateThreadPoolConfigDTO.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
        int queueCapacity =threadPoolConfigEntity.getRemainingCapacity() + threadPoolConfigEntity.getQueueSize();
        updateThreadPoolConfigDTO.setQueueCapacity(queueCapacity);

        return updateThreadPoolConfigDTO;
    }
}
