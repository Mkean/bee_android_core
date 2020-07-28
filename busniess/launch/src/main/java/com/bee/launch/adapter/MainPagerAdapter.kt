package com.bee.launch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *@Description:
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val MAX_COUNT = 5
    }


    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null


        return fragment
    }

    override fun getCount(): Int = MAX_COUNT
}