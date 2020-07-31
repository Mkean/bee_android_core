package com.bee.android.common

import com.bee.android.common.bean.UserBean
import com.bee.android.common.network.BaseResp
import io.reactivex.Observable
import retrofit2.http.GET

/**
 *@Description:
 *
 */
interface CommonApi {

    /**
     * 用户资料
     *
     * https://test.imonkey.xueersi.com/ucenter/api/v1/account/profile/get
     */
    @GET("ucenter/api/v1/profile/get")
    fun getUserInfo():Observable<BaseResp<UserBean>>

    /**
     * 退出
     */
    @GET("ucenter/api/v1/auth/logout")
    fun logout():Observable<BaseResp<String>>
}