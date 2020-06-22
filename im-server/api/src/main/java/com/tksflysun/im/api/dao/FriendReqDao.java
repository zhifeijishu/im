package com.tksflysun.im.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tksflysun.im.common.bean.FriendReq;

@Mapper
public interface FriendReqDao {
    void addFriendReq(FriendReq req);

    void updateFriendReqStatus(FriendReq req);

    List<FriendReq> getFriendReqByFriendId(@Param("friendId") Long friendId);

    FriendReq getFriendReqById(@Param("reqId") Long reqId);

}
