package com.bee.android.common.network.rxjava;

import android.text.TextUtils;

import com.bee.android.common.network.BaseResp;
import com.bee.core.network.exception.ApiException;
import com.bee.core.network.rxjava.RetryWithDelay;
import com.bee.core.network.rxjava.transformer.ApplyMainTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * @Description: 基于BaseResp 业务 bean 进行封装，判断成功code 直接返回base中的data数据
 */
public class BaseTranFormer<T> implements ObservableTransformer<BaseResp<T>, T> {

    private RetryWithDelay retryWithDelay;

    public BaseTranFormer() {
        this.retryWithDelay = new RetryWithDelay();
    }

    public BaseTranFormer(RetryWithDelay retryWithDelay) {
        this.retryWithDelay = retryWithDelay;
    }

    @Override
    public ObservableSource apply(Observable upstream) {
        return upstream.onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>())
                .compose(new ApplyMainTransformer<>())
                .retryWhen(retryWithDelay);
    }


    /**
     * 非服务器产生的异常，比如本地无网络请求，json数据解析错误等等。
     *
     * @param <T>
     */
    private class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BaseResp<T>>> {

        @Override
        public ObservableSource<? extends BaseResp<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(ApiException.handleException(throwable));

        }
    }

    /**
     * 服务器返回的异常
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private class ResponseFunction<T> implements Function<BaseResp<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(BaseResp<T> tResponse) throws Exception {
            String code = tResponse.code;
            String msg = tResponse.msg;
            if (isSuccessCode(code)) {
                return handlerResponse(tResponse.data);
            } else {
                int codeInt = Integer.parseInt(code);
                return Observable.error(new ApiException.ResponseThrowable(codeInt, msg));
            }
        }
    }

    private boolean isSuccessCode(String code) {
        if (TextUtils.isEmpty(code)) return false;

        return "200".equals(code);
    }

    private <T> ObservableSource<T> handlerResponse(final T t) {
        return new Observable<T>() {
            @Override
            protected void subscribeActual(Observer<? super T> observer) {
                try {
                    observer.onNext(t);
                    observer.onComplete();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        };
    }
}
