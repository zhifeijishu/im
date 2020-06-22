package com.tksflysun.hi.ui.register;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tksflysun.hi.base.BasePresenter;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.bean.req.RegisterReq;
import com.tksflysun.hi.bean.res.ApiResponse;
import com.tksflysun.hi.bean.res.ResCode;
import com.tksflysun.hi.common.RetrofitManager;
import com.tksflysun.hi.net.Api;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {


    @Override
    public void register(RegisterReq registerReq) {
        Log.i("注册信息", JSON.toJSONString(registerReq));
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "application/json"), JSON.toJSONString(registerReq));
        // 步骤5：创建 网络请求接口 的实例
        final Api request = RetrofitManager.getInstance().create(Api.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<ApiResponse<User>> observable =
                request.register(requestBody);
        mView.showLoading();
        // 步骤7：发送网络请求
        observable.subscribeOn(Schedulers.io())               // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程 处理请求结果
                .subscribe(new Observer<ApiResponse<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApiResponse<User> result) {
                        if (mView == null) {
                            return;
                        }
                        mView.hideLoading();

                        // 步骤8：对返回的数据进行处理
                        if (ResCode.SUCCESS.equals(result.getCode())) {
                            Log.i("注册成功", JSON.toJSONString(result));
                            mView.onRegisterSuccess(result.getData());
                            mView.jumpToMain();
                        } else {
                            Log.e("注册失败", result.getCode());
                            mView.onRegisterError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView == null) {
                            return;
                        }
                        mView.hideLoading();
                        Log.e("注册失败", e.getMessage());
                        mView.onRegisterError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

