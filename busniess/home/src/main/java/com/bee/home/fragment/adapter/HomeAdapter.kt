package com.bee.home.fragment.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import com.bee.home.R
import com.bee.home.fragment.bean.HomeNativeBean
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_BANNER
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_COURSE
import com.bee.home.fragment.bean.HomeNativeBean.Companion.TYPE_HOME_DEFAULT
import com.bee.home.fragment.widget.HomePageBannerView_New
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *@Description:
 *
 */
class HomeAdapter(data: List<HomeNativeBean>, fragment: Fragment) :
    BaseMultiItemQuickAdapter<HomeNativeBean, BaseViewHolder>(
        data
    ) {

    private var bannerMap: SparseArray<HomePageBannerView_New>? = null

    init {
        addItemType(TYPE_HOME_DEFAULT, R.layout.home_fragment_item_home_default) // 默认
        addItemType(TYPE_HOME_BANNER, R.layout.home_fragment_item_home_banner_new) // 轮播图
        addItemType(TYPE_HOME_COURSE, R.layout.home_fragment_item_home_course) // 课程相关

        initBannerList()
    }

    override fun convert(helper: BaseViewHolder?, item: HomeNativeBean?) {
        TODO("Not yet implemented")
    }

    private fun initBannerList() {
        if (bannerMap != null) {
            bannerMap!!.clear()
            bannerMap = null
        }
        bannerMap = SparseArray()
    }

}