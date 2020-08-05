package com.bee.home.fragment.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bee.home.fragment.fragment.HomeCourseFragment;

import java.util.List;

/**
 * @Description: 首页学科---加载Fragment的adapter
 */
public class HomeCourseFrgAdapter extends FragmentPagerAdapter {

    private List<HomeCourseFragment> fragments;
    private List<String> titles;


    public HomeCourseFrgAdapter(FragmentManager fm, List<HomeCourseFragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.size() > 0 && position < titles.size()) {
            return titles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            super.destroyItem(container, position, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
