package com.tksflysun.im.process.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.process.service.IMsgSendService;
import com.tksflysun.im.process.util.SpringBeanUtils;

/**
 * tcp 往udp中转消息任务
 * 
 * @author lpf
 *
 */
public class MsgSendTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MsgSendTask.class);
    private Im.TcpPackage tcpPackage;

    public MsgSendTask(Im.TcpPackage tcpPackage) {
        this.tcpPackage = tcpPackage;
    }

    @Override
    public void run() {
        try {
            switch (tcpPackage.getPackageType()) {
                // 单聊消息
                case Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE:
                    Im.SingleMsg singleMsg = Im.SingleMsg.parseFrom(tcpPackage.getContent());
                    IMsgSendService msgSendService = ((IMsgSendService)SpringBeanUtils.getBean("msgSendService"));
                    if (msgSendService != null) {
                        msgSendService.sendSingleMsg(singleMsg);
                    }
                    break;
                // 群聊消息
                case Im.PkgTypeEnum.MSG_SEND_GROUP_VALUE:
                    Im.GroupMsg groupMsg = Im.GroupMsg.parseFrom(tcpPackage.getContent());
                    ((IMsgSendService)SpringBeanUtils.getBean("msgSendService")).sendGroupMsg(groupMsg);
                    break;
                default:
                    logger.error("没有找到对应的包");
                    break;
            }
        } catch (Throwable e) {
            logger.error("解析消息发送包出错", e);
        }
    }

}
