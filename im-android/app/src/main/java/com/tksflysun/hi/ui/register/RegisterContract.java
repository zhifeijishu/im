package com.tksflysun.hi.ui.register;

import com.tksflysun.hi.base.IBaseView;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.bean.req.RegisterReq;

public class RegisterContract {
    interface Presenter {
        /**
         * 注册
         */
        void register(RegisterReq registerReq);
    }

    interface View extends IBaseView {
        /**
         * 调转到首页
         */
        void jumpToMain();

        void onRegisterError();

        void onRegisterSuccess(User user);

    }
}
