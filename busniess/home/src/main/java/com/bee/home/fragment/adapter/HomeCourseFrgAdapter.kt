package com.bee.home.fragment.adapter

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bee.home.fragment.fragment.HomeCourseFragment

/**
 *@Description:
 *
 */
class HomeCourseFrgAdapter(
    fm: FragmentManager,
    private val fragments: List<HomeCourseFragment>?,
    private val titles: List<String>?
) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): HomeCourseFragment? = fragments?.get(position)


    override fun getCount(): Int = fragments?.size ?: 0

    override fun getPageTitle(position: Int): CharSequence? {
        if (titles != null && titles.isNotEmpty() && position < titles.size) {
            return titles[position]
        }
        return super.getPageTitle(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        try {
            super.destroyItem(container, position, `object`)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
