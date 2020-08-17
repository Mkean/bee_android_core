package com.bee.home.fragment

import com.bee.android.common.network.BaseResp
import com.bee.home.fragment.bean.HomeNativeBean
import com.bee.home.fragment.bean.HomeSubjectItemBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *@Description: 首页--所有接口
 *
 */
interface HomeIClassApi {

    /**
     * 首页--所有数据
     */
    @GET("shop/api/v1/home")
    fun getHomeAllData(): Observable<BaseResp<HomeNativeBean>>


    /**
     * 首页--获取单科数据（弃用）
     */
    @GET("shop/api/v1/home_subject")
    fun getHomeSubjectData(@Query("subject_id") subject_id: String): Observable<BaseResp<ArrayList<HomeSubjectItemBean>>>
}