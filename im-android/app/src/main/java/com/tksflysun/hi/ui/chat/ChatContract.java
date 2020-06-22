package com.tksflysun.hi.ui.chat;

import com.tksflysun.hi.base.IBaseView;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.req.LoginReq;

import java.util.List;

public class ChatContract {
    interface Presenter {
    }

    interface View extends IBaseView {
        void refresh(List<ChatMsg> msgs);

        Friend getChatFriend();

        void jumpToVideoChat(long friendId);
    }
}
