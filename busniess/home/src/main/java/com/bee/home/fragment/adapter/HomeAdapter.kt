package com.bee.home.fragment.adapter

import com.bee.home.fragment.bean.HomeNativeBean
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *@Description:
 *
 */
class HomeAdapter(data:List<HomeNativeBean>?):
    BaseMultiItemQuickAdapter<HomeNativeBean, BaseViewHolder>(
        data) {


    init {
        // 添加多种类
    }

    override fun convert(helper: BaseViewHolder?, item: HomeNativeBean?) {
        TODO("Not yet implemented")
    }

}