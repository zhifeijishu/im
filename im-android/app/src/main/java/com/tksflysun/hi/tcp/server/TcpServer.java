package com.tksflysun.hi.tcp.server;

import android.util.Log;

import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.tcp.decoder.FixLengthDecoder;
import com.tksflysun.hi.tcp.decoder.ProtoBufDecoder;
import com.tksflysun.hi.tcp.encoder.ProtoBufEncoder;
import com.tksflysun.hi.tcp.handle.TcpMsgHandler;
import com.tksflysun.hi.tcp.protobuf.Im;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class TcpServer {
    private static ChannelFuture channelFuture;
    private static final ExecutorService threadPool =
            new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(10000),
                    new ThreadPoolExecutor.DiscardPolicy());
    private static boolean starting = false;
    private static long lastAct=0;
    private static int interval = 0;//启动间隔

    /**
     * 启动，启动之前如果之前已经有，先执行关闭，启动，重连都可以调用这个方法
     *
     * @throws Exception
     */
    public static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TcpServer.class) {
                    if (starting) {
                        return;
                    }
                    Log.i("长连接启动", "长连接启动");
                    close();
                    starting = true;
                }
                EventLoopGroup group = null;
                try {
                    Thread.sleep(interval);
                    interval = interval + 10000;
                    if (interval > 120000) {
                        interval = 120000;
                    }
                    Bootstrap client = new Bootstrap();
                    group = new NioEventLoopGroup();
                    client.group(group);
                    client.channel(NioSocketChannel.class);
                    client.handler(new ChannelInitializer<NioSocketChannel>() { // 通道是NioSocketChannel
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //3分钟如果没有发送任何消息发送一个心跳，如果2个心跳还是没有消息返回，那么认为已经关闭了
                            ch.pipeline().addLast(new IdleStateHandler(365, 180, 0));
                            ch.pipeline().addLast(new FixLengthDecoder());
                            ch.pipeline().addLast(new ProtoBufDecoder());
                            ch.pipeline().addLast(new TcpMsgHandler());
                            ch.pipeline().addLast(new ProtoBufEncoder());
                        }
                    });
                    channelFuture =
                            client.connect("*.*.136.6", 8888).sync();
                    //连接成功之后马上发送登录请求
                    Im.LoginReq loginReq =
                            Im.LoginReq.newBuilder().setDeviceType(Im.DeviceTypeEnum.ANDROID_VALUE).setToken(HiApplication.getInstance().getUser().getToken()).setUserId(HiApplication.getInstance().getUser().getUserId()).build();
                    Im.TcpPackage loginmsg =
                            Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.LOGIN_REQ_VALUE).setContent(loginReq.toByteString()).build();
                    channelFuture.channel().writeAndFlush(loginmsg);
                    channelFuture.channel().closeFuture().sync();
                } catch (Throwable e) {
                    Log.i("tcp长连接建立失败", e.getMessage());
                } finally {
                    Log.i("tcp长连接关闭", "tcp长连接关闭");
                    if (group != null) {
                        group.shutdownGracefully();
                    }
                    starting = false;
                    //关闭之后进行重连
                    start();
                }
            }
        }).start();
    }

    public static void close() {
        if (channelFuture != null && channelFuture.channel() != null) {
            channelFuture.channel().close();
        }
        starting = false;
    }

    public static void sendMsg(final Im.TcpPackage msg) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (channelFuture == null || !channelFuture.channel().isActive()) {
                        start();
                    }
                    channelFuture.channel().writeAndFlush(msg);

                } catch (Throwable e) {
                    Log.e("发送消息失败", e.getMessage());
                }
            }
        });

    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
