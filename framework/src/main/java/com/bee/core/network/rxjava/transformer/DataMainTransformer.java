package com.bee.core.network.rxjava.transformer;

import com.bee.core.network.exception.ApiException;
import com.bee.core.network.rxjava.RetryWithDelay;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 封装请求过程 全局统一的异常处理 MAIN
 *
 * @param <T>
 */
public class DataMainTransformer<T> implements ObservableTransformer<T, T> {
    private RetryWithDelay retryWithDelay;

    public DataMainTransformer() {
        this.retryWithDelay = new RetryWithDelay();
    }

    public DataMainTransformer(RetryWithDelay retryWithDelay) {
        this.retryWithDelay = retryWithDelay;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.onErrorResumeNext(new ErrorResumeFunction<T>()).compose(new ApplyMainTransformer<T>()).retryWhen(retryWithDelay);
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T> Response 对应的javaBean
     */
    private class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends T>> {

        @Override
        public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
            return Observable.error(ApiException.handleException(throwable));
        }
    }
}
