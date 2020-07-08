package com.bee.android.common.network.rxjava.transformer;

import com.bee.android.common.network.exception.ApiException;
import com.bee.android.common.network.rxjava.RetryWithDelay;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 封装请求过程 全局统一的异常处理 IO
 *
 * @param <T>
 */
public class DataIOTransformer<T> implements ObservableTransformer<T, T> {

    private RetryWithDelay retryWithDelay;

    public DataIOTransformer() {
        this.retryWithDelay = new RetryWithDelay();
    }

    public DataIOTransformer(RetryWithDelay retryWithDelay) {
        this.retryWithDelay = retryWithDelay;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.onErrorResumeNext(new ErrorResumeFunction<T>()).compose(new ApplyIOTransformer<T>()).retryWhen(retryWithDelay);
    }

    /**
     * 非服务器产生的异常，比如本地无网络请求，Json数据解析错误等等
     *
     * @param <T> Response 对应的javaBean
     */
    private class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends T>> {

        @Override
        public ObservableSource<? extends T> apply(Throwable throwable) {
            return Observable.error(ApiException.handleException(throwable));
        }
    }
}
