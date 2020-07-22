package com.bee.android.common.base;

public interface IPresenter<T extends IView> {

    void attachView(T view);

    void detachView();
}
