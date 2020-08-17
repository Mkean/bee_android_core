package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bee.android.common.view.WrapContentHeightViewPager;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;
import com.bee.home.fragment.adapter.HomeCourseFrgAdapter;
import com.bee.home.fragment.bean.HomeSubjectBean;
import com.bee.home.fragment.fragment.HomeCourseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 首页--学科整体View
 */
public class HomePageCourseView extends ConstraintLayout {

    private Context context;

    private Fragment parentFragment;

    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;

    private HomeCourseFrgAdapter adapter;

    private List<HomeCourseFragment> fragments;
    private List<String> titles;

    private String currSubjectId = "-1"; // 当前正在显示的 课程 id
    private int currPosition = 0;


    public HomePageCourseView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomePageCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomePageCourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_course, this);
        tabLayout = findViewById(R.id.home_view_course_tab);
        viewPager = findViewById(R.id.home_view_course_viewpager);
        viewPager.setOffscreenPageLimit(3);
    }

    public void setCourseData(List<HomeSubjectBean> data, Fragment fragment) {
        if (fragment != null && data != null && data.size() > 0) {
            setVisibility(VISIBLE);
            CommonLogger.e("首页--创建", "创建HomePageCourseView_setCourseData");
            this.parentFragment = fragment;

            createFragment(data);
            createTitles(data);
            initTabLayout(data);
            initViewPager(data);
            initTab();
        } else {
            setVisibility(GONE);
        }
    }

    private void initTabLayout(List<HomeSubjectBean> data) {
        if (tabLayout != null && data != null && data.size() > 0) {
            if (tabLayout.getTabCount() > 0) {
                tabLayout.removeAllTabs();
            }

            int adapterCount = data.size();
            for (int i = 0; i < adapterCount; i++) {
                TabLayout.Tab tab = tabLayout.newTab();
                tabLayout.addTab(tab, false);
            }
            int currentItem = viewPager.getCurrentItem();
            CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_currentItem==" + currentItem);
            CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_tabLayout.getSelectedTabPosition()==" + tabLayout.getSelectedTabPosition());
            CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_currPosition==" + currPosition);
            CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_currSubjectId==" + currSubjectId);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try {
                        changeTabTextView(tab, true, data.get(tab.getPosition()).getSubject_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    try {
                        changeTabTextView(tab, false, data.get(tab.getPosition()).getSubject_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout");
            CommonLogger.e("首页--创建", "创建HomePageCourseView_TabLayout_TabCount==" + tabLayout.getTabCount());
        }
    }

    private void initViewPager(List<HomeSubjectBean> data) {
        if (viewPager != null && fragments != null && fragments.size() > 0 && parentFragment != null) {
            CommonLogger.e("TAG", fragments.size() + "");

            adapter = new HomeCourseFrgAdapter(parentFragment.getChildFragmentManager(), fragments, titles);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            if (currPosition > 0) {
                viewPager.setCurrentItem(currPosition, false);
            }
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        currSubjectId = data.get(position).getSubject_id();
                        CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_PageChangeListener_currSubjectId==" + currSubjectId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            CommonLogger.e("首页--创建", "创建HomePageCourseView_initViewPager");
        }
    }

    private void initTab() {
        if (tabLayout != null && tabLayout.getTabCount() > 0) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    try {
                        if (currPosition == i) {
                            changeTabTextView(tab, true, titles.get(i));
                        } else {
                            changeTabTextView(tab, false, titles.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void createFragment(List<HomeSubjectBean> data) {
        if (data != null && data.size() > 0) {
            currPosition = 0;
            if (fragments == null) {
                fragments = new ArrayList<>();
            }
            if (fragments.size() > 0) {
                fragments.clear();
            }

            for (HomeSubjectBean subjectBean : data) {
                if (subjectBean != null) {
                    HomeCourseFragment fragment = HomeCourseFragment.newInstance(subjectBean, data.indexOf(subjectBean));
                    fragments.add(fragment);

                    // 如果是第一次，默认是-1
                    if (currSubjectId.equals("-1")) {
                        currSubjectId = subjectBean.getSubject_id();
                        currPosition = data.indexOf(subjectBean);
                        CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_createFragments(-1)_currSubjectId==" + currSubjectId);
                        CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_createFragments(-1)_currPosition==" + currPosition);
                    } else if (currSubjectId.equals(subjectBean.getSubject_id())) {
                        // 如果不是第一次，判断是否相等
                        currPosition = data.indexOf(subjectBean);
                        CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_createFragments(-1)_currSubjectId==" + currSubjectId);
                        CommonLogger.e("首页--创建", "创建HomePageCourseView_initTabLayout_createFragments(-1)_currPosition==" + currPosition);
                    }
                }
                CommonLogger.e("首页--创建", "创建HomePageCourseView_createFragments");
            }

        }
    }

    private void createTitles(List<HomeSubjectBean> data) {
        if (data != null && data.size() > 0) {
            if (titles == null) {
                titles = new ArrayList<>();
            }
            if (titles.size() > 0) {
                titles.clear();
            }
            for (HomeSubjectBean subjectBean : data) {
                if (subjectBean != null) {
                    titles.add(StringUtils.getFiltedNullStr(subjectBean.getSubject_name()));
                }
            }
            CommonLogger.e("首页--创建", "创建HomePageCourseView_createTitles");
        }
    }

    private void changeTabTextView(TabLayout.Tab tab, boolean big, String subjectName) {
        if (tab != null) {
            View view = tab.getCustomView();
            if (view == null) {
                tab.setCustomView(R.layout.home_fragment_view_tab_layout);
            }
            if (tab.getCustomView() != null) {
                TextView textView = tab.getCustomView().findViewById(R.id.home_view_tab_item_tv);
                if (textView != null) {
                    textView.setText(subjectName);
                    if (big) {
                        textView.setTextAppearance(context, R.style.HomeTabLayoutBoldTextSize);
                    } else {
                        textView.setTextAppearance(context, R.style.HomeTabLayoutNormalTextSize);
                    }
                }
            }
        }
    }
}
