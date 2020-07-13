package com.bee.android.common.view.pageState

interface IPageStateChange {
    fun showContent()

    fun showError(error: PageError)

    fun showEmpty()

    fun showLoading(loadType: Int)

    fun showLogin()
}