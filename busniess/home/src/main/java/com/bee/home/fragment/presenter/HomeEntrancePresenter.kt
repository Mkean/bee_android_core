package com.bee.home.fragment.presenter

import androidx.lifecycle.LifecycleOwner
import com.bee.android.common.base.BasePresenter
import com.bee.android.common.network.rxjava.BaseTranFormer
import com.bee.android.common.network.rxjava.ErrorException
import com.bee.android.common.network.rxjava.ResultCallback
import com.bee.core.logger.CommonLogger
import com.bee.core.network.API
import com.bee.home.fragment.HomeIClassApi
import com.bee.home.fragment.bean.HomeNativeBean
import com.bee.home.fragment.contract.HomeEntranceContract
import com.rxjava.rxlife.RxLife

/**
 *@Description:
 *
 */
class HomeEntrancePresenter(owner: LifecycleOwner) :
    BasePresenter<HomeEntranceContract.View>(owner),
    HomeEntranceContract.Presenter {

    /**
     * 请求页面所有数据
     */
    fun getServerData() {
        API.service(HomeIClassApi::class.java).getHomeAllData()
            .compose(BaseTranFormer())
            .`as`(RxLife.`as`(this))
            .subscribe(object : ResultCallback<HomeNativeBean>() {
                override fun onSuccess(homeNativeBean: HomeNativeBean?) {
                    CommonLogger.e("首页-接口", "所有数据接口成功")
                    if (mView != null) {
                        if (homeNativeBean != null) {
                            addDataSuccess(homeNativeBean)
                        } else {
                            // 显示整体无数据页面

                            mView.showErrorView(!mView.isHasData, "")
                        }
                    }
                }

                override fun onCommonFailure(e: ErrorException?) {
                    super.onCommonFailure(e)
                    CommonLogger.e("首页—接口", "所有数据接口失败")

                    if (mView != null) {
                        // 显示家在失败提醒

                        var code = ""
                        if (e != null) {
                            CommonLogger.e("首页-接口", "所有数据接口失败——${e.toString()}")
                            code = e.code
                        }

                        // 正式数据
                        mView.showErrorView(!mView.isHasData, code)

                        mView.finishRefreshAndLoadMore()
                    }
                }

            })

    }

    private fun addDataSuccess(serverBean: HomeNativeBean?) {
        if (serverBean != null) {
            val beanList = ArrayList<HomeNativeBean>()
            if (serverBean.banner_list != null && serverBean.banner_list!!.isNotEmpty()) {
                val bannerBean: HomeNativeBean = HomeNativeBean()
                bannerBean.type = HomeNativeBean.TYPE_HOME_BANNER
                bannerBean.banner_list = serverBean.banner_list
                beanList.add(bannerBean)
            }
            if (serverBean.course != null && serverBean.course!!.isNotEmpty()) {
                val courseBean: HomeNativeBean = HomeNativeBean()
                courseBean.type = HomeNativeBean.TYPE_HOME_COURSE
                courseBean.course = serverBean.course
                beanList.add(courseBean)
            }

            CommonLogger.e("首页", "服务器数据铺设==${beanList.size}")

            if (isDataSuccess(beanList)) {
                // 数据完整
                mView.setServerData(beanList)
            } else {
                // 显示整体无数据页面
                mView.showErrorView(!mView.isHasData, "")
            }
        }

    }

    /**
     *判断数据完整性
     * <p>
     * 根据产品需求，如果banner轮播没有数据，或者每个学科返回了字段，但是卡片数据无数据，认为是数据不完整
     *
     * @param beanList 服务器数据
     * @return {@code true}：数据完整， {@code false}：数据不完整
     */
    private fun isDataSuccess(beanList: ArrayList<HomeNativeBean>?): Boolean {
        if (beanList != null && beanList.isNotEmpty()) {
            var beanCount = 0

            for (nativeBean in beanList) {
                if (nativeBean.isDataSuccess()) {
                    beanCount++
                } else {
                    break
                }
            }

            return beanCount == beanList.size
        }
        return false
    }
}