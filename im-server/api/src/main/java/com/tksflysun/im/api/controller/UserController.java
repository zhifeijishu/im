package com.tksflysun.im.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tksflysun.im.api.service.UserService;
import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.bean.UserAttribute;
import com.tksflysun.im.common.result.ControllerResult;
import com.tksflysun.im.common.util.MobileUtil;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = "/registByMobile", method = RequestMethod.POST)
    public ControllerResult register(@RequestBody User userReq) {
        logger.warn("用户注册的信息：" + JSON.toJSONString(userReq));
        ControllerResult result = ControllerResult.getSuccessResult();
        if (StringUtils.isEmpty(userReq.getMobilePhone())) {
            result = ControllerResult.getResult(ControllerResult.ACCOUNT_NULL_CODE, ControllerResult.ACCOUNT_NULL_MSG);
            return result;
        }
        if (!MobileUtil.isMobile(userReq.getMobilePhone())) {
            result =
                ControllerResult.getResult(ControllerResult.INVALID_PHONE_CODE, ControllerResult.INVALID_PHONE_MSG);
            return result;
        }

        if (StringUtils.isEmpty(userReq.getInviteCode())) {
            result = ControllerResult.getResult(ControllerResult.INVITATION_CODE_NULL_CODE,
                ControllerResult.INVITATION_CODE_NULL_MSG);
            return result;
        }

        User user = userService.registByMobile(userReq);
        if (user == null) {
            result = ControllerResult.getResult(ControllerResult.REGIST_FAIL_CODE, ControllerResult.REGIST_FAIL_MSG);
        }
        result.setData(user);
        return result;
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ControllerResult update(@RequestBody UserAttribute userAttribute) {
        ControllerResult result = ControllerResult.getSuccessResult();
        userService.updateUserAttribute(userAttribute);
        return result;
    }

    @RequestMapping(path = "/updateInviteCode", method = RequestMethod.POST)
    public ControllerResult updateInviteCode() {
        ControllerResult result = ControllerResult.getSuccessResult();
        userService.updateInviteCode();
        return result;
    }
}
