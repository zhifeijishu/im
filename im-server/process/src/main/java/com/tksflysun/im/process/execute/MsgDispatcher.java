package com.tksflysun.im.process.execute;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.process.task.MsgReqAckTask;
import com.tksflysun.im.process.task.MsgReqTask;
import com.tksflysun.im.process.task.MsgSendTask;
import com.tksflysun.im.process.task.execute.TaskExecute;

@Component
public class MsgDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MsgDispatcher.class);
    private static final ExecutorService receiveMsgPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());

    public static void dispatcher(Im.UdpPackage udpPackage) {
        receiveMsgPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Im.TcpPackage tcpPackage = Im.TcpPackage.parseFrom(udpPackage.getContent());
                    switch (tcpPackage.getPackageType()) {
                        case Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE:
                            TaskExecute.executeMsgSend(new MsgSendTask(tcpPackage));
                            break;
                        case Im.PkgTypeEnum.MSG_SEND_GROUP_VALUE:
                            TaskExecute.executeMsgSend(new MsgSendTask(tcpPackage));
                            break;
                        case Im.PkgTypeEnum.MSG_REQ_ACK_VALUE:
                            TaskExecute.executeMsgReqAck(new MsgReqAckTask(tcpPackage));
                            break;
                        case Im.PkgTypeEnum.MSG_REQ_VALUE:
                            TaskExecute.executeMsgReq(new MsgReqTask(tcpPackage));
                            break;
                        default:
                            logger.error("没有找到对应的消息包处理task");
                            break;
                    }
                } catch (Throwable e) {
                    logger.error("解码接入层消息失败");
                }
            }
        });

    }
}
