package com.tksflysun.im.connect.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.TcpPackage;
import com.tksflysun.im.connect.common.UserChannelCache;
import com.tksflysun.im.connect.task.LoginMsgTask;
import com.tksflysun.im.connect.task.TransferMsgTask;
import com.tksflysun.im.connect.task.execute.TaskExecute;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Tcp 消息handle
 * 
 * @author lpf
 *
 */
@Service
@Sharable
public class TcpMsgHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TcpMsgHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("接收到用户的消息");
        }
        if (msg instanceof Im.TcpPackage) {
            Im.TcpPackage tcpPackage = (TcpPackage)msg;
            // 登录消息
            if (tcpPackage.getPackageType() == Im.PkgTypeEnum.LOGIN_REQ_VALUE) {
                TaskExecute.execute(new LoginMsgTask(tcpPackage, ctx.channel()));
                // 心跳消息，直接发送响应
            } else if (tcpPackage.getPackageType() == Im.PkgTypeEnum.PING_VALUE) {
                if (UserChannelCache.getUserId(ctx.channel()) == null) {
                    // 发送心跳消息的时候如果通道缓存不存在那么关闭此通道，必须登录过的通道才是可用的
                    UserChannelCache.removeChannel(ctx.channel());
                    ctx.channel().close();
                }
                Im.TcpPackage pong = Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.PONG_VALUE).build();
                ctx.channel().writeAndFlush(pong);
            } else if (tcpPackage.getPackageType() == Im.PkgTypeEnum.PONG_VALUE) {
            } else if (tcpPackage.getPackageType() == Im.PkgTypeEnum.MSG_SEND_RES_VALUE) {
                logger.warn("接收到某人发过来消息");
            } else {
                TaskExecute.execute(new TransferMsgTask(tcpPackage));
            }
            ctx.fireChannelRead(msg);
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

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        UserChannelCache.removeChannel(ctx.channel());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        UserChannelCache.removeChannel(ctx.channel());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.warn("已经十分钟没有收到客户端的消息了，关闭通道");
                UserChannelCache.removeChannel(ctx.channel());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
