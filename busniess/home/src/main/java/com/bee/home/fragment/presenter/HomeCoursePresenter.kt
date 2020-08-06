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

    //------------------------------------单科数据请求----------------------------------------
    /**
     * 根据产品需求：不再请求这个接口：已废弃
     */
    fun getSubjectData(subjectId: String) {
        API.service(HomeIClassApi::class.java)
            .getHomeSubjectData(subjectId)
            .compose(BaseTranFormer())
            .`as`(RxLife.`as`(this))
            .subscribe(object : ResultCallback<ArrayList<HomeSubjectItemBean>>() {
                override fun onSuccess(itemBeans: ArrayList<HomeSubjectItemBean>?) {
                    CommonLogger.e("首页--接口", "学科数据接口成功")
                    if (mView != null) {
                        if (itemBeans != null && itemBeans.size > 0) {
                            subjectDataSuccess(itemBeans)
                        } else {
                            CommonLogger.e("首页--接口", "学科数据接口成功-返回无数据")
                        }
                    }
                }

                override fun onCommonFailure(e: ErrorException?) {
                    super.onCommonFailure(e)
                    CommonLogger.e("首页--接口", "学科数据接口失败")
                    CommonLogger.e("首页--接口", "学科数据接口失败==" + e.toString())
                    if (mView != null) {
                        // 显示加载失败提醒
                    }
                }
            })

    }

    private fun subjectDataSuccess(itemBeans: ArrayList<HomeSubjectItemBean>) {
        if (mView != null) {
            if (itemBeans.size > 0) {
                CommonLogger.e("首页--接口", "学科数据接口成功--铺设数据")
                mView.setSubjectData(itemBeans)
            } else {

            }
        }
    }
}