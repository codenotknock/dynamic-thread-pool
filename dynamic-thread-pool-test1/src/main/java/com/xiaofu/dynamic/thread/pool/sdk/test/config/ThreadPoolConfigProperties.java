package com.xiaofu.dynamic.thread.pool.sdk.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 线程池配置属性
 **/

@Data
@ConfigurationProperties(prefix = "thread.pool.executor.config", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

    private Integer corePoolSize;

    private Integer maxPoolSize;

    private Integer keepAliveTime;

    private Integer blockQueueSize;

    private String policy;
}
