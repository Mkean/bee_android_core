package com.bee.android.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.bee.android.common.R;

/**
 * Base dialog 基类
 */
public class BaseDialog extends Dialog {


    protected Context context;
    protected Activity activity;
    protected boolean keyCanBack = true;

    public BaseDialog(Context context) {
        super(context, R.style.CommonRemindDialog);
        this.context = context;
        setWindowParams();
        setHelperOwnerActivity();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setWindowParams();
        setHelperOwnerActivity();
    }

    private void setWindowParams() {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.common_color_transparent);
        }
    }

    private void setHelperOwnerActivity() {
        if (context != null) {
            try {
                activity = (Activity) context;
            } catch (Exception e) {
                activity = null;
                e.printStackTrace();
            }
        }
        if (activity != null) {
            setOwnerActivity(activity);
        }
    }

    public void setCanceledOnTouchOutsideAndKeyBack(boolean canceled,
                                                    boolean kanBack) {
        setCanceledOnTouchOutside(canceled);
        setKeyCanBack(kanBack);
    }

    public void setKeyCanBack(boolean bl) {
        this.keyCanBack = bl;
    }

    public boolean getKeyCanBack() {
        return keyCanBack;
    }

    @Override
    public void onBackPressed() {
        if (keyCanBack) {
            super.onBackPressed();
        }
    }

    protected void onTouchOutside() {
        //点击黑色屏幕外的位置。
        Log.i("BaseDialog", "baseDialog抓取dialog外面的的黑色区域");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 触摸外部弹窗 */
        if (isOutOfBounds(context, event)) {
            onTouchOutside();
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }


    @Override
    public void show() {
        try {
            if (activity != null && !activity.isFinishing() && !isShowing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dismiss() {
        try {
            if (activity != null && !activity.isFinishing() && isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ActionListener {
        void clickLeft();

        void clickRight();
    }

    public interface ButtonListener {
        void clickButton();
    }
}
