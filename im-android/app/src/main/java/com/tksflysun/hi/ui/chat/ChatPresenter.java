package com.tksflysun.hi.ui.chat;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.tksflysun.hi.base.BasePresenter;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.PushCmdMsg;
import com.tksflysun.hi.bean.req.LoginReq;
import com.tksflysun.hi.bean.res.ApiResponse;
import com.tksflysun.hi.bean.res.PushCmdConstants;
import com.tksflysun.hi.bean.res.ResCode;
import com.tksflysun.hi.common.RetrofitManager;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.ChatMsgDao;
import com.tksflysun.hi.net.Api;
import com.tksflysun.hi.tcp.IMsgListener;
import com.tksflysun.hi.tcp.protobuf.Im;
import com.tksflysun.hi.ui.login.LoginContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChatPresenter extends BasePresenter<ChatContract.View> implements ChatContract.Presenter, IMsgListener {


    @Override
    public void doReceive(Object msg) {
        Log.i("消息",JSON.toJSONString(msg));
        if (msg instanceof ChatMsg) {
            ChatMsg chatMsg = (ChatMsg) msg;
            if (((ChatMsg) msg).getFriendId() != mView.getChatFriend().getFriendId()) {
                return;
            }
            if (chatMsg.getMsgType() == Im.MsgTypeEnum.PUSHCMD_VALUE) {
                PushCmdMsg pushCmdMsg = new Gson().fromJson(chatMsg.getMsg(),
                        PushCmdMsg.class);
                if (pushCmdMsg.getCmdType() == PushCmdConstants.VIDEO_CALL) {
                    mView.jumpToVideoChat(chatMsg.getFromUserId());
                    return;
                 }
                return;
            }
            List<ChatMsg> chatMsgs = new ArrayList<>();
            chatMsgs.add((ChatMsg) msg);
            mView.refresh(chatMsgs);
        }
    }

    @Override
    public void doReceiveList(List<Object> msgs) {
        Log.i("消息",JSON.toJSONString(msgs));
        if (msgs != null && msgs.size() != 0 && msgs.get(0) instanceof ChatMsg) {
            List<ChatMsg> list = new ArrayList<>();
            for (Object obj : msgs) {
                ChatMsg chatMsg = (ChatMsg) obj;

                if (chatMsg.getMsgType() == Im.MsgTypeEnum.PUSHCMD_VALUE) {
                    PushCmdMsg pushCmdMsg =
                            new Gson().fromJson(chatMsg.getMsg(),
                                    PushCmdMsg.class);
                    if (pushCmdMsg.getCmdType() == PushCmdConstants.VIDEO_CALL) {
                        mView.jumpToVideoChat(chatMsg.getFromUserId());
                        return;
                    }
                }
                if (((ChatMsg) obj).getFriendId() == mView.getChatFriend().getFriendId()) {
                    list.add((ChatMsg) obj);
                }
            }
            if (list.size() != 0 && mView != null) {
                mView.refresh(list);
            }
        }
    }
}

