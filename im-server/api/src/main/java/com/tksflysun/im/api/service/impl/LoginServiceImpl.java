package com.tksflysun.im.api.service.impl;

import java.util.UUID;

import com.tksflysun.im.api.dao.UserAttributeDao;
import com.tksflysun.im.common.bean.UserAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tksflysun.im.api.bean.LoginReq;
import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.api.redis.LoginLimitRedisCache;
import com.tksflysun.im.api.service.LoginLogService;
import com.tksflysun.im.api.service.LoginService;
import com.tksflysun.im.api.service.UserService;
import com.tksflysun.im.common.bean.LoginLog;
import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.constants.Constants;
import com.tksflysun.im.common.util.DateUtil;
import com.tksflysun.im.common.util.Md5Util;

import static java.lang.Character.getType;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserAttributeDao userAttributeDao;
    @Autowired
    private LoginLogService loginLogService;

    private static String getType(Object o) {
        return o.getClass().toString();
    }

    @Override
    public User loginByMobile(LoginReq loginReq, Integer loginCount) {
        UserQo userQo = new UserQo();
        userQo.setPhone(loginReq.getMobilePhone());
        User user = userService.getUserWithPassword(userQo);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(Md5Util.getMd5Str(loginReq.getPassword() + user.getSalt()))) {
            LoginLimitRedisCache.getInstance().getJedisCluster()
                    .set(Constants.LIMIT_PREX.MOBILE + loginReq.getMobilePhone(), String.valueOf(loginCount + 1));
            LoginLimitRedisCache.getInstance().getJedisCluster().expire(Constants.LIMIT_PREX.MOBILE + loginReq.getMobilePhone(), LoginLimitRedisCache.TTL);
            return null;
        }
        return loginSuccess(user);

    }

    private User loginSuccess(User user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        user.setToken(token);
        user.setPassword(null);
        user.setSalt(null);

        UserAttribute userAttribute = userAttributeDao.getUserAttribute(user.getUserId());
        user.setAttr(userAttribute);

        loginUp(user, token);

        return user;
    }

    private void loginUp(User user, String token) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setLoginTimeStamp(DateUtil.getTimeStampStr());
        loginLog.setStatus(Constants.LOGIN_STATUS.ACTIVE);
        loginLog.setToken(token);
        loginLogService.login(loginLog);
    }
}
