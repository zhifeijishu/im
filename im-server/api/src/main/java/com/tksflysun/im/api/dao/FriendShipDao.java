package com.tksflysun.im.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tksflysun.im.common.bean.FriendShip;

@Mapper
public interface FriendShipDao {
    void addFriendShip(FriendShip friendShip);

    void updateFriendShip(FriendShip friendShip);

    FriendShip getFriendShipById(@Param("friendShipId") Long friendShipId);

    FriendShip getFriendShipByUserAndFriendId(@Param("friendShipId") Long friendShipId, @Param("userId") Long userId);

    List<FriendShip> getFriendShipByUserId(@Param("userId") Long userId);
}
