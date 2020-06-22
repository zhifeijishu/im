package com.tksflysun.hi.tcp.decoder;

import android.util.Log;

import com.tksflysun.hi.tcp.protobuf.Im;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ProtoBuf解码器，约定前四个字节用于指定需要读取的长度
 *
 * @author liupf
 */
public class ProtoBufDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof byte[]) {
            Im.TcpPackage tcpPackage = Im.TcpPackage.parseFrom((byte[]) msg);
            Log.i("解码服务器的消息","解码服务器的消息"+tcpPackage.getPackageType());

            ctx.fireChannelRead(tcpPackage);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
