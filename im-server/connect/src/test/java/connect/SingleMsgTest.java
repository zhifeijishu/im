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

public class SingleMsgTest {
    public static void main(String[] args) throws Exception {
        start(1, 2);
        start(2, 1);
        Thread.sleep(10000000);
    }

    private static void start(long toUserId, long fromUserId) {
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

                    ChannelFuture future = client.connect("111.229.136.6", 8888).sync();
                    Im.SingleMsg singleMsg = Im.SingleMsg.newBuilder().setToUserId(toUserId).setFromUserId(fromUserId)
                        .setContent("hello" + toUserId).build();
                    Im.TcpPackage msgTcpPackage = Im.TcpPackage.newBuilder().setContent(singleMsg.toByteString())
                        .setPackageType(Im.PkgTypeEnum.MSG_SEND_SINGLE_VALUE).build();
                    Im.LoginReq loginReq = Im.LoginReq.newBuilder().setUserId(fromUserId).build();
                    Im.TcpPackage loginmsg = Im.TcpPackage.newBuilder().setPackageType(Im.PkgTypeEnum.LOGIN_REQ_VALUE)
                        .setContent(loginReq.toByteString()).build();
                    future.channel().writeAndFlush(loginmsg);
                    for (;;) {
                        Thread.sleep(5000);
                        future.channel().writeAndFlush(msgTcpPackage);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
