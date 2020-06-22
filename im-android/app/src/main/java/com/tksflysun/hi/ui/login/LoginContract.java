package com.tksflysun.hi.ui.login;

import com.tksflysun.hi.base.IBaseView;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.bean.req.LoginReq;

public class LoginContract {
    interface  Presenter {
        /**
         * 登录方法
         */
        void login(LoginReq loginReq);

    }
    interface View extends IBaseView{
        /**
         * 调转到首页
         */
       void jumpToMain();
       void onLoginError();
       void onLoginSuccess(User user);
    }
}
