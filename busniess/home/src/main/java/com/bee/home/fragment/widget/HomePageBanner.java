package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bee.android.common.view.banner.BannerScroller;
import com.bee.android.common.view.banner.BannerViewPager;
import com.bee.android.common.view.banner.OnBannerListener;
import com.bee.android.common.view.banner.WeakHandler;
import com.bee.android.common.view.indicator.BannerIndicator;
import com.bee.core.glide.ImageLoadingUtil;
import com.bee.home.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 */
public class HomePageBanner extends FrameLayout implements ViewPager.OnPageChangeListener {
    private String tag = "OrderBanner";

    private int delayTime = 4000;
    private int scrollTime = 1000;

    private boolean isAutoPlay = true;
    private boolean isScroll = true;

    private int startIndex;
    private int count = 0;
    private int currentItem;

    private Context context;

    private List<String> imageUrls;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;

    private BannerViewPager viewPager;
    private BannerIndicator bannerIndicator;

    private BannerPagerAdapter adapter;

    private BannerScroller mScroller;
    private OnBannerListener listener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private WeakHandler handler = new WeakHandler();
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    public HomePageBanner(@NonNull Context context) {
        super(context, null);
    }

    public HomePageBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomePageBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.imageViews = new ArrayList<>();
        this.indicatorImages = new ArrayList<>();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();

        View view = LayoutInflater.from(context).inflate(R.layout.home_fragment_view_banner_layout_new, this, true);
        viewPager = view.findViewById(R.id.home_bannerViewPager);
        bannerIndicator = view.findViewById(R.id.home_bannerIndicator);
        initViewPagerScroll();
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(context);
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, Objects.requireNonNull(e.getMessage()));
        }
    }

    public HomePageBanner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public HomePageBanner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public HomePageBanner setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set a {@link ViewPager.PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations tp each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return
     */
    public HomePageBanner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public HomePageBanner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    public HomePageBanner setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public void setPageMargin(int marginPixels) {
        if (viewPager != null) {
            viewPager.setPageMargin(marginPixels);
        }
    }

    public HomePageBanner setImages(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public void update(List<String> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    public HomePageBanner start() {
        setImageList(imageUrls);
        setData();
        return this;
    }

    private void setImageList(List<String> imageUrl) {
        if (imageUrl == null || imageUrl.size() <= 0) {
            return;
        }
        if (imageUrl.size() == 1) {
            HomePageBannerItem itemImg = new HomePageBannerItem(getContext());
            itemImg.loadUrl(imageUrl.get(0));
            imageViews.add(itemImg);
        } else {
            for (int i = 0; i <= count + 1; i++) {
                HomePageBannerItem itemImg = new HomePageBannerItem(getContext());
                String url;
                if (i == 0) {
                    url = imageUrl.get(count - 1);
                } else if (i == count + 1) {
                    url = imageUrl.get(0);
                } else {
                    url = imageUrl.get(i - 1);
                }
                itemImg.loadUrl(url);
                imageViews.add(itemImg);
            }
        }
    }

    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(currentItem);
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }

        if (imageViews != null && imageViews.size() > 1) {
            if (isAutoPlay) {
                startAutoPlay();
            }
            bannerIndicator.setVisibility(View.VISIBLE);
            bannerIndicator.setUpWithViewPager(viewPager);
        } else {
            bannerIndicator.setVisibility(View.GONE);
        }
    }

    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        switch (state) {
            case 0: // No operation
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1: // start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2: // end Sliding
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return
     */
    private int toRealPosition(int position) {
        int realPosition = 0;
        if (count != 0) {
            realPosition = (position - 1) % count;
        }
        if (realPosition < 0) {
            realPosition += count;
        }

        return realPosition;
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }

        int realPosition = toRealPosition(position);
        for (int i = 0; i < indicatorImages.size(); i++) {
            // 选中的页面改变小圆点为选中状态，反之为未选中
            if (realPosition == 1) {
                indicatorImages.get(i).setBackgroundResource(R.drawable.common_shape_bg_radius_45_fd8a6b);
            } else {
                indicatorImages.get(i).setBackgroundResource(R.drawable.common_shape_bg_radius_45_ebebeb);
            }
        }
    }

    public HomePageBanner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void onDestroy() {
        // 释放内存
        handler.removeCallbacks(task);
        if (imageViews != null && imageViews.size() > 0) {
            for (View imageView : imageViews) {
                ImageLoadingUtil.INSTANCE.clearViewCache(context, imageView);
            }
            imageViews.clear();
        }
    }

    class BannerPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            if (listener != null) {
                view.setOnClickListener(v -> {
                    listener.OnBannerClick(toRealPosition(position));
                });
            }
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }

}
