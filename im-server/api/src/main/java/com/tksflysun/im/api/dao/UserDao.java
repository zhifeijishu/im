package com.tksflysun.im.api.dao;

import org.apache.ibatis.annotations.Mapper;

import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.common.bean.User;

@Mapper
public interface UserDao {
    Long addUser(User user);

    User getUser(UserQo userQo);

    User getUserWithPassword(UserQo userQo);

    void updateSrlNo(User user);

    void updateReadSrlNo(User user);

    void updateInviteCode(User user);

}
