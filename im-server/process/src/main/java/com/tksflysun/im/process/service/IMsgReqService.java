package com.tksflysun.im.process.service;

import com.tksflysun.im.common.protobuf.Im;

public interface IMsgReqService {
    /**
     * 获取消息列表
     * 
     * @param msgReq
     */
    void getMsgList(Im.MsgReq msgReq);
}
