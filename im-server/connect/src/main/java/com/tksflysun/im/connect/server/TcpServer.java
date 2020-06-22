package com.tksflysun.im.connect.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tksflysun.im.connect.decoder.FixLengthDecoder;
import com.tksflysun.im.connect.decoder.ProtoBufDecoder;
import com.tksflysun.im.connect.encoder.ProtoBufEncoder;
import com.tksflysun.im.connect.handle.TcpMsgHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class TcpServer {
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
    @Value("${tcp.port}")
    private int port;
    @Autowired
    private TcpMsgHandler msgHandler;
    @Autowired
    private ProtoBufDecoder protoBufDecoder;
    @Autowired
    private ProtoBufEncoder protoBufEncoder;

    public void start() throws Exception {

        /**
         * 配置服务端的NIO线程组 NioEventLoopGroup 是用来处理I/O操作的Reactor线程组
         * bossGroup：用来接收进来的连接，workerGroup：用来处理已经被接收的连接,进行socketChannel的网络读写， bossGroup接收到连接后就会把连接信息注册到workerGroup
         * workerGroup的EventLoopGroup默认的线程数是CPU核数的二倍
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * 设置group，将bossGroup， workerGroup线程组传递到ServerBootstrap
             */
            serverBootstrap = serverBootstrap.group(bossGroup, workerGroup);
            /**
             * ServerSocketChannel是以NIO的selector为基础进行实现的，用来接收新的连接，这里告诉Channel通过NioServerSocketChannel获取新的连接
             */
            serverBootstrap = serverBootstrap.channel(NioServerSocketChannel.class);
            /**
             * option是设置 bossGroup，childOption是设置workerGroup netty 默认数据包传输大小为1024字节, 设置它可以自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费
             * 最小 初始化 最大 (根据生产环境实际情况来定) 使用对象池，重用缓冲区
             */
            // 添加handler，管道中的处理器，通过ChannelInitializer来构造
            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    // 客户端分钟没有消息过来就认为挂了
                    channel.pipeline().addLast(new IdleStateHandler(600, 0, 0));
                    channel.pipeline().addLast(new FixLengthDecoder());
                    channel.pipeline().addLast(protoBufDecoder);
                    channel.pipeline().addLast(msgHandler);
                    channel.pipeline().addLast(protoBufEncoder);
                }
            });

            // 6.设置参数
            // 设置参数，TCP参数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 4096); // 连接缓冲池的大小
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);// 维持链接的活跃，清除死链接
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);// 关闭延迟发送
            logger.warn("tcp服务启动");
            /**
             * 绑定端口，同步等待成功
             */
            ChannelFuture f = serverBootstrap.bind(port).sync();
            /**
             * 等待服务器监听端口关闭
             */
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {

        } finally {
            logger.warn("tcp服务关闭");

            /**
             * 退出，释放线程池资源
             */
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
