package com.tksflysun.im.api.service;

import com.tksflysun.im.api.bean.LoginReq;
import com.tksflysun.im.common.bean.User;

public interface LoginService {
    User loginByMobile(LoginReq loginReq, Integer loginCount);
}
