package connect;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.connect.decoder.FixLengthDecoder;
import com.tksflysun.im.connect.decoder.ProtoBufDecoder;
import com.tksflysun.im.connect.encoder.ProtoBufEncoder;
import com.tksflysun.im.connect.handle.TcpMsgHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class PingPongTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2; i++) {
            start();

        }

        Thread.sleep(10000000);
    }

    private static void start() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 首先，netty通过ServerBootstrap启动服务端
                    Bootstrap client = new Bootstrap();

                    // 第1步 定义线程组，处理读写和链接事件，没有了accept事件
                    EventLoopGroup group = new NioEventLoopGroup();
                    client.group(group);

                    // 第2步 绑定客户端通道
                    client.channel(NioSocketChannel.class);

                    // 第3步 给NIoSocketChannel初始化handler， 处理读写事件
                    client.handler(new ChannelInitializer<NioSocketChannel>() { // 通道是NioSocketChannel
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new FixLengthDecoder());
                            ch.pipeline().addLast(new ProtoBufDecoder());
                            ch.pipeline().addLast(new TcpMsgHandler());

                            ch.pipeline().addLast(new ProtoBufEncoder());
                        }
                    });

                    ChannelFuture future = client.connect("localhost", 8888).sync();
                    Im.TcpPackage tcpPackage =
                        Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.PING_VALUE).build();
                    Im.TcpPackage msgTcpPackage =
                        Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE).build();
                    Im.TcpPackage loginmsg =
                        Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.LOGIN_REQ_VALUE).build();

                    for (;;) {
                        future.channel().writeAndFlush(tcpPackage);
                        future.channel().writeAndFlush(loginmsg);

                        future.channel().writeAndFlush(msgTcpPackage);
                        Thread.sleep(5000000);

                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
