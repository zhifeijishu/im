package com.tksflysun.im.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tksflysun.im"})
@EnableTransactionManagement
public class ProcessApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProcessApp.class, args);
    }
}
