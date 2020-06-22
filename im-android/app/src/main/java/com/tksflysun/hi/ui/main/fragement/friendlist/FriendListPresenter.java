package com.tksflysun.hi.ui.main.fragement.friendlist;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.base.BasePresenter;
import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.res.ApiResponse;
import com.tksflysun.hi.bean.res.ResCode;
import com.tksflysun.hi.common.RetrofitManager;
import com.tksflysun.hi.dao.FriendDao;
import com.tksflysun.hi.net.Api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FriendListPresenter extends BasePresenter<FriendListContract.View> implements FriendListContract.Presenter {


    @Override
    public void getFriends( ) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),"");
        //步骤4：创建Retrofit对象


        // 步骤5：创建 网络请求接口 的实例
        final Api request = RetrofitManager.getInstance().create(Api.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<ApiResponse<List<Friend>>> observable = request.getFriends( );
        mView.showLoading();

        // 步骤7：发送网络请求
        observable.subscribeOn(Schedulers.io())               // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程 处理请求结果
                .subscribe(new Observer<ApiResponse<List<Friend>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApiResponse<List<Friend>> result) {
                        new FriendDao().insertAll(result.getData());
                        if(mView==null){
                           return;
                        }
                        mView.hideLoading();
                        if (ResCode.SUCCESS.equals(result.getCode())) {
                            Log.i("获取好友列表成功", JSON.toJSONString(result));
                            mView.onSuccess();
                            mView.refreshFriendListView(result.getData());
                        } else {
                            if (ResCode.NO_LOGIN.equals(result.getCode())){
                                mView.toLogin();
                                return;
                            }
                            Log.i("获取好友列表失败", JSON.toJSONString(result));
                            mView.onError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView==null){
                            return;
                        }
                        mView.onSuccess();
                        Log.e("获取好友列表失败", e.getMessage());
                        mView.onError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

