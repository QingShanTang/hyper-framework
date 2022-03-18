package org.qingshan.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.qingshan.dao"})
@MapperScan(basePackages = {"org.qingshan.dao.mapper"})
public class HyperWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyperWebApplication.class, args);
    }

}
