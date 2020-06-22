package com.tksflysun.hi.ui.main.fragement.friendlist;

import com.tksflysun.hi.base.IBaseView;
import com.tksflysun.hi.bean.Friend;

import java.util.List;

public class FriendListContract {
    interface  Presenter {
        /**
         * 获取好友列表
         */
        void getFriends();

    }
    interface View extends IBaseView{
       void refreshFriendListView(List<Friend> friends);
       void onError();
       void onSuccess();
    }
}
