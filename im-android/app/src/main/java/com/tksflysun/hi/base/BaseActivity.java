package com.tksflysun.hi.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tksflysun.hi.R;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.ui.login.LogInActivity;
import com.wang.avi.AVLoadingIndicatorView;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IBaseView {
    private AVLoadingIndicatorView avi;
    protected T mPresenter;

    public void showLoading() {
        if (avi == null) {
            Log.i("loading", "初始化loading");
            setLoadingView();
        }
        avi.show();
    }


    public void hideLoading() {
        avi.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }

    public void setLoadingView() {
        avi = findViewById(getLoadingViewId());
    }

    public int getLoadingViewId() {
        return R.id.avi;
    }


    public abstract T initPresenter();

    //退出时销毁持有Activity
    @Override
    protected void onDestroy() {
        this.mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void toLogin() {
        Intent intent=new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
