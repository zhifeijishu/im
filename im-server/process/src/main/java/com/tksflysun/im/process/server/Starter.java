package com.tksflysun.im.process.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Starter implements CommandLineRunner {

    @Autowired
    private UdpServer udpServer;

    @Override
    public void run(String... args) throws Exception {
        udpServer.start();
    }

}
