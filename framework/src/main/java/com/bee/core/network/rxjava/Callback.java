package com.bee.core.network.rxjava;

import io.reactivex.observers.DisposableObserver;

public abstract class Callback<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {
            onCompleted();
    }

    public abstract void onFailure(Throwable e);

    public abstract void onSuccess(T t);

    public void onCompleted() {

    }
}
