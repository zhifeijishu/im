package com.tksflysun.hi.ui.main.fragement.messagelist;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.base.BasePresenter;
import com.tksflysun.hi.bean.ChatMsg;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.LastMsg;
import com.tksflysun.hi.bean.res.ApiResponse;
import com.tksflysun.hi.bean.res.ResCode;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.common.RetrofitManager;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.dao.LastMsgDao;
import com.tksflysun.hi.net.Api;
import com.tksflysun.hi.tcp.IMsgListener;
import com.tksflysun.hi.ui.main.fragement.friendlist.FriendListContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.List;

public class MessageListPresenter extends BasePresenter<MessageListContract.View> implements MessageListContract.Presenter, IMsgListener {

    @Override
    public void doReceive(Object msg) {

    }

    @Override
    public void doReceiveList(List<Object> msgs) {
        if (msgs != null && msgs.size() != 0 && msgs.get(0) instanceof ChatMsg) {
            List<LastMsg> lastMsgs =
                    new LastMsgDao().queryByUserId(HiApplication.getInstance().getUser().getUserId());
            mView.refresh(lastMsgs);
        }
    }
}

