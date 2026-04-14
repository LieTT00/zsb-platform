package com.zsb.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.zsb.community.mapper")
public class ZsbCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZsbCommunityApplication.class, args);
    }
}
