package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.home.R;

/**
 * @Description: 首页--顶部title
 */
public class HomePageAdapterHeaderView extends ConstraintLayout {

    private Context context;

    private TextView nameTv;

    public HomePageAdapterHeaderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomePageAdapterHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomePageAdapterHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_item_home_tv_header,this);
        nameTv = findViewById(R.id.header_name_tv);
    }

    public TextView getNameTv() {
        return nameTv;
    }
}
