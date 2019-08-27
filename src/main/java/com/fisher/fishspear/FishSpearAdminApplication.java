package com.fisher.fishspear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FishSpearAdminApplication {
    //springboot程序启动入口
    public static void main(String[] args) {
        SpringApplication.run(FishSpearAdminApplication.class, args);
    }
}
