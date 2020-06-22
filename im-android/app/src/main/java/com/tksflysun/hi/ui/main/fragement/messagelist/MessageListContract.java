package com.tksflysun.hi.ui.main.fragement.messagelist;

import com.tksflysun.hi.base.IBaseView;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.LastMsg;

import java.util.List;

public class MessageListContract {
    interface Presenter {

    }

    interface View extends IBaseView {
        void refresh(List<LastMsg> msgs);

        void onError();

        void onSuccess();
    }
}
