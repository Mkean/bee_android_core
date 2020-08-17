package com.bee.home.fragment.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import com.bee.core.logger.CommonLogger
import com.bee.home.R
import com.bee.home.fragment.bean.HomeNativeBean
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_BANNER
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_COURSE
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_DEFAULT
import com.bee.home.fragment.widget.HomePageBannerView_New
import com.bee.home.fragment.widget.HomePageCourseView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.lang.Exception

/**
 *@Description: 首页加载--真实数据 使用的adapter
 *
 */
class HomeAdapter(
    data: List<HomeNativeBean>,
    private val parentFragment: Fragment
) : BaseMultiItemQuickAdapter<HomeNativeBean, BaseViewHolder>(data) {


    private var bannerMap: SparseArray<HomePageBannerView_New>? = null

    init {

        addItemType(TYPE_HOME_DEFAULT, R.layout.home_fragment_item_home_default) // 默认
        addItemType(TYPE_HOME_BANNER, R.layout.home_fragment_item_home_banner_new) // 轮播图
        addItemType(TYPE_HOME_COURSE, R.layout.home_fragment_item_home_course) // 课程

        initBannerList()
    }

    override fun convert(helper: BaseViewHolder?, item: HomeNativeBean?) {
        if (item != null) {
            when (item.type) {
                TYPE_HOME_BANNER -> {
                    val bannerView: HomePageBannerView_New? =
                        helper?.getView(R.id.home_item_top_banner_new)
                    bannerView?.setBannerData(item.banner_list)

                    putBanner(bannerView)
                }

                TYPE_HOME_COURSE -> {
                    // 课程
                    helper?.getView<HomePageCourseView>(R.id.home_item_top_course)
                        ?.setCourseData(item.course, parentFragment)
                }
            }
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        when (holder.itemViewType) {
            TYPE_HOME_BANNER -> {
                val view = holder.getView<HomePageBannerView_New>(R.id.home_item_top_banner_new)
                if (view != null) {
                    removeBanner(view)
                    view.release()
                }
            }
        }
    }

    private fun initBannerList() {
        if (bannerMap != null) {
            bannerMap!!.clear()
            bannerMap = null
        }
        bannerMap = SparseArray()
    }

    private fun putBanner(view: HomePageBannerView_New?) {
        if (view != null && bannerMap != null) {
            try {
                bannerMap!!.append(view.hashCode(), view)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun removeBanner(view: HomePageBannerView_New?) {
        if (bannerMap != null && view != null) {
            try {
                bannerMap!!.delete(view.hashCode())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startBanner() {
        if (bannerMap != null && bannerMap!!.size() > 0) {
            for (x in 0 until bannerMap!!.size()) {
                try {
                    val view = bannerMap!!.get(bannerMap!!.keyAt(x))
                    if (view != null) {
                        CommonLogger.e("首页", "banner 轮播开始")
                        view.onStartBanner()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun stopBanner() {
        if (bannerMap != null && bannerMap!!.size() > 0) {
            for (x in 0 until bannerMap!!.size()) {
                try {
                    val view = bannerMap!!.get(bannerMap!!.keyAt(x))
                    if (view != null) {
                        CommonLogger.e("首页", "banner 轮播停止")
                        view.onStopBanner()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}