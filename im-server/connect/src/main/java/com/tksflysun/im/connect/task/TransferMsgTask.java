package com.tksflysun.im.connect.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.protobuf.format.JsonFormat;
import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.UdpPackage;
import com.tksflysun.im.connect.server.UdpServer;

/**
 * tcp消息转换为udp发送给process
 * 
 * @author lpf
 *
 */
public class TransferMsgTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TransferMsgTask.class);
    private Im.TcpPackage tcpPackage;

    public TransferMsgTask(Im.TcpPackage tcpPackage) {
        this.tcpPackage = tcpPackage;
    }

    @Override
    public void run() {
        logger.warn("接收到客户端消息", JsonFormat.printToString(tcpPackage));
        UdpPackage udpPackage = Im.UdpPackage.newBuilder().setContent(tcpPackage.toByteString()).build();
        logger.warn("转换为updPackage", JsonFormat.printToString(udpPackage));
        UdpServer.sendMsg(udpPackage);

    }

}
