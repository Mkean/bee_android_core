package com.bee.home.fragment.listener

/**
 *@Description:
 *
 */
interface HomeJzCommonPlayerListener {

    fun onPrepared()

    fun onStatePreparing()

    fun onStatePlaying()

    fun onStateAutoComplete()

    fun onStateError()

    fun onReset();
}