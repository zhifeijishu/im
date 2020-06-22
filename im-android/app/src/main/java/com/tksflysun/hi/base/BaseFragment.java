package com.tksflysun.hi.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tksflysun.hi.R;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.ui.login.LogInActivity;
import com.wang.avi.AVLoadingIndicatorView;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IBaseView {
     protected T mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }


    public int getLoadingViewId() {
        return R.id.avi;
    }


    public abstract T initPresenter();

    //退出时销毁持有Activity
    @Override
    public void onDestroy() {
        this.mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void toLogin() {
        Intent intent=new Intent(getActivity(), LogInActivity.class);
        startActivity(intent);
    }
}
