package com.tksflysun.im.api.service;

import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.bean.UserAttribute;

public interface UserService {

    User registByMobile(User userReq);

    User getUser(UserQo userQo);

    User getUserWithPassword(UserQo userQo);

    void updateUserAttribute(UserAttribute userAttribute);

    void updateInviteCode();

}
