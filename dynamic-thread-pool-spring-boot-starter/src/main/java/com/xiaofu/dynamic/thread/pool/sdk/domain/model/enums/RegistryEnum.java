package com.xiaofu.dynamic.thread.pool.sdk.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 注册中心枚举值对象
 **/

@AllArgsConstructor
@Getter
public enum RegistryEnum {

    THREAD_POOL_CONFIG_LIST_KEY("THREAD_POOL_CONFIG_LIST_KEY", "线程池配置列表"),

    THREAD_POOL_CONFIG_PARAMETER_LIST_KEY("THREAD_POOL_CONFIG_PARAMETER_LIST_KEY", "线程池配置参数列表"),

    DYNAMIC_THREAD_POOL_ADJUST_REDIS_TOPIC_KEY("DYNAMIC_THREAD_POOL_ADJUST_REDIS_TOPIC_KEY", "动态线程池REDIS主题配置"),

    REPORT_THREAD_POOL_CONFIG_LIST_REDIS_LOCK_KEY("REPORT_THREAD_POOL_CONFIG_LIST_REDIS_LOCK_KEY", "锁");

    private final String key;

    private final String description;
}