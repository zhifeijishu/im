package com.tksflysun.im.process.service;

import com.tksflysun.im.common.protobuf.Im;

public interface IMsgReqAckService {
    /**
     * 消息请求ack处理
     * 
     * @param msgReqAck
     */
    void reqAck(Im.MsgReqAck msgReqAck);
}
