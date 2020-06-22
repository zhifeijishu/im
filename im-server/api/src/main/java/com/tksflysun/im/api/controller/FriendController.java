package com.tksflysun.im.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tksflysun.im.api.service.FriendService;
import com.tksflysun.im.api.util.UserLocalCache;
import com.tksflysun.im.common.bean.FriendReq;
import com.tksflysun.im.common.bean.FriendShip;
import com.tksflysun.im.common.result.ControllerResult;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @RequestMapping(path = "/addreq", method = RequestMethod.POST)
    public ControllerResult addFriendReq(@RequestBody FriendReq req) {
        ControllerResult result = ControllerResult.getSuccessResult();

        FriendShip friendShip =
            friendService.getFriendShipByUserAndFriendId(req.getFriendId(), UserLocalCache.getUser().getUserId());
        if (friendShip != null) {
            result.setData("已经在你的好友列表中");
            return result;
        }
        friendService.addFriendReq(req);
        return result;

    }

    @RequestMapping(path = "/delship/{friendShipId}", method = RequestMethod.POST)
    public ControllerResult delFriendShip(@PathVariable Long friendShipId) {
        ControllerResult result = ControllerResult.getSuccessResult();
        friendService.delFriendShip(friendShipId);
        return result;
    }

    @RequestMapping(path = "/blackship/{friendShipId}", method = RequestMethod.POST)
    public ControllerResult blackFriendShip(@PathVariable Long friendShipId) {
        ControllerResult result = ControllerResult.getSuccessResult();
        friendService.blackFriendShip(friendShipId);
        return result;
    }

    @RequestMapping("/agreereq/{reqId}")
    public ControllerResult agreeFriendReq(@PathVariable("reqId") Long reqId) {
        ControllerResult result = ControllerResult.getSuccessResult();
        friendService.agreeFriendReq(reqId);
        return result;
    }

    @RequestMapping(path = "/rejectreq/{reqId}", method = RequestMethod.POST)
    public ControllerResult rejectFriendReq(@PathVariable("reqId") Long reqId) {
        ControllerResult result = ControllerResult.getSuccessResult();
        friendService.rejectFriendReq(reqId);
        return result;
    }

    @RequestMapping(path = "/friends", method = RequestMethod.POST)
    public ControllerResult getUserFriends() {
        ControllerResult result = ControllerResult.getSuccessResult();
        result.setData(friendService.getFriendShipsByUserId(UserLocalCache.getUser().getUserId()));
        return result;
    }

    @RequestMapping(path = "/friendreqs", method = RequestMethod.POST)
    public ControllerResult getUserFriendReqs() {
        ControllerResult result = ControllerResult.getSuccessResult();
        result.setData(friendService.getFriendReqsByFriendId(UserLocalCache.getUser().getUserId()));
        return result;
    }
}
