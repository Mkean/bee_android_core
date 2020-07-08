package com.bee.android.common.network.call;

import android.util.Log;

import com.bee.android.common.network.exception.ApiException;
import com.bee.android.common.network.exception.ErrorException;
import com.dianping.logan.BuildConfig;

public abstract class ResultCallback<T> extends Callback<T> {

    @Override
    public void onError(Throwable e) {
        onFailure(e);
        onFailed(handleException(e));
    }

    @Override
    public void onComplete() {
        onCompleted();
    }

    public abstract void onSuccess(T t);

    public void onCompleted() {
    }

    private ErrorException handleException(Throwable e) {
        ErrorException et;
        if (e instanceof ApiException.HttpThrowable) {
            ApiException.HttpThrowable httpThrowable = (ApiException.HttpThrowable) e;
            et = new ErrorException(httpThrowable.getCode() + "", httpThrowable.getMessage());
        } else {
            ApiException.ResponseThrowable responseThrowable = (ApiException.ResponseThrowable) e;
            et = new ErrorException(responseThrowable.getCode() + " ", responseThrowable.getMessage());
        }
        return et;
    }

    public void onFailed(ErrorException e) {

    }

    @Override
    public void onFailure(Throwable e) {
        if (e instanceof ApiException.HttpThrowable) {
            ApiException.HttpThrowable httpThrowable = (ApiException.HttpThrowable) e;
            // 如果是网络请求过程中或者json解析造成的失败，debug模式下log输出
            if (BuildConfig.DEBUG) {
                Log.d("net_error", httpThrowable.getCode() + "  :  " + httpThrowable.getMessage());
            }
        } else if (e instanceof ApiException.ResponseThrowable) {
            ApiException.ResponseThrowable responseThrowable = (ApiException.ResponseThrowable) e;
            // 如果是服务端非成功code错误，进行toast提示
            if (BuildConfig.DEBUG) {
                Log.d("net_error", responseThrowable.code + " :  " + responseThrowable.getMessage());
            }
        }
    }
}
