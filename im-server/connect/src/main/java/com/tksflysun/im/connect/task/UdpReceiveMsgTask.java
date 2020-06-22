package com.tksflysun.im.connect.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.connect.common.UserChannelCache;

import io.netty.channel.Channel;

/**
 * process消息转发给tcp
 * 
 * @author lpf
 *
 */
public class UdpReceiveMsgTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(UdpReceiveMsgTask.class);
    private Im.UdpPackage udpPackage;

    public UdpReceiveMsgTask(Im.UdpPackage udpPackage) {
        this.udpPackage = udpPackage;
    }

    @Override
    public void run() {
        try {
            logger.warn("接收到process发给用户：" + udpPackage.getToUserId() + "的消息");
            String userId = udpPackage.getToUserId() + "";
            Channel channel = UserChannelCache.getChannel(userId);
            if (channel == null) {
                logger.warn("用户" + userId + "的channel不存在");
                return;
            }
            channel.writeAndFlush(Im.TcpPackage.parseFrom(udpPackage.getContent().toByteArray()));
        } catch (Throwable e) {
            logger.error("给用户下发消息失败" + udpPackage.getToUserId(), e);
        }
    }

}
