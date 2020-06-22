package com.tksflysun.hi.tcp;

import com.tksflysun.hi.tcp.protobuf.Im;

import java.util.List;

public interface IMsgListener {
  void  doReceive(Object msg);
  void  doReceiveList(List<Object> msg);

}
