package com.tksflysun.im.process.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.process.service.IMsgReqService;
import com.tksflysun.im.process.util.SpringBeanUtils;

/**
 * tcp 往udp中转消息任务
 * 
 * @author lpf
 *
 */
public class MsgReqTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MsgReqTask.class);
    private Im.TcpPackage tcpPackage;

    public MsgReqTask(Im.TcpPackage tcpPackage) {
        this.tcpPackage = tcpPackage;
    }

    @Override
    public void run() {
        try {
            Im.MsgReq msgReq = Im.MsgReq.parseFrom(tcpPackage.getContent());
            ((IMsgReqService)SpringBeanUtils.getBean("msgReqService")).getMsgList(msgReq);
            // TODO消息请求包处理
        } catch (Throwable e) {
            logger.error("解析消息请求包出错", e);
        }
    }

}
