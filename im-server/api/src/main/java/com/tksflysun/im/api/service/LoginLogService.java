package com.tksflysun.im.api.service;

import com.tksflysun.im.common.bean.LoginLog;

public interface LoginLogService {
    void login(LoginLog loginLog);

    LoginLog getLatestedActiveLogin(Long userId);

    LoginLog getLoginLogByToken(String token);
}
