package com.tksflysun.hi.tcp.encoder;


import com.tksflysun.hi.tcp.protobuf.Im;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * ProtoBuf解码器，约定前四个字节用于指定需要读取的长度
 *
 * @author
 */
public class ProtoBufEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Im.TcpPackage) {
            Im.TcpPackage tcpPackage = (Im.TcpPackage) msg;
            ByteBuf byteBuf = Unpooled.buffer();
            byte[] b = tcpPackage.toByteArray();
            byteBuf.writeInt(b.length);
            byteBuf.writeBytes(b);
            ctx.writeAndFlush(byteBuf);
        }
    }
}
