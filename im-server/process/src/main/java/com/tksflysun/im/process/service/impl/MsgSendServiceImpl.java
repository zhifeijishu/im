package com.tksflysun.im.process.service.impl;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.GroupMsg;
import com.tksflysun.im.common.protobuf.Im.SingleMsg;
import com.tksflysun.im.common.util.DateUtil;
import com.tksflysun.im.process.bean.Msg;
import com.tksflysun.im.process.dao.MsgDao;
import com.tksflysun.im.process.dao.UserDao;
import com.tksflysun.im.process.server.UdpServer;
import com.tksflysun.im.process.service.IMsgSendService;

@Service("msgSendService")
public class MsgSendServiceImpl implements IMsgSendService {

    @Autowired
    private MsgDao msgDao;
    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(MsgSendServiceImpl.class);

    @Override
    // @Transactional
    public void sendSingleMsg(SingleMsg singleMsg) {
        try {
            logger.warn("单聊消息类型============》" + singleMsg.getMsgType());
            if (singleMsg.getMsgType() == Im.MsgTypeEnum.PUSHCMD_VALUE) {
                logger.warn("=====================>进入PUSHCMD消息处理流程中");
                // 不用增加消息序号，也不用落库，不用发送消息接收响应，不用发送消息未读通知，直接发送给用户
                Im.MsgReqRes.Builder builder =
                        Im.MsgReqRes.newBuilder()
                                .setSrlNo(singleMsg.getSrlNo())
                                .addSingleMessageList(singleMsg)
                                .setTimeStamp(System.currentTimeMillis());
                Im.TcpPackage tcpPackage = Im.TcpPackage.newBuilder().setContent(builder.build().toByteString())
                        .setPackageType(Im.PkgTypeEnum.MSG_REQ_RES_VALUE).build();
                Im.UdpPackage udpPackage = Im.UdpPackage.newBuilder().setToUserId(singleMsg.getToUserId())
                        .setContent(tcpPackage.toByteString()).build();
                logger.warn("给用户" +singleMsg.getToUserId()+"发送PUSHCMD消息");
                UdpServer.sendMsg(udpPackage);
            } else {
                long toUserId = singleMsg.getToUserId();
                User user = userDao.getUserByUserId(toUserId);
                user.setSrlNo(user.getSrlNo() + 1);
                userDao.updateSrlNo(user);
                addMsg(singleMsg, user);
                msgSendRes(singleMsg);
                msgInform(user);
            }
        } catch (Exception e) {
            logger.error("发送消息失败===============》" + e.getMessage());
        }
    }

    /**
     * 消息通知
     * 
     * @param user
     */
    private void msgInform(User user) {
        logger.warn("给用户" + user.getUserId() + "发送消息通知包");
        Im.MsgInform msgInform =
            Im.MsgInform.newBuilder().setTimeStamp(System.currentTimeMillis()).setSrlNo(user.getSrlNo()).build();
        Im.TcpPackage tcpPackage = Im.TcpPackage.newBuilder().setContent(msgInform.toByteString())
            .setPackageType(Im.PkgTypeEnum.MSG_INFORM_VALUE).build();
        Im.UdpPackage udpPackage =
            Im.UdpPackage.newBuilder().setToUserId(user.getUserId()).setContent(tcpPackage.toByteString()).build();
        UdpServer.sendMsg(udpPackage);
    }

    /**
     * 发送消息发送成功返回包
     * 
     * @param singleMsg
     */
    private void msgSendRes(SingleMsg singleMsg) {
        Im.MsgSendRes msgSendRes = Im.MsgSendRes.newBuilder().setMsgId(singleMsg.getMsgId()).build();
        Im.TcpPackage tcpPackage = Im.TcpPackage.newBuilder().setContent(msgSendRes.toByteString())
            .setPackageType(Im.PkgTypeEnum.MSG_SEND_RES_VALUE).build();
        Im.UdpPackage udpPackage = Im.UdpPackage.newBuilder().setToUserId(singleMsg.getFromUserId())
            .setContent(tcpPackage.toByteString()).build();
        logger.warn("给用户" + singleMsg.getFromUserId() + "发送消息发送成功响应");
        UdpServer.sendMsg(udpPackage);
    }

    /**
     * 消息入库
     * 
     * @param singleMsg
     * @param user
     */
    private void addMsg(SingleMsg singleMsg, User user) {
        Msg msg = new Msg();
        msg.setAddTime(DateUtil.getTimeStampStr());
        msg.setUserId(user.getUserId());
        msg.setContent(Base64.getEncoder().encodeToString(singleMsg.toByteArray()));
        msg.setType(1);

        msg.setSrlNo(user.getSrlNo());
        msg.setMsgId(singleMsg.getMsgId());
        msgDao.addMsg(msg);
    }

    @Override
    @Transactional
    public void sendGroupMsg(GroupMsg groupMsg) {

    }

}
