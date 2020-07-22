package com.bee.android.common.base;

import androidx.lifecycle.LifecycleOwner;

import com.rxjava.rxlife.BaseScope;

/**
 * RxLife：一款轻量级的RxJava生命周期管理库，代码入侵极低，随用随取，不需要做任何准备工作，支持在Activity/Fragment的任意生命周期的方法断开管道。
 *
 *
 * @param <T>
 */
public class BasePresenter<T extends IView> extends BaseScope implements IPresenter<T> {

    protected T mView;

    public BasePresenter(LifecycleOwner owner) {
        super(owner);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
