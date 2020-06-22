package com.tksflysun.hi.base;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public interface IBaseView {
    void showLoading();

    void hideLoading();

    void showToast( String message);

    void toLogin();

}
