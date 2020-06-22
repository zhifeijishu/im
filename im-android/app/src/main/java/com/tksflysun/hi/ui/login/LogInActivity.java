package com.tksflysun.hi.ui.login;

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
import com.tksflysun.hi.bean.req.LoginReq;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.ui.main.MainActivity;
import com.tksflysun.hi.ui.register.RegisterActivity;

public class LogInActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    //ImageView imageView;
    //TextView textView;
    // int count = 0;
    private Button loginBtn;
    private TextView toRegisterText;
    private TextInputLayout passwordText;
    private TextInputLayout phoneText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.loginBtn);
        toRegisterText = findViewById(R.id.toRegisterText);
        phoneText = findViewById(R.id.loginPhoneText);
        passwordText = findViewById(R.id.loginPasswordText);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordText.getEditText().getText().toString();
                String mobilePhone = phoneText.getEditText().getText().toString();


                LoginReq loginReq = new LoginReq();
                loginReq.setMobilePhone(mobilePhone);
                loginReq.setPassword(password);
                if (!checkInput(loginReq)) {
                    return;
                }
                mPresenter.login(loginReq);
            }
        });
        toRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        /** requestWindowFeature(Window.FEATURE_NO_TITLE);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.activity_login);
         imageView = findViewById(R.id.imageView);
         textView = findViewById(R.id.textView);
         imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
         public void onSwipeTop() {
         }

         public void onSwipeRight() {
         if (count == 0) {
         imageView.setImageResource(R.drawable.good_night_img);
         textView.setText("Night");
         count = 1;
         } else {
         imageView.setImageResource(R.drawable.good_morning_img);
         textView.setText("Morning");
         count = 0;
         }
         }

         public void onSwipeLeft() {
         if (count == 0) {
         imageView.setImageResource(R.drawable.good_night_img);
         textView.setText("Night");
         count = 1;
         } else {
         imageView.setImageResource(R.drawable.good_morning_img);
         textView.setText("Morning");
         count = 0;
         }
         }

         public void onSwipeBottom() {
         }

         });**/
    }

    private boolean checkInput(LoginReq loginReq) {
        if (TextUtils.isEmpty(loginReq.getMobilePhone())) {
            phoneText.setError("手机号不能为空");
            return false;
        } else {
            phoneText.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(loginReq.getPassword())) {
            passwordText.setError("密码不能为空");
            return false;

        } else {
            passwordText.setErrorEnabled(false);
        }
        return true;
    }


    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginError() {
        showToast("登录失败");
    }

    @Override
    public void onLoginSuccess(final User user) {
        ((HiApplication) getApplication()).cache(HiConstants.USREINFO, JSON.toJSONString(user));

    }
}
