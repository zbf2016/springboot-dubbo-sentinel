package com.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 * @title: ProviderApplication
 * @projectName dubbo-springboot
 * @description: TODO
 * @date 2021/11/20 18:59
 */
@SpringBootApplication
@MapperScan("com.dubbo.mapper")
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class,args);
    }
}
