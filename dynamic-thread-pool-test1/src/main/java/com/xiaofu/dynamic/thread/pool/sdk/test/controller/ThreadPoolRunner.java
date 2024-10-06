package com.xiaofu.dynamic.thread.pool.sdk.test.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofu
 * @date 2024/10/06
 * @program dynamic-thread-pool
 * @description 测试
 **/

@Component
public class ThreadPoolRunner implements ApplicationRunner {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor01;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    Random random = new Random();
                    int startDuration = random.nextInt(5) + 1;
                    int runDuration = random.nextInt(10) + 1;

                    threadPoolExecutor01.submit(() -> {
                        try {
                            TimeUnit.SECONDS.sleep(startDuration);
                            System.out.printf("启动花费时间: %ds\n", startDuration);

                            TimeUnit.SECONDS.sleep(runDuration);
                            System.out.printf("运行花费时间: %ds\n", runDuration);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    Thread.sleep((random.nextInt(10) + 1) * 1000);
                }
            } catch (Exception e) {
            }
        });
        t.start();
    }
}
