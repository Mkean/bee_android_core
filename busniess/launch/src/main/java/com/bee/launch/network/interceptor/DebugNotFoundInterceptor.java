package com.bee.launch.network.interceptor;

import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.utils.ToastUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DebugNotFoundInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        if (response.code() == 404) {
            CommonLogger.d("NetWorkManager", request.url() + " : " + response.code());

            Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        ToastUtils.showWarring("Http Code 404 url ：--> " + request.url() + " <-- 请检查路径");
                    });
        }
        return response;
    }
}
