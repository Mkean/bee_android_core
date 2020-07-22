package com.bee.core.network.rxjava;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private int maxRetries = 3; // 最大出错重试次数
    private int retryDelayMillis = 1000; // 重试间隔时间
    private int retryCount = 0; // 当前出错重试次数

    public RetryWithDelay() {
    }

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(throwable -> {

            if (throwable instanceof IOException) {
                if (++retryCount <= maxRetries) {
                    return Observable.timer(retryDelayMillis * retryCount, TimeUnit.MILLISECONDS);
                }
            }
            return Observable.error(throwable);
        });
    }
}
