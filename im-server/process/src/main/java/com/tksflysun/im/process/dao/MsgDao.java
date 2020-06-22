package com.tksflysun.im.process.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tksflysun.im.process.bean.Msg;

@Mapper
public interface MsgDao {
    void addMsg(Msg msg);

    List<Msg> getMsgs(@Param("userId") Long userId, @Param("srlNo") Long srlNo, @Param("pageSize") int pageSize);

}
