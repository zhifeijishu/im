package com.tksflysun.im.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tksflysun.im.api.dao.FriendReqDao;
import com.tksflysun.im.api.dao.FriendShipDao;
import com.tksflysun.im.api.dao.UserAttributeDao;
import com.tksflysun.im.api.qo.UserQo;
import com.tksflysun.im.api.service.FriendService;
import com.tksflysun.im.api.util.UserLocalCache;
import com.tksflysun.im.common.bean.AuthPrivicyId;
import com.tksflysun.im.common.bean.FriendReq;
import com.tksflysun.im.common.bean.FriendShip;
import com.tksflysun.im.common.bean.UserAttribute;
import com.tksflysun.im.common.constants.Constants;
import com.tksflysun.im.common.exception.BusinessException;
import com.tksflysun.im.common.result.ControllerResult;
import com.tksflysun.im.common.util.DateUtil;

@Service("friendService")
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendShipDao friendShipDao;
    @Autowired
    private FriendReqDao friendReqDao;
    @Autowired
    private UserAttributeDao userAttributeDao;

    @Override
    public void addFriendShip(FriendShip friendShip) {
        friendShipDao.addFriendShip(friendShip);
    }

    @Override
    public void addFriendReq(FriendReq req) {
        req.setUserId(UserLocalCache.getUser().getUserId());
        req.setReqTime(DateUtil.getTimeStampStr());
        req.setStatus(Constants.FRIEND_REQ_STATUS.PENDING);
        friendReqDao.addFriendReq(req);
    }

    @Override
    public void delFriendShip(Long friendShipId) {
        FriendShip friendShip = friendShipDao.getFriendShipById(friendShipId);
        friendShip.setStatus(Constants.FRIEND_SHIP_STATUS.DELETE);
        checkUserRight(friendShip);
        friendShipDao.updateFriendShip(friendShip);
    }

    @Override
    public void blackFriendShip(Long friendShipId) {
        FriendShip friendShip = friendShipDao.getFriendShipById(friendShipId);
        friendShip.setStatus(Constants.FRIEND_SHIP_STATUS.BLACK);
        checkUserRight(friendShip);
        friendShipDao.updateFriendShip(friendShip);
    }

    @Override
    @Transactional
    public void agreeFriendReq(Long reqId) {
        FriendReq friendReq = friendReqDao.getFriendReqById(reqId);
        friendReq.setStatus(Constants.FRIEND_REQ_STATUS.AGREE);
        checkUserRight(friendReq);
        friendReq.setHandleTime(DateUtil.getTimeStampStr());
        friendReqDao.updateFriendReqStatus(friendReq);
        com.tksflysun.im.api.qo.UserQo userQo = new UserQo();
        userQo.setUserId(friendReq.getUserId());
        UserAttribute friendUser = userAttributeDao.getUserAttribute(friendReq.getUserId());
        FriendShip friendShip = new FriendShip();
        friendShip.setAddTimeStamp(DateUtil.getTimeStampStr());
        friendShip.setFriendId(friendReq.getUserId());
        friendShip.setLevel(1);
        friendShip.setOriginName(friendUser.getNickName());
        friendShip.setRemarkName(friendUser.getNickName());
        friendShip.setStatus(Constants.FRIEND_SHIP_STATUS.ACTIVE);
        friendShip.setUserId(UserLocalCache.getUser().getUserId());
        friendShipDao.addFriendShip(friendShip);
        friendShip.setUserId(friendShip.getFriendId());
        friendShip.setFriendId(UserLocalCache.getUser().getUserId());
        friendShip.setOriginName(UserLocalCache.getUser().getAttr().getNickName());
        friendShip.setRemarkName(UserLocalCache.getUser().getAttr().getNickName());
        friendShipDao.addFriendShip(friendShip);
    }

    @Override
    public void rejectFriendReq(Long reqId) {
        FriendReq friendReq = friendReqDao.getFriendReqById(reqId);
        friendReq.setStatus(Constants.FRIEND_REQ_STATUS.REJECT);
        checkUserRight(friendReq);
        friendReqDao.updateFriendReqStatus(friendReq);
    }

    @Override
    public List<FriendReq> getFriendReqsByFriendId(Long friendId) {
        return friendReqDao.getFriendReqByFriendId(friendId);
    }

    @Override
    public List<FriendShip> getFriendShipsByUserId(Long userId) {
        return friendShipDao.getFriendShipByUserId(userId);
    }

    /**
     * 验证用户权限
     * 
     * @param obj
     */
    private void checkUserRight(AuthPrivicyId obj) {
        if (!UserLocalCache.getUser().getUserId().equals(obj.getAuthPrivicyId())) {
            throw new BusinessException("用户没有权限", ControllerResult.NO_AUTHORITY);
        }
    }

    @Override
    public FriendShip getFriendShipByUserAndFriendId(Long friendShipId, Long userId) {
        return friendShipDao.getFriendShipByUserAndFriendId(friendShipId, userId);
    }

}
