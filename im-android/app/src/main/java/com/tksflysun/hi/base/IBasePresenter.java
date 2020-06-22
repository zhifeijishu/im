package com.tksflysun.hi.base;

public interface IBasePresenter<T extends  IBaseView> {
    void attachView(T view);

    void detachView();
}
