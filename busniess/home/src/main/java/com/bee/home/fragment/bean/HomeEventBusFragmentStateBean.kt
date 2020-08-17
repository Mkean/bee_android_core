package com.bee.home.fragment.bean

import java.io.Serializable

/**
 *@Description: EventBus 对象，头像轮播动画是否执行
 *
 */
class HomeEventBusFragmentStateBean : Serializable {

    var isShow = false;

    constructor(isShow: Boolean) {
        this.isShow = isShow
    }
}