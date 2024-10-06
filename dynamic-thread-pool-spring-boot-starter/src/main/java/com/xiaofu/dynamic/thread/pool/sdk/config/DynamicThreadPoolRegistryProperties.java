package com.xiaofu.dynamic.thread.pool.sdk.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 注册任务配置
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "dynamic.thread.pool.registry")
public class DynamicThreadPoolRegistryProperties {

    private String reportCron = "0/20 * * * * ?";
}
