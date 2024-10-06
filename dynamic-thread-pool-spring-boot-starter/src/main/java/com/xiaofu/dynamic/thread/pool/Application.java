package com.xiaofu.dynamic.thread.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Hello world!
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) {
        System.out.println("reportCron: " + env.getProperty("dynamic.thread.pool.registry.report-cron"));
    }
}