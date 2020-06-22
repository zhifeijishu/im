package com.tksflysun.im.process.dao;

import org.apache.ibatis.annotations.Mapper;

import com.tksflysun.im.common.bean.User;

@Mapper
public interface UserDao {
    User getUserByUserId(Long userId);

    void updateSrlNo(User user);

    void updateReadSrlNo(User user);

}
