package com.tksflysun.im.connect.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Starter implements CommandLineRunner {
    @Autowired
    private TcpServer tcpServer;
    @Autowired
    private UdpServer udpServer;

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            udpServer.start();
        }).start();
        tcpServer.start();
    }

}
