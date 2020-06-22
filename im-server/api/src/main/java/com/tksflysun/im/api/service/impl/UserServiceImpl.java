package com.tksflysun.im.api.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tksflysun.im.api.dao.UserAttributeDao;
import com.tksflysun.im.api.dao.UserDao;
import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.api.service.FriendService;
import com.tksflysun.im.api.service.LoginLogService;
import com.tksflysun.im.api.service.UserService;
import com.tksflysun.im.api.util.UserLocalCache;
import com.tksflysun.im.common.bean.FriendShip;
import com.tksflysun.im.common.bean.LoginLog;
import com.tksflysun.im.common.bean.User;
import com.tksflysun.im.common.bean.UserAttribute;
import com.tksflysun.im.common.constants.Constants;
import com.tksflysun.im.common.util.DateUtil;
import com.tksflysun.im.common.util.Md5Util;
import com.tksflysun.im.common.util.ShareCodeUtil;
import com.tksflysun.im.common.util.StringUtil;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAttributeDao userAttributeDao;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private FriendService friendService;

    @Override
    public User registByMobile(User userReq) {
        long inviteUserId = ShareCodeUtil.codeToId(userReq.getInviteCode());
        UserAttribute friendUserAttribute = userAttributeDao.getUserAttribute(inviteUserId);
        if (friendUserAttribute == null) {
            return null;
        }
        User user = addUser(userReq, friendUserAttribute);
        return user;
    }

    @Transactional
    public User addUser(User user, UserAttribute friendAttribute) {
        String timeStamp = DateUtil.getTimeStampStr();
        user.setAddTimeStamp(timeStamp);
        user.setUpdateTimeStamp(timeStamp);
        user.setSalt(StringUtil.getRandom(16));
        user.setPassword(Md5Util.getMd5Str(user.getPassword() + user.getSalt()));
        user.setSrlNo(0l);
        user.setReadSrlNo(0l);
        userDao.addUser(user);
        Long userId = user.getUserId();
        user.setInviteCode(ShareCodeUtil.toSerialCode(userId));
        userDao.updateInviteCode(user);

        UserAttribute userAttribute = user.getAttr();
        if (userAttribute == null) {
            userAttribute = new UserAttribute();
        }
        String originName = StringUtils.isEmpty(userAttribute.getNickName())
                ? StringUtil.getSuffix(user.getMobilePhone(), 4) : userAttribute.getNickName();
        userAttribute.setUserId(userId);
        userAttribute.setNickName(originName);
        userAttributeDao.addUserAttribute(userAttribute);

        // 和邀请人
        becomeFriend(friendAttribute, originName, userId);

        // 一键注册登录
        String token = UUID.randomUUID().toString().replace("-", "");
        autoLogin(user, token);

        user.setPassword(null);
        user.setSalt(null);
        user.setToken(token);
        user.setAttr(userAttribute);
        return user;
    }

    private void autoLogin(User user, String token) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setLoginTimeStamp(DateUtil.getTimeStampStr());
        loginLog.setStatus(Constants.LOGIN_STATUS.ACTIVE);
        loginLog.setToken(token);
        loginLogService.login(loginLog);
    }

    private void becomeFriend(UserAttribute friendAttribute, String originName, Long userId) {

        FriendShip invitorFriend = new FriendShip();
        invitorFriend.setUserId(friendAttribute.getUserId());
        invitorFriend.setFriendId(userId);
        invitorFriend.setOriginName(originName);
        invitorFriend.setRemarkName(originName);
        invitorFriend.setAddTimeStamp(DateUtil.getTimeStampStr());
        invitorFriend.setStatus(Constants.FRIEND_SHIP_STATUS.ACTIVE);
        friendService.addFriendShip(invitorFriend);

        FriendShip registerFriend = new FriendShip();
        registerFriend.setUserId(userId);
        registerFriend.setFriendId(friendAttribute.getUserId());
        registerFriend.setOriginName(friendAttribute.getNickName());
        registerFriend.setRemarkName(friendAttribute.getNickName());
        registerFriend.setAddTimeStamp(DateUtil.getTimeStampStr());
        registerFriend.setStatus(Constants.FRIEND_SHIP_STATUS.ACTIVE);
        friendService.addFriendShip(registerFriend);
    }

    @Override
    public User getUser(UserQo userQo) {
        User user = userDao.getUser(userQo);
        if (user != null) {
            UserAttribute userAttribute = userAttributeDao.getUserAttribute(user.getUserId());
            user.setAttr(userAttribute);
        }
        return user;
    }

    @Override
    public User getUserWithPassword(UserQo userQo) {
        return userDao.getUserWithPassword(userQo);
    }

    @Override
    public void updateUserAttribute(UserAttribute userAttribute) {
        userAttribute.setUserId(UserLocalCache.getUser().getUserId());
        userAttributeDao.updateUserAttr(userAttribute);
    }

    @Override
    public void updateInviteCode() {
        long userId = UserLocalCache.getUser().getUserId();
        // 暂时仅支持生成userId在1亿内的邀请码，通过扩展序列最小长度来升级
        String inviteCode = ShareCodeUtil.toSerialCode(userId);
        User user = new User();
        user.setUserId(userId);
        user.setInviteCode(inviteCode);
        userDao.updateInviteCode(user);
    }

}
