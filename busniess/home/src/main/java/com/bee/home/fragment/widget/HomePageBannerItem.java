package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.core.glide.ImageLoadingUtil;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;

/**
 * @Description: OrderBannerItem
 */
public class HomePageBannerItem extends ConstraintLayout {

    private ImageView imageView;

    public HomePageBannerItem(Context context) {
        super(context);
        initView();
    }

    public HomePageBannerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HomePageBannerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.home_item_banner_new, this);
        imageView = findViewById(R.id.home_item_banner_iv);
    }

    public void loadUrl(String url) {
        if (imageView != null) {
            if (!StringUtils.isNullOrEmpty(url)) {
                CommonLogger.e("首页-banner", "onBindView_url==" + url);
                ImageLoadingUtil.INSTANCE.loadRoundedCorners(imageView,
                        url,
                        R.drawable.common_banner_default,
                        imageView,
                        getContext().getResources().getDimensionPixelSize(R.dimen.size_10dp),
                        0);
            } else {
                ImageLoadingUtil.INSTANCE.loadImg(imageView,
                        R.drawable.common_banner_default,
                        imageView);
            }
        }
    }
}
