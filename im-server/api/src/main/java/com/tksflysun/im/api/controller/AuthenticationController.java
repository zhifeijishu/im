package com.tksflysun.im.api.controller;

import com.tksflysun.im.common.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tksflysun.im.api.bean.LoginReq;
import com.tksflysun.im.api.redis.LoginLimitRedisCache;
import com.tksflysun.im.api.redis.VerifyCodeRedisCache;
import com.tksflysun.im.api.service.LoginService;
import com.tksflysun.im.common.constants.Constants;
import com.tksflysun.im.common.result.ControllerResult;
import com.tksflysun.im.common.util.MobileUtil;
import com.tksflysun.im.common.util.StringUtil;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(path = "/verifycode", method = RequestMethod.POST)
    public ControllerResult getVerifyCode(@RequestBody LoginReq loginReq) {
        ControllerResult result = ControllerResult.getSuccessResult();
        String verifyCode = StringUtil.getRandom(6);
        result.setData(verifyCode);
        VerifyCodeRedisCache.getInstance().setexT(loginReq.getMobilePhone(), verifyCode.toLowerCase());
        return result;
    }

    @RequestMapping(path = "/loginByMobile", method = RequestMethod.POST)
    public ControllerResult login(@RequestBody LoginReq loginReq) {
        ControllerResult result = ControllerResult.getSuccessResult();
        if (StringUtils.isEmpty(loginReq.getMobilePhone())) {
            result = ControllerResult.getResult(ControllerResult.ACCOUNT_NULL_CODE, ControllerResult.ACCOUNT_NULL_MSG);
            return result;
        }
        if (StringUtils.isEmpty(loginReq.getPassword())) {
            result = ControllerResult.getResult(ControllerResult.PWD_NULL_CODE, ControllerResult.PWD_NULL_MSG);
            return result;
        }
        if (!MobileUtil.isMobile(loginReq.getMobilePhone())) {
            result =
                ControllerResult.getResult(ControllerResult.INVALID_PHONE_CODE, ControllerResult.INVALID_PHONE_MSG);
            return result;
        }
        String loginCountStr = LoginLimitRedisCache.getInstance()
                .getJedisCluster().get(Constants.LIMIT_PREX.MOBILE + loginReq.getMobilePhone());
        int loginCount = loginCountStr == null ? 0 : Integer.parseInt(loginCountStr);
        if (loginCount >= LoginLimitRedisCache.ACCOUNT_LIMIT_COUNT) {
            result = ControllerResult.getResult(ControllerResult.CHECK_REJECT_CODE, ControllerResult.CHECK_REJECT_MSG);
            return result;
        }
        User user = loginService.loginByMobile(loginReq, loginCount);
        if (user == null) {
            result.setCode(ControllerResult.LOGIN_FAIL);
        }
        result.setData(user);
        return result;
    }
}
