package com.tksflysun.im.process.service;

import com.tksflysun.im.common.protobuf.Im;

public interface IMsgSendService {
    void sendSingleMsg(Im.SingleMsg singleMsg);

    void sendGroupMsg(Im.GroupMsg groupMsg);

}
