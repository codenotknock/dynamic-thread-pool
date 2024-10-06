package com.xiaofu.dynamic.thread.pool.sdk.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaofu
 * @date 2024/10/05
 * @program dynamic-thread-pool
 * @description redis客户端配置: 动态线程池注册中心
 **/
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolRedisClientConfigProperties.class)
public class DynamicThreadPooRegistryRedisConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPooRegistryRedisConfig.class);

    /**
     * 创建 redisson 客户端实例
     *
     * @param applicationContext
     * @param properties
     * @return
     */
    @Bean("dynamicThreadRedissonClient")
    public RedissonClient redissonClient(ConfigurableApplicationContext applicationContext, DynamicThreadPoolRedisClientConfigProperties properties) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        try {
            config.useSingleServer()
                    .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                    .setPassword(properties.getPassword())
                    .setConnectionPoolSize(properties.getPoolSize())
                    .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                    .setIdleConnectionTimeout(properties.getIdleTimeout())
                    .setConnectTimeout(properties.getConnectTimeout())
                    .setRetryAttempts(properties.getRetryAttempts())
                    .setRetryInterval(properties.getRetryInterval())
                    .setPingConnectionInterval(properties.getPingInterval())
                    .setKeepAlive(properties.isKeepAlive());
            // 创建 Redisson 客户端实例
            RedissonClient redissonClient = Redisson.create(config);
            LOGGER.info("dynamic-thread-pool: Successfully created Redisson client with host: {}, port: {}", properties.getHost(), properties.getPort());
            return redissonClient;
        } catch (Exception e) {
            // 记录日志或抛出自定义异常
            LOGGER.error("dynamic-thread-pool: Failed to create Redisson client", e);
            throw new RuntimeException("dynamic-thread-pool: Failed to create Redisson client", e);
        }
    }
}
