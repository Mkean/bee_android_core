package com.bee.android.common.network.config.cookies;


import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2016/ic_yello_ball/28.
 */
public class CookiesManager implements CookieJar {

    private PersistentCookieStore cookieStore;


    public CookiesManager(Context context) {
        this.cookieStore = new PersistentCookieStore(context);
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.addAll(url.uri(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.getCookies();
    }
}
