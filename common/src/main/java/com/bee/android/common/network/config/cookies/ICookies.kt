package com.bee.android.common.network.config.cookies

import android.content.Context
import okhttp3.CookieJar

interface ICookies {
    /**
     * 添加 cookie
     */
    fun getCookiesManager(context: Context): CookieJar?
}