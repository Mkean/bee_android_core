package com.bee.android.common.network.config.okhttp.interceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

public class InterceptorArray {

    public static List<Interceptor> interceptors = new ArrayList<>();

    public static List<Interceptor> networkInterceptors = new ArrayList<>();
}
