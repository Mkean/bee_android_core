package com.bee.launch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bee.course.fragment.CourseEntranceFragment
import com.bee.expand.fragment.ExpandEntranceFragment
import com.bee.home.fragment.HomeEntranceFragment
import com.bee.mall.fragment.MallEntranceFragment
import com.bee.mine.fragment.MyFragment

/**
 *@Description:
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val MAX_COUNT = 5
        const val TAB_POSITION_HOME = 0
        const val TAB_POSITION_COURSE = 1
        const val TAB_POSITION_EXERCISE = 2
        const val TAB_POSITION_MALL = 3
        const val TAB_POSITION_MINE = 4
    }

    private var mHomeEntranceFragment: HomeEntranceFragment? = null
    private var mCourseEntranceFragment: CourseEntranceFragment? = null
    private var mExpandEntranceFragment: ExpandEntranceFragment? = null
    private var mMallEntranceFragment: MallEntranceFragment? = null
    private var mMyFragment: MyFragment? = null


    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null

        when (position) {
            TAB_POSITION_HOME -> {
                if (mHomeEntranceFragment == null) {
                    mHomeEntranceFragment = HomeEntranceFragment()
                }
                fragment = mHomeEntranceFragment
            }
            TAB_POSITION_COURSE -> {
                if (mCourseEntranceFragment == null) {
                    mCourseEntranceFragment = CourseEntranceFragment()
                }
                fragment = mCourseEntranceFragment
            }
            TAB_POSITION_EXERCISE -> {
                if (mExpandEntranceFragment == null) {
                    mExpandEntranceFragment = ExpandEntranceFragment()
                }
                fragment = mExpandEntranceFragment;
            }
            TAB_POSITION_MALL -> {
                if (mMallEntranceFragment == null) {
                    mMallEntranceFragment = MallEntranceFragment()
                }

                fragment = mMallEntranceFragment
            }
            TAB_POSITION_MINE -> {
                if (mMyFragment == null) {
                    mMyFragment = MyFragment()
                }
                fragment = mMyFragment
            }
        }

        return fragment
    }

    override fun getCount(): Int = MAX_COUNT
}