package com.tksflysun.im.process.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.protobuf.Im.MsgReqAck;
import com.tksflysun.im.process.dao.UserDao;
import com.tksflysun.im.process.service.IMsgReqAckService;

@Service("msgReqAckService")
public class MsgReqAckServiceImpl implements IMsgReqAckService {
    @Autowired
    UserDao userDao;

    @Override
    public void reqAck(MsgReqAck msgReqAck) {
        User user = new User();
        user.setReadSrlNo(msgReqAck.getSrlNo());
        user.setUserId(msgReqAck.getUserId());
        userDao.updateReadSrlNo(user);
    }

}
