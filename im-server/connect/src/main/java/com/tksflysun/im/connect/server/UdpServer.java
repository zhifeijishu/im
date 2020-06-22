package com.tksflysun.im.connect.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.connect.handle.UdpMsgHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

@Component
public class UdpServer {
    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
    @Value("${udp.port}")
    private int port;
    private static List<Channel> channels = new ArrayList<Channel>();
    private static AtomicInteger auto = new AtomicInteger(0);
    @Autowired
    UdpMsgHandler udpMsgHandler;

    // public void start() {
    //// EventLoopGroup group = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    //// try {
    //// logger.warn("udp服务启动");
    //// Bootstrap bootstrap = new Bootstrap();
    //// bootstrap.group(group).channel(Epoll.isAvailable() ? EpollDatagramChannel.class : NioDatagramChannel.class)
    //// .option(ChannelOption.SO_BROADCAST, true).option(ChannelOption.SO_RCVBUF, 1024 * 1024)
    //// .handler(udpMsgHandler);
    //// // linux平台下支持SO_REUSEPORT特性以提高性能
    //// if (Epoll.isAvailable()) {
    //// bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
    //// }
    //// if (Epoll.isAvailable()) {
    //// // linux系统下使用SO_REUSEPORT特性，使得多个线程绑定同一个端口
    //// int cpuNum = Runtime.getRuntime().availableProcessors();
    //// logger.info("using epoll reuseport and cpu:" + cpuNum);
    //// for (int i = 0; i < cpuNum; i++) {
    //// try {
    //// ChannelFuture chanlFuture = bootstrap.bind(port).await();
    //// if (!chanlFuture.isSuccess()) {
    //// logger.error("bootstrap bind fail port is " + port);
    //// } else {
    //// channels.add(chanlFuture.channel());
    //// }
    //// } catch (InterruptedException e) {
    //// logger.error("bootstrap bind exception " + port);
    //// }
    //// }
    //// } else {
    //// try {
    //// Channel ch = bootstrap.bind(port).sync().channel();
    //// channels.add(ch);
    //// ch.closeFuture().await();
    ////
    //// } catch (InterruptedException e) {
    //// logger.error("bootstrap bind exception " + port);
    //// }
    //// }
    //// } finally {
    //// logger.error("udp服务启动失败");
    //// group.shutdownGracefully();
    //// }
    //// }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            logger.warn("udp服务启动");
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(20000)).handler(udpMsgHandler);
            try {
                Channel ch = bootstrap.bind(port).sync().channel();
                channels.add(ch);
                ch.closeFuture().await();
            } catch (InterruptedException e) {
                logger.error("bootstrap bind exception " + port);
            }
        } finally {
            logger.error("关闭udp服务");
            group.shutdownGracefully();
        }
    }

    public static void sendMsg(Im.UdpPackage udpPackage) {
        try {
            int i = auto.getAndIncrement();
            if (i > channels.size() - 1) {
                auto.set(0);
                i = 0;
            }
            channels.get(i).writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(udpPackage.toByteArray()),
                new InetSocketAddress("127.0.0.1", 8090))).sync();
        } catch (Throwable e) {
            logger.warn("connect层udp消息发送失败", e);
        }
        logger.warn("connect 层udp消息发送成功");
    }
}
