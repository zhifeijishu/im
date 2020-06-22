package com.tksflysun.im.process.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.process.service.IMsgReqAckService;
import com.tksflysun.im.process.util.SpringBeanUtils;

/**
 * tcp 往udp中转消息任务
 * 
 * @author lpf
 *
 */
public class MsgReqAckTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MsgReqAckTask.class);
    private Im.TcpPackage tcpPackage;

    public MsgReqAckTask(Im.TcpPackage tcpPackage) {
        this.tcpPackage = tcpPackage;
    }

    @Override
    public void run() {
        try {
            Im.MsgReqAck msgReqAck = Im.MsgReqAck.parseFrom(tcpPackage.getContent());
            ((IMsgReqAckService)SpringBeanUtils.getBean("msgReqAckService")).reqAck(msgReqAck);
            // TODO 处理应答包
        } catch (Throwable e) {
            logger.error("解析消息请求ack包出错", e);
        }
    }

}
