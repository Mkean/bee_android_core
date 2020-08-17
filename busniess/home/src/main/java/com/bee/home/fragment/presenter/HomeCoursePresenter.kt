package com.bee.home.fragment.presenter

import androidx.lifecycle.LifecycleOwner
import com.bee.android.common.base.BasePresenter
import com.bee.android.common.network.rxjava.BaseTranFormer
import com.bee.android.common.network.rxjava.ErrorException
import com.bee.android.common.network.rxjava.ResultCallback
import com.bee.core.logger.CommonLogger
import com.bee.core.network.API
import com.bee.home.fragment.HomeIClassApi
import com.bee.home.fragment.bean.HomeSubjectItemBean
import com.bee.home.fragment.contract.HomeCourseContract
import com.rxjava.rxlife.RxLife

/**
 *@Description:
 *
 */
class HomeCoursePresenter(owner: LifecycleOwner) : BasePresenter<HomeCourseContract.View>(owner),
    HomeCourseContract.Presenter {

    /**
     *  根据产品要求：不再使用这个接口，已废弃
     */
    @Deprecated("废弃", ReplaceWith("getServerData()"))
    fun getSubjectData(subjectId: String) {
        API.service(HomeIClassApi::class.java)
            .getHomeSubjectData(subjectId)
            .compose(BaseTranFormer())
            .`as`(RxLife.`as`(this))
            .subscribe(object : ResultCallback<ArrayList<HomeSubjectItemBean>>() {

                override fun onSuccess(itemBean: ArrayList<HomeSubjectItemBean>?) {
                    CommonLogger.e("首页--接口", "学科数据接口成功")
                    if (mView != null) {
                        if (itemBean != null && itemBean.isNotEmpty()) {
                            subjectDataSuccess(itemBean)
                        } else {
                            CommonLogger.e("首页--接口", "学科数据接口成功--返回无数据")
                        }
                    }
                }

                override fun onCommonFailure(e: ErrorException?) {
                    super.onCommonFailure(e)
                    CommonLogger.e("首页--接口", "学科数据接口失败")
                    CommonLogger.e("首页--接口", "学科数据接口失败==${e.toString()}")
                }
            })

    }

    private fun subjectDataSuccess(itemBean: ArrayList<HomeSubjectItemBean>?) {
        if (mView != null) {
            if (itemBean != null && itemBean.isNotEmpty()) {
                CommonLogger.e("首页--接口", "学科数据接口成功——铺设数据")
                mView.setSubjectData(itemBean);
            } else {

            }
        }
    }

}