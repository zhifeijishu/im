package com.tksflysun.im.process.service.impl;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.protobuf.Im;
import com.tksflysun.im.common.protobuf.Im.MsgReq;
import com.tksflysun.im.process.bean.Msg;
import com.tksflysun.im.process.dao.MsgDao;
import com.tksflysun.im.process.dao.UserDao;
import com.tksflysun.im.process.server.UdpServer;
import com.tksflysun.im.process.service.IMsgReqService;

@Service("msgReqService")
public class MsgReqServiceImpl implements IMsgReqService {
    private static final Logger logger = LoggerFactory.getLogger(MsgReqServiceImpl.class);

    @Autowired
    MsgDao msgDao;
    @Autowired
    UserDao userDao;

    @Override
    public void getMsgList(MsgReq msgReq) {
        try {
            logger.warn("接收到用户" + msgReq.getUserId() + "的消息拉取请求");
            User user = userDao.getUserByUserId(msgReq.getUserId());
            List<Msg> msgs = msgDao.getMsgs(msgReq.getUserId(), user.getReadSrlNo(), 10);
            logger.warn("接收到用户" + msgReq.getUserId() + "的消息拉取请求,未读消息长度" + msgs.size());
            Im.MsgReqRes.Builder builder =
                Im.MsgReqRes.newBuilder().setSrlNo(0).setTimeStamp(System.currentTimeMillis());
            long srlNo = 0;
            for (Msg m : msgs) {
                if (srlNo < m.getSrlNo()) {
                    srlNo = m.getSrlNo();
                }
                Im.SingleMsg singleMsg = Im.SingleMsg.parseFrom(Base64.getDecoder().decode(m.getContent()));
                builder.addSingleMessageList(singleMsg);
            }
            builder.setSrlNo(srlNo);
            builder.setTimeStamp(System.currentTimeMillis());
            Im.TcpPackage tcpPackage = Im.TcpPackage.newBuilder().setContent(builder.build().toByteString())
                .setPackageType(Im.PkgTypeEnum.MSG_REQ_RES_VALUE).build();
            Im.UdpPackage udpPackage = Im.UdpPackage.newBuilder().setToUserId(msgReq.getUserId())
                .setContent(tcpPackage.toByteString()).build();
            UdpServer.sendMsg(udpPackage);
        } catch (Exception e) {
            logger.error("用户获取消息列表失败===========> " + msgReq.getUserId(), e);
        }

    }

}
