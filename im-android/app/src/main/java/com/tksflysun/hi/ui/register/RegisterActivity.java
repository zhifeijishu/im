package com.tksflysun.hi.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.textfield.TextInputLayout;
import com.tksflysun.hi.R;
import com.tksflysun.hi.base.BaseActivity;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.bean.req.RegisterReq;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.ui.login.LogInActivity;
import com.tksflysun.hi.ui.main.MainActivity;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    private TextView toLoginText;
    private Button registerBtn;
    private TextInputLayout passwordText;
    private TextInputLayout phoneText;
    private TextInputLayout inviteCodeText;
    private TextInputLayout confirmPasswordText;
    private TextInputLayout nickNameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        passwordText = findViewById(R.id.registerPasswordText);
        phoneText = findViewById(R.id.registerPhoneText);
        inviteCodeText = findViewById(R.id.registerInviteCode);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        toLoginText = findViewById(R.id.toLoginText);
        nickNameText = findViewById(R.id.registerNickNameText);
        toLoginText.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent =
                                                       new Intent(getApplicationContext(), LogInActivity.class);
                                               startActivity(intent);
                                           }
                                       }

        );
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password =
                        passwordText.getEditText().getText().toString();
                String confirmPassword =
                        confirmPasswordText.getEditText().getText().toString();
                String mobilePhone =
                        phoneText.getEditText().getText().toString();
                String inviteCode =
                        inviteCodeText.getEditText().getText().toString();
                RegisterReq registerReq = new RegisterReq(mobilePhone,
                        password, inviteCode, confirmPassword,
                        nickNameText.getEditText().getText().toString());
                if (checkReq(registerReq)) {
                    mPresenter.register(registerReq);
                }
            }
        });
    }

    private boolean checkReq(RegisterReq registerReq) {
        if (TextUtils.isEmpty(registerReq.getMobilePhone())) {
            phoneText.setError("手机号不能为空");
            return false;
        } else {
            phoneText.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(registerReq.getPassword())) {
            passwordText.setError("密码不能为空");
            return false;
        } else {
            passwordText.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(registerReq.getInviteCode())) {
            passwordText.setError("请输入邀请码");
            return false;
        } else {
            passwordText.setErrorEnabled(false);
        }
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            confirmPasswordText.setError("两次输入密码不一样");
            return false;
        } else {
            confirmPasswordText.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRegisterError() {
        showToast("注册失败");
    }

    @Override
    public void onRegisterSuccess(User user) {
        ((HiApplication) getApplication()).cache(HiConstants.USREINFO,
                JSON.toJSONString(user));
    }
}
