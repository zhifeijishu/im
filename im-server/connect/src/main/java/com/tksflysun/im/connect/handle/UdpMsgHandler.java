package com.tksflysun.im.connect.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.connect.task.UdpReceiveMsgTask;
import com.tksflysun.im.connect.task.execute.TaskExecute;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * udp消息handle
 * 
 * @author lpf
 *
 */
@Component
@Sharable
public class UdpMsgHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger logger = LoggerFactory.getLogger(UdpMsgHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("接收到来自process的消息");
        }
        ByteBuf buf = msg.copy().content();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        Im.UdpPackage udpPackage = Im.UdpPackage.parseFrom(data);
        TaskExecute.execute(new UdpReceiveMsgTask(udpPackage));
    }

}
