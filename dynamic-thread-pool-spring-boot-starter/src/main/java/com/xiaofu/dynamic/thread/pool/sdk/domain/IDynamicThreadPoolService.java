package com.xiaofu.dynamic.thread.pool.sdk.domain;

import com.xiaofu.dynamic.thread.pool.sdk.domain.model.dto.UpdateThreadPoolConfigDTO;
import com.xiaofu.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 动态线程池服务
 **/

public interface IDynamicThreadPoolService {

    /**
     * 查询所有线程池配置列表
     *
     * @return
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     * 根据线程池名称查询线程池配置
     *
     * @param threadPoolName
     * @return
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     * 更新线程池配置
     *
     * @param updateThreadPoolConfigDTO
     */
    Boolean updateThreadPoolConfig(UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO);

}
