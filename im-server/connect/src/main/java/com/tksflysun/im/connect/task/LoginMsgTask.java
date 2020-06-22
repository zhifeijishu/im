package com.tksflysun.im.connect.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.LoginReq;
import com.tksflysun.im.connect.common.UserChannelCache;

import io.netty.channel.Channel;

/**
 * 登录消息处理任务 *
 * 
 * @author lpf
 *
 */
public class LoginMsgTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LoginMsgTask.class);
    private Im.TcpPackage tcpPackage;
    private Channel channel;

    public LoginMsgTask(Im.TcpPackage tcpPackage, Channel channel) {
        this.tcpPackage = tcpPackage;
        this.channel = channel;
    }

    @Override
    public void run() {
        logger.warn("接收到客户端登录消息", tcpPackage.getPackageType());
        LoginReq loginReq = null;
        try {
            loginReq = Im.LoginReq.parseFrom(tcpPackage.getContent().toByteArray());
            // TODO:改为获取本机ip
            // UserIpRedisCache.getInstance().setT(loginReq.getUserId() + "", "127.0.0.1");
            logger.warn("用户登录成功：" + loginReq.getUserId());
            UserChannelCache.set(loginReq.getUserId() + "", channel);
        } catch (Throwable e) {
            logger.error("解析登录包失败", e);
            channel.close();
        }
    }

}
