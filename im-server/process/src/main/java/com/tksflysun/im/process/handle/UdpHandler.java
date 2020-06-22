package com.tksflysun.im.process.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.process.execute.MsgDispatcher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

@Component
@Sharable
public class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger logger = LoggerFactory.getLogger(UdpHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg.copy().content();
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        Im.UdpPackage udpPackage = Im.UdpPackage.parseFrom(data);
        MsgDispatcher.dispatcher(udpPackage);

    }

}
