package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bee.android.common.view.WrapContentHeightViewPager;
import com.bee.home.fragment.adapter.HomeCourseFrgAdapter;
import com.bee.home.fragment.fragment.HomeCourseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * @Description: 首页---学科整体View
 */
public class HomePageCourseView extends ConstraintLayout {

    private Context cOntext;
    private Fragment parentFragment;

    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private HomeCourseFrgAdapter adapter;


    private List<String> titles;

    private String currSubjectId = "-1"; // 当前正在显示的 课程 ID
    private int currPosition = 0;

    public HomePageCourseView(Context context) {
        super(context);
    }

    public HomePageCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomePageCourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
