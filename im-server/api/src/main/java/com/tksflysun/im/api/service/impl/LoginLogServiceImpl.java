package com.tksflysun.im.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tksflysun.im.api.dao.LoginLogDao;
import com.tksflysun.im.api.service.LoginLogService;
import com.tksflysun.im.common.bean.LoginLog;

@Service("loginLogService")
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    private LoginLogDao loginLogDao;

    @Override
    @Transactional
    public void login(LoginLog loginLog) {
        loginLogDao.invalidPreLogin(loginLog.getUserId());
        loginLogDao.addLoginLog(loginLog);
    }

    @Override
    public LoginLog getLatestedActiveLogin(Long userId) {
        return loginLogDao.getLatestedActiveLogin(userId);
    }

    @Override
    public LoginLog getLoginLogByToken(String token) {
        return loginLogDao.getLoginLogByToken(token);
    }

}
