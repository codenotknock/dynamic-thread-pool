package com.xiaofu.dynamic.thread.pool.sdk.registry;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 注册中心接口
 **/

public interface IRegistry {

    /**
     * 上报线程池
     *
     * @param threadPoolEntities
     */
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    /**
     * 上报单个线程池配置参数
     *
     * @param threadPoolConfigEntity 单个线程池配置实体
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
