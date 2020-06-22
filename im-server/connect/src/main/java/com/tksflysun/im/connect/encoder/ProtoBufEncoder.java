package com.tksflysun.im.connect.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.TcpPackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * ProtoBuf解码器，约定前四个字节用于指定需要读取的长度
 * 
 * @author
 *
 */
@Service
@Sharable
public class ProtoBufEncoder extends ChannelOutboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProtoBufEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Im.TcpPackage) {
            Im.TcpPackage tcpPackage = (TcpPackage)msg;
            ByteBuf byteBuf = Unpooled.buffer();
            byte[] b = tcpPackage.toByteArray();
            byteBuf.writeInt(b.length);
            byteBuf.writeBytes(b);
            ctx.writeAndFlush(byteBuf);
        }
    }
}
