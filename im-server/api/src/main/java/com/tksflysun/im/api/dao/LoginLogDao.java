package com.tksflysun.im.api.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tksflysun.im.common.bean.LoginLog;

@Mapper
public interface LoginLogDao {
    void addLoginLog(LoginLog loginLog);

    void invalidPreLogin(@Param("userId") Long userId);

    LoginLog getLatestedActiveLogin(@Param("userId") Long userId);

    LoginLog getLoginLogByToken(@Param("token") String token);

}
