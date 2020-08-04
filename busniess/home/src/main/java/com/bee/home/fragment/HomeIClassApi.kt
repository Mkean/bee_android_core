package com.bee.home.fragment

import com.bee.android.common.network.BaseResp
import com.bee.home.fragment.bean.HomeNativeBean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 *@Description: 首页---首页：所有接口
 *
 */
interface HomeIClassApi {

    /**
     * 首页--所有数据
     *
     */
    @GET("shop/api/v1/home")
    fun getHomeAllData(): Observable<BaseResp<HomeNativeBean>>
}