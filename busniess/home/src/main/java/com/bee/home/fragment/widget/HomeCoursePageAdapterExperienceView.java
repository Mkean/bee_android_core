package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.home.R;

/**
 * @Description: 首页---体验课View
 */
public class HomeCoursePageAdapterExperienceView extends ConstraintLayout {

    private Context context;

    public HomeCoursePageAdapterExperienceView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterExperienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_home_experience, this);
    }
}
