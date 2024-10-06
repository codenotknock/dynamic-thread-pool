package com.xiaofu.dynamic.thread.pool.sdk.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 测试
 **/
@RestController
public class TestController {


    @Resource
    private ThreadPoolExecutor threadPoolExecutor02;

    @GetMapping("/dos")
    public void submitLogsOfThreads() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                threadPoolExecutor02.submit(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(60 * 2);
                    } catch (Exception ignored) {
                    }
                });
            }
        }).start();
    }
}
