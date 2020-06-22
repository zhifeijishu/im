package com.tksflysun.hi.tcp.decoder;

import android.util.Log;

import java.util.List;

import com.tksflysun.hi.tcp.server.TcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ProtoBuf解码器，约定前四个字节用于指定需要读取的长度
 * 
 * @author
 *
 */
public class FixLengthDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Log.i("接收到服务器端的消息","接收到服务器端的消息");
        // 固定编码前四位为消息长度
        if (in.readableBytes() < 4) {
            return;
        }
        // 在读取前标记readerIndex
        in.markReaderIndex();
        // 读取头部
        int length = in.readInt();
        if (length < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
            ctx.close();
        }
        if (in.readableBytes() < length) {
            // 消息不完整，无法处理，将readerIndex复位
            in.resetReaderIndex();
            return;
        }
        byte[] b = new byte[length];
        in.readBytes(b);
        out.add(b);
    }

}
