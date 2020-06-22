package com.tksflysun.im.api.service;

import java.util.List;

import com.tksflysun.im.common.bean.FriendReq;
import com.tksflysun.im.common.bean.FriendShip;

public interface FriendService {

    void addFriendShip(FriendShip friendShip);

    void addFriendReq(FriendReq req);

    void delFriendShip(Long friendShipId);

    void blackFriendShip(Long friendShipId);

    void agreeFriendReq(Long reqId);

    void rejectFriendReq(Long reqId);

    List<FriendReq> getFriendReqsByFriendId(Long friendId);

    List<FriendShip> getFriendShipsByUserId(Long userId);

    FriendShip getFriendShipByUserAndFriendId(Long friendShipId, Long userId);

}
