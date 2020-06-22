package com.tksflysun.im.connect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tksflysun.im"})
public class ConnectApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConnectApp.class, args);
    }
}
