package com.bee.launch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *@Description:
 */
class MainPagerAdapter(fm: FragmentManager, private var fragments: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =fragments[position]

    override fun getCount(): Int = fragments.size
}