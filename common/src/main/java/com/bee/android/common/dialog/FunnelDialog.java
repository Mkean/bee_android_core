package com.bee.android.common.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bee.android.common.R;

/**
 * 沙漏加载弹框
 */
public class FunnelDialog extends BaseDialog {

    private String msg;
    private LottieAnimationView mLottieView;


    public FunnelDialog(Context context, String msg) {
        super(context, R.style.common_toast_CommonRemindDialog);
        this.msg = msg;
    }

    public FunnelDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_dialog_funnel_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        TextView messageTv = findViewById(R.id.message);
        mLottieView = findViewById(R.id.lottieView);
        mLottieView.setRepeatCount(ValueAnimator.INFINITE);
        messageTv.setText(msg);
    }

    @Override
    public void show() {
        super.show();
        mLottieView.playAnimation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mLottieView.cancelAnimation();
    }
}
