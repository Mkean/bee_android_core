package com.bee.launch

import com.bee.android.common.bean.UpdateBean
import com.bee.android.common.network.BaseResp
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *@Description:
 *
 */
interface LaunchApi {

    /**
     * 版本升级
     *
     * @param local_version
     * @param client_type
     */
    @GET("common/api/v1/version")
    fun updateApk(@Query("local_version") local_version: String, @Query("client_type") client_type: String): Observable<BaseResp<UpdateBean>>


    /**
     * 协议时间戳
     */
    @GET("common/api/v1/generalConfig/get")
    fun getProtocolTimeStamp(): Observable<BaseResp<Map<String, String>>>


}