package com.tksflysun.im.api.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tksflysun.im.common.bean.UserAttribute;

@Mapper
public interface UserAttributeDao {
    void addUserAttribute(UserAttribute attr);

    void updateUserAttr(UserAttribute attr);

    UserAttribute getUserAttribute(@Param("userId") Long userId);

}
